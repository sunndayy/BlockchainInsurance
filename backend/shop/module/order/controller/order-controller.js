const orderBusiness = require('../business/order-business');
const mongoose = require('mongoose');
const User = mongoose.model('user');

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
	
	async getAllOrders() {
		if (this.req.user.role !== 0) {
			throw new Error('Not authorization');
		}
		return await orderBusiness.getAllOrders();
	}
	
	async getOrdersByStatus() {
		if (this.req.user.role !== 0) {
			throw new Error('Not authorization');
		}
		return await orderBusiness.getOrdersByStatus(this.req.params.status === 'true');
	}
	
	async getOrdersByUser() {
		let user;
		if (this.req.user.role === 0) {
			user = await User.findOne({username: this.req.params.username});
		} else {
			user = this.req.user;
		}
		if (!user) {
			throw new Error('Invalid username');
		}
		return await orderBusiness.getOrdersByUser(user);
	}
	
	async createOrder() {
		return await orderBusiness.createOrder(this.req.user, this.req.body);
	}
	
	async updateOrder() {
		if (this.req.user.role !== 0) {
			throw new Error('Not authorization');
		}
		return await orderBusiness.updateOrder(this.req.params.id, this.req.body);
	}
}

module.exports.getAllOrders = async (req, res) => {
	let controller = new OrderController(req, res);
	await controller.exec(controller.getAllOrders.bind(controller));
};

module.exports.getOrdersByStatus = async (req, res) => {
	let controller = new OrderController(req, res);
	await controller.exec(controller.getOrdersByStatus.bind(controller));
};

module.exports.getOrdersByUser = async (req, res) => {
	let controller = new OrderController(req, res);
	await controller.exec(controller.getOrdersByUser.bind(controller));
};

module.exports.createOrder = async (req, res) => {
	let controller = new OrderController(req, res);
	await controller.exec(controller.createOrder.bind(controller));
};

module.exports.updateOrder = async (req, res) => {
	let controller = new OrderController(req, res);
	await controller.exec(controller.updateOrder.bind(controller));
};
