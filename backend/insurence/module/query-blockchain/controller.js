const queryBusiness = require('./business');

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
				this.res.json(JSON.parse(body));
			} catch (e) {
				this.res.json({
					error: e.message
				});
			}
		}
	}
	
	queryPlan() {
		queryBusiness.getAllPlans((e, body) => {
			this.handleAfterQuery(e, body);
		});
	}
	
	queryContract() {
		queryBusiness.getAllContracts((e, body) => {
			this.handleAfterQuery(e, body);
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