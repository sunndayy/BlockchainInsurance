const orderBusiness = require('./business');

class OrderController {
	constructor(req, res) {
		this.req = req;
		this.res = res;
	}
	
	async exec(business) {
		try {
			let result = await business();
			if (Buffer.isBuffer(result)) {
				this.res.set({'Content-Type': 'image/gif'});
				this.res.end(result);
			} else {
				this.res.json(result);
				
			}
		} catch (e) {
			this.res.json({
				error: e.message
			});
		}
	}
	
	async createOrder() {
		return await orderBusiness.createOrder(this.req.body, this.req.file);
	}
	
	async updateOrder() {
		return await orderBusiness.updateOrder(parseInt(this.req.params.id), this.req.body);
	}
	
	static async getOrders() {
		return await orderBusiness.getOrders();
	}
	
	async getImage() {
		return await orderBusiness.getImage(parseInt(this.req.params.id));
	}
}

module.exports.createOrder = async (req, res) => {
	let controller = new OrderController(req, res);
	await controller.exec(controller.createOrder.bind(controller));
};

module.exports.updateOrder = async (req, res) => {
	let controller = new OrderController(req, res);
	await controller.exec(controller.updateOrder.bind(controller));
};

module.exports.getOrders = async (req, res) => {
	let controller = new OrderController(req, res);
	await controller.exec(OrderController.getOrders.bind(controller));
};

module.exports.getImage = async (req, res) => {
	let controller = new OrderController(req, res);
	await controller.exec(controller.getImage.bind(controller));
};