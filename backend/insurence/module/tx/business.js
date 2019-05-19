const Tx = require('./model');
const request = require('request');
const config = require('../../config');
const sign = require('../../utils/sign');

module.exports.createTx = async txInfo => {
	return await Tx.create(txInfo);
};

module.exports.updateTx = async (id, txInfo) => {
	delete txInfo.id;
	let tx = await Tx.findOneAndUpdate({id}, {$set: txInfo}, {new: true});
	if (tx) {
		if (tx.state) {
			let msg = {
				type: tx.type,
				ref: tx.ref,
				preStateHash: tx.preStateHash,
				action: tx.action
			};
			config.defaultNodes.forEach(node => {
				request.post({url: node, form: sign(msg)}, (e, res, body) => {
					if (e) {
						console.error(e);
					}
				});
			});
		}
	}
};

module.exports.getAllTx = async () => {
	return await Tx.find({}).sort({time: -1});
};