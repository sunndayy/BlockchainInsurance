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
	
	async createTx() {
		let tx = await txBusiness.createTx(this.req.body);
		this.res.json(tx);
	}
	
	async updateTx() {
		let tx = await txBusiness.updateTx(this.req.params.id, this.req.body);
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

module.exports.createTx = async (req, res) => {
	try {
		let controller = new TxController(req, res);
		await controller.createTx();
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
