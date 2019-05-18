const OrderBusiness = require('./business');

class OrderController {
	constructor(req, res) {
		this.req = req;
		this.res = res;
	}
	
	async createOrder() {
		await OrderBusiness.createOrder(req.body);
	}
	
	async updateOrder() {
		await OrderBusiness.updateOrder(req.params.id, req.body);
	}
}

module.exports.createOrder = async (req, res) => {
	try {
		let controller = new OrderController(req, res);
		await controller.createOrder();
	} catch (e) {
		res.json({
			error: e.message
		});
	}
};

module.exports.updateOrder = async (req, res) => {
	try {
		let controller = new OrderController(req, res);
		await controller.updateOrder();
	} catch (e) {
		res.json({
			error: e.message
		})
	}
};