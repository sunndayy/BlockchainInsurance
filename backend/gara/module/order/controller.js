const OrderBusiness = require('./business');

class OrderController {
	constructor(req, res) {
		this.req = req;
		this.res = res;
	}
	
	async createOrder() {
		let order = await OrderBusiness.createOrder(this.req.body);
		this.res.json(order);
	}
	
	updateOrder() {
		OrderBusiness.updateOrder(parseInt(this.req.params.id), this.req.body, (e, order) => {
			if (e) {
				this.res.json({
					error: e.message
				});
			} else {
				this.res.json(order);
			}
		});
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

module.exports.updateOrder = (req, res) => {
	let controller = new OrderController(req, res);
	controller.updateOrder();
};

module.exports.getOrders = async (req, res) => {
	try {
		let controller = new OrderController(req, res);
		await controller.getOrders();
	} catch (e) {
		res.json({
			error: e.message
		});
	}
};