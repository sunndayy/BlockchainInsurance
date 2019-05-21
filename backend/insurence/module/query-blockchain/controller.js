const queryBusiness = require('./business');
const config = require('../../config');

class QueryController {
	constructor(req, res) {
		this.req = req;
		this.res = res;
	}
	
	handleAfterQuery(e, body) {
		if (e) {
			this.res.json({
				error: e.message
			});
		} else {
			try {
				let data = JSON.parse(body);
				this.res.json(data);
			} catch (e) {
				this.res.json({
					error: e.message
				});
			}
		}
	}
	
	queryPlan() {
		let failCount = 0;
		let success = false;
		queryBusiness.getAllPlans((e, body) => {
			if (e) {
				failCount++;
				if (failCount === config.defaultNodes.length) {
					this.handleAfterQuery(new Error('Cannot connect to blockchain'), null);
				}
			} else {
				if (!success) {
					success = true;
					this.handleAfterQuery(null, body);
				}
			}
		});
	}
	
	queryContract() {
		let failCount = 0;
		let success = false;
		queryBusiness.getAllContracts((e, body) => {
			if (e) {
				failCount++;
				if (failCount === config.defaultNodes.length) {
					this.handleAfterQuery(new Error('Cannot connect to blockchain'), null);
				}
			} else {
				if (!success) {
					success = true;
					try {
						let plans = JSON.parse(body);
						let contracts = [];
						for (let i = 0; i < plans.length; i++) {
							for (let j = 0; j < plans[i].contracts.length; j++) {
								let contract = Object.assign({
									company: plans[i].company,
									id: plans[i].id
								}, plans[i].contracts[j]);
								contracts.push(contract);
							}
						}
						this.handleAfterQuery(null, JSON.stringify(contracts));
					} catch (e) {
						this.handleAfterQuery(e, null);
					}
				}
			}
		});
	}
	
	queryContractByLicensePlate() {
		let failCount = 0;
		let success = false;
		queryBusiness.getAllContracts((e, body) => {
			if (e) {
				failCount++;
				if (failCount === config.defaultNodes.length) {
					this.handleAfterQuery(new Error('Cannot connect to blockchain'), null);
				}
			} else {
				if (!success) {
					success = true;
					try {
						let plans = JSON.parse(body);
						let contracts = [];
						for (let i = 0; i < plans.length; i++) {
							for (let j = 0; j < plans[i].contracts.length; j++) {
								let contract = Object.assign({
									company: plans[i].company,
									id: plans[i].id
								}, plans[i].contracts[j]);
								if (contract.userInfo.licensePlate === this.req.params.licensePlate) {
									contract.plan = plans[i];
									contracts.push(contract);
								}
							}
						}
						this.handleAfterQuery(null, JSON.stringify(contracts));
					} catch (e) {
						this.handleAfterQuery(e, null);
					}
				}
			}
		});
	}
}

module.exports.queryPlan = (req, res) => {
	let controller = new QueryController(req, res);
	controller.queryPlan();
};

module.exports.queryContract = (req, res) => {
	let controller = new QueryController(req, res);
	controller.queryContract();
};

module.exports.queryContractByLicensePlate = (req, res) => {
	let controller = new QueryController(req, res);
	controller.queryContractByLicensePlate();
};