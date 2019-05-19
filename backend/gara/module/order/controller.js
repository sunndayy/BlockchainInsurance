const OrderBusiness = require('./business');

class OrderController {
	constructor(req, res) {
		this.req = req;
		this.res = res;
	}
	
	async createOrder() {
		let order = await OrderBusiness.createOrder(req.body);
		this.res.json(order);
	}
	
	async updateOrder() {
		let order = await OrderBusiness.updateOrder(req.params.id, req.body);
		this.res.json(order);
	}
	
	async getOrders() {
		let orders = await OrderBusiness.getOrders();
		this.res.json(orders);
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
		});
	}
};

module.exports.getOrderes = async (req, res) => {
	try {
		let controller = new OrderController(req, res);
		await controller.getOrders();
	} catch (e) {
		res.json({
			error: e.message
		});
	}
};