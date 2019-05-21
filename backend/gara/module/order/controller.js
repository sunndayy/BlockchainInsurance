const OrderBusiness = require('./business');

class OrderController {
	constructor(req, res) {
		this.req = req;
		this.res = res;
	}
	
	createOrder(cb) {
		OrderBusiness.createOrder(this.req.body, this.req.file, cb);
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
	
	async getImage() {
		let buffer = await OrderBusiness.getImage(parseInt(this.req.params.id));
		if (buffer) {
			this.res.set({'Content-Type': 'image/gif'});
			this.res.end(buffer);
		} else {
			this.res.json({
				error: 'Image not found'
			});
		}
	}
}

module.exports.createOrder = (req, res) => {
	let controller = new OrderController(req, res);
	controller.createOrder((e, order) => {
		if (e) {
			res.json({
				error: e.message
			});
		} else {
			res.json(order);
		}
	});
};

module.exports.getImage = async (req, res) => {
	let controller = new OrderController(req, res);
	try {
		await controller.getImage();
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