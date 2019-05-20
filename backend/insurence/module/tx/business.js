const Tx = require('./model');
const request = require('request');
const config = require('../../config');
const sign = require('../../utils/sign');
const sha256 = require('sha256');

const syncBlockchain = async msg => {
	let _sign = sign(msg);
	config.defaultNodes.forEach(node => {
		request.post({url: `http://${node}/tx`, form: _sign}, (e, res, body) => {
			if (e) {
				console.error(e);
			}
		});
	});
};

const getPreStateHash = (ref, cb) => {
	let msg = {
		header: 'GET_PLANS_BY_COMPANY',
		company: config.myCompany,
		time: new Date()
	};
	let _sign = sign(msg);
	let failCount = 0;
	
	let queryStatePromise = new Promise((resolve, reject) => {
		config.defaultNodes.forEach(node => {
			request.post({url: `http://${node}/get-plans-by-company`, form: _sign}, (e, res, body) => {
				if (e) {
					failCount++;
					if (failCount === config.defaultNodes.length) {
						return reject(new Error('Cannot connect to blockchain'));
					}
				} else {
					let plans = JSON.parse(body);
					for (let i = 0; i < plans.length; i++) {
						if (plans[i].id === ref.plan.id) {
							return resolve(sha256(JSON.stringify(plans[i].term)));
						}
					}
				}
			});
		});
	});
	
	queryStatePromise.then(preStateHash => {
		cb(preStateHash);
	}).catch(e => {
		console.error(e);
	})
};

module.exports.updateTx = async (id, txInfo) => {
	delete txInfo.id;
	let tx = await Tx.findOneAndUpdate({id: id}, {$set: txInfo}, {new: true});
	if (tx) {
		if (tx.status && tx.type === 'CONTRACT') {
			getPreStateHash(tx.ref, preStateHash => {
				syncBlockchain({
					type: tx.type,
					ref: tx.ref,
					preStateHash: preStateHash,
					action: {
						create: true
					}
				});
			});
		}
	}
};

module.exports.getAllTx = async () => {
	return await Tx.find({}).sort({time: -1});
};


module.exports.createContractTx = async txInfo => {
	delete txInfo.action;
	return await Tx.create(txInfo);
};

module.exports.createPlanTx = async txInfo => {
	syncBlockchain({
		type: txInfo.type,
		ref: txInfo.ref,
		preStateHash: null,
		action: txInfo.action
	});
	txInfo.status = true;
	return await Tx.create(txInfo);
};