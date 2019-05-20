const Order = require('./model');
const request = require('request');
const config = require('../../config');
const crypto = require('../../utils/crypto');

module.exports.createOrder = async order => {
	return await Order.create(order);
};

const validateContract = (order, contract, curTime) => {
	return contract.garaPubKeyHashes.indexOf(crypto.getPubKeyHash(config.myPrivKey)) >= 0
		&& contract.userInfo.licensePlate === order.licensePlate
		&& curTime >= contract.expireTime.timeStart && curTime <= contract.expireTime.timeEnd
		&& order.insurence.company === contract.plan.company && order.insurence.id === contract.plan.id;
};

const calBalance = (order, contract) => {
	let sum = 0;
	contract.refunds.forEach(refund => {
		sum += refund.refund;
	});
	let maxRefund = contract.plan.term.maxRefund;
	return maxRefund - sum;
};

const orderToRefund = (order, contract, curTime) => {
	let balance = calBalance(order, contract);
	if (balance > 0) {
		return {
			total: order.total,
			refund: (balance >= order.total * contract.plan.term.percentage) ? order.total * contract.plan.term.percentage : balance,
			time: curTime,
			garaPubKeyHash: crypto.getPubKeyHash(config.myPrivKey)
		};
	}
	return null;
};

const refundToMsg = (newRefund, contract) => {
	return {
		type: 'CONTRACT',
		ref: {
			plan: {
				company: contract.plan.company,
				id: contract.plan.id
			},
			userInfo: contract.userInfo,
			garaPubKeyHashes: contract.garaPubKeyHashes,
			expireTime: contract.expireTime
		},
		preStateHash: crypto.hash(JSON.stringify(contract.refunds)),
		action: {
			update: {
				push: newRefund
			}
		}
	};
};

module.exports.updateOrder = (id, order, cb) => {
	delete order.id;
	
	if (order.status) {
		request(`http://${config.company[order.insurence.company]}/contracts-by-license-plate/${order.licensePlate}`, (e, res, body) => {
				if (e) {
					cb(e, null);
				} else {
					try {
						let curTime = new Date().getTime();
						
						let contracts = JSON.parse(body);
						for (let i = 0; i < contracts.length; i++) {
							if (validateContract(order, contracts[i], curTime)) {
								let newRefund = orderToRefund(order, contracts[i], curTime);
								if (newRefund) {
									let msg = refundToMsg(newRefund, contracts[i]);
									let sign = crypto.sign(msg);
									config.defaultNodes.forEach(node => {
										request.post({url: `http://${node}/tx`, form: sign}, (e, res, body) => {
											if (e) {
												console.error(e);
											} else {
												console.log(body.toString());
											}
										});
									});
									
									order.insurence.refund = newRefund.refund;
									Order.findOneAndUpdate({id}, {$set: {order}}, {upsert: true}, (e, doc) => {
										if (e) {
											cb(e, null);
										} else {
											cb(null, doc);
										}
									});
									return;
								}
							}
						}
						
						cb(new Error('Cannot find contract'), null);
					} catch (e) {
						cb(e, null);
					}
				}
			}
		);
	}
};

module.exports.getOrders = async () => {
	return await Order.find({});
};