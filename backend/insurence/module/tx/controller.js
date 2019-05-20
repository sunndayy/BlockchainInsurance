const txBusiness = require('./business');

class TxController {
	constructor(req, res) {
		this.req = req;
		this.res = res;
	}
	
	async getAllTx() {
		let txs = await txBusiness.getAllTx();
		this.res.json(txs);
	}
	
	async updateTx() {
		let tx = await txBusiness.updateTx(parseInt(this.req.params.id), this.req.body);
		this.res.json(tx);
	}
	
	async createPlanTx() {
		let tx = await txBusiness.createPlanTx(this.req.body);
		this.res.json(tx);
	}
	
	async createContractTx() {
		let tx = await txBusiness.createContractTx(this.req.body);
		this.res.json(tx);
	}
}

module.exports.getAllTx = async (req, res) => {
	try {
		let controller = new TxController(req, res);
		await controller.getAllTx();
	} catch (e) {
		res.json({
			error: e.message
		});
	}
};

module.exports.updateTx = async (req, res) => {
	try {
		let controller = new TxController(req, res);
		await controller.updateTx();
	} catch (e) {
		res.json({
			error: e.message
		});
	}
};

module.exports.createPlanTx = async (req, res) => {
	try {
		let controller = new TxController(req, res);
		await controller.createPlanTx();
	} catch (e) {
		res.json({
			error: e.message
		});
	}
};

module.exports.createContractTx = async (req, res) => {
	try {
		let controller = new TxController(req, res);
		await controller.createContractTx();
	} catch (e) {
		res.json({
			error: e.message
		});
	}
};