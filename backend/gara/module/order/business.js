const Order = require('./model');
const request = require('request');
const config = require('../../config');
const crypto = require('../../utils/crypto');

module.exports.createOrder = async orderInfo => {
	return await Order.create(orderInfo);
};

module.exports.updateOrder = async (id, orderInfo) => {
	delete orderInfo.id;
	let order = await Order.findOneAndUpdate({id}, {$set: orderInfo}, {new: true});
	
	if (order.state) {
		request(`http://${config.company[orderInfo.insurence.company]}/contracts-by-license-plate/${orderInfo.insurence.licensePlate}`, (e, res, body) => {
			if (e) {
				console.error(e);
			} else {
				try {
					let stringFromRef = ref => {
						return JSON.stringify({
								company: ref.plan.company,
								id: ref.plan.id
							})
							+ JSON.stringify(ref.userInfo)
							+ JSON.stringify(ref.garaPubKeyHashes)
							+ JSON.stringify(ref.expireTime);
					};
					
					let contracts = JSON.parse(body);
					let contract = contracts.find(contract => {
						return contract.garaPubKeyHashes.indexOf(crypto.getPubKeyHash(config.myPrivKey)) >= 0
							&& stringFromRef(order.insurence.ref) === stringFromRef(contract.ref);
					});
					
					if (contract) {
						let sign = crypto.sign({
							type: 'CONTRACT',
							ref: {
								plan: {
									company: contract.ref.plan.company,
									id: contract.ref.plan.id
								},
								userInfo: contract.ref.userInfo,
								garaPubKeyHashes: contract.ref.garaPubKeyHashes,
								expireTime: contract.ref.expireTime
							},
							preStateHash: crypto.hash(JSON.stringify(contract.ref.plan.term) + JSON.stringify(contract.refunds)),
							action: {
								update: {
									push: {
										garaPubKeyHash: crypto.getPubKeyHash(config.myPrivKey),
										total: order.total,
										refund: order.total * contract.ref.plan.term.percentage,
										time: new Date().getTime()
									}
								}
							}
						});
						
						config.defaultNodes.forEach(node => {
							request.post({url: `http://${node}/tx`, form: sign}, (e, res, body) => {
								if (e) {
									console.error(e);
								} else {
									console.log(body.toString())
								}
							});
						});
					}
				} catch (e) {
					console.error(e);
				}
			}
		});
	}
};

module.exports.getOrders = async () => {
	return await Order.find({});
};