const request = require('request');
const config = require('../../config');
const sign = require('../../utils/sign');

const query = (msg, url, cb, failCount, success) => {
	let sign = sign(msg);
	request.post({url, form: sign}, (e, res, body) => {
		if (e) {
			failCount++;
			if (failCount === config.defaultNodes.length) {
				cb(new Error('Cannot connect to blockchain'), null);
			}
		} else {
			if (!success) {
				cb(null, body.toString());
				success = true;
			}
		}
	});
};

const callApiToBlockchain = (msg, url, cb) => {
	let success = false;
	let failCount = 0;
	config.defaultNodes.forEach(host => {
		query(msg, 'http://' + host + url, cb, failCount, success);
	});
};

module.exports.getAllPlans = cb => {
	callApiToBlockchain({
		header: 'GET_PLANS_BY_COMPANY',
		company: config.myCompany,
		time: new Date()
	}, '/get-plans-by-company', cb);
};

module.exports.getAllContracts = cb => {
	callApiToBlockchain({
		header: 'GET_CONTRACTS_BY_COMPANY',
		company: config.myCompany,
		time: new Date()
	}, '/get-contracts-by-company', cb);
};

const getContractsByLicensePlate = (licensePlate, cb) => {
	callApiToBlockchain({
		header: 'GET_CONTRACTS_BY_LICENSE_PLATE',
		company: licensePlate,
		// licensePlate,
		time: new Date()
	}, '/get-contracts-by-license-plate', cb);
};

module.exports.getContractsByLicensePlate = (licensePlate, cb) => {
	getContractsByLicensePlate(licensePlate, (e, body) => {
		if (e) {
			cb(e, null);
		} else {
			let contracts = JSON.parse(body);
			contracts = contracts.filter(contract => {
				return contract.plan.company === config.myCompany;
			});
			cb(null, JSON.stringify(contracts));
		}
	});
};