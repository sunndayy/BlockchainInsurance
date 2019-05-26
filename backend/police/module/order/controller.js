const orderBusiness = require('./business');

class OrderController {
	constructor(req, res) {
		this.req = req;
		this.res = res;
	}
	
	async exec(business) {
		try {
			this.res.json(await business());
		} catch (e) {
			this.res.json({
				error: e.message
			});
		}
	}
	
	async getOrder() {
		return orderBusiness.getOrder(this.req.params.uid);
	}
	
	static async getOrders() {
		return orderBusiness.getOrders();
	}
	
	async createOrder() {
		return orderBusiness.createOrder(this.req.body);
	}
	
	async updateOrder() {
		return orderBusiness.updateOrder(this.req.params.uid, this.req.body);
	}
}

module.exports.getOrder = async (req, res) => {
	let controller = new OrderController(req, res);
	return controller.exec(controller.getOrder.bind(controller));
};

module.exports.getOrders = async (req, res) => {
	let controller = new OrderController(req, res);
	return controller.exec(OrderController.getOrders);
};

module.exports.createOrder = async (req, res) => {
	let controller = new OrderController(req, res);
	return controller.exec(controller.createOrder.bind(controller));
};

module.exports.updateOrder = async (req, res) => {
	let controller = new OrderController(req, res);
	return controller.exec(controller.updateOrder.bind(controller));
};