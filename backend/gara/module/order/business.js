const Order = require('./model');
const request = require('request');
const config = require('../../config');
const crypto = require('../../utils/crypto');
const fs = require('fs');

module.exports.createOrder = async (order, imageFile) => {
	if (imageFile) {
		return new Promise((resolve, reject) => {
			fs.readFile(imageFile.path, (e, data) => {
				if (e) {
					reject(e);
				} else {
					order.image = data;
					Order.create(order, (e, order) => {
						if (e) {
							reject(e);
						} else {
							delete order._doc.image;
							resolve(order);
						}
					});
				}
			});
		});
	}
	return Order.create(order);
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
		preStateHash: crypto.hash(JSON.stringify(contract.plan.term) + JSON.stringify(contract.refunds)),
		action: {
			update: {
				push: newRefund
			}
		}
	};
};

module.exports.updateOrder = async (id, order) => {
	delete order.id;
	
	if (order.status) {
		return new Promise((resolve, reject) => {
				request(`http://${config.company[order.insurence.company]}/contracts-by-license-plate/${order.licensePlate}`, async (e, res, body) => {
						if (e) {
							reject(e);
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
														console.log(JSON.stringify(body.toString()));
													}
												});
											});
											
											order.insurence.refund = newRefund.refund;
											order = await Order.findOneAndUpdate({id}, {
												$set: {
													insurence: order.insurence,
													status: true
												}
											}, {new: true});
											return resolve(order);
										}
									}
								}
								reject(new Error('Cannot create refund'));
							} catch (e) {
								reject(e);
							}
						}
					}
				);
			}
		);
	}
	
	return await Order.findOneAndUpdate({id}, {$set: order}, '-image');
};

module.exports.getOrders = async () => {
	return await Order.find({}, '-image');
};

module.exports.getImage = async id => {
	let order = await Order.findOne({id: id}, 'image');
	if (order) {
		return order.image;
	}
	return null;
};