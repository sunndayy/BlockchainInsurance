const Order = require('../model/order-model');
const User = require('../../user/model/user-model');
const Product = require('../../product/model/product-model');
const request = require('request');

module.exports.getAllOrders = async () => {
	return await Order.find({}).populate('items.product', '-image');
};

module.exports.getOrdersByStatus = async status => {
	return await Order.find({status}).populate('items.product', '-image');
};

module.exports.getOrdersByUser = async user => {
	if (typeof user === 'string') {
		user = await User.findOne({username: user});
	}
	if (!user) {
		throw new Error('Invalid username');
	}
	return await Order.find({user}).populate('items.product', '-image');
};

module.exports.createOrder = async (user, data) => {
	data.user = user;
	for (let i = 0; i < data.items.length; i++) {
		data.items[i].product = await Product.findOne({id: data.items[i].id});
		if (data.items[i].product.price !== data.items[i].price) {
			throw new Error('Invalid price');
		}
	}
	data.status = false;
	let order = await Order.create(data);
	order._doc.items.forEach(item => {
		delete item._doc.product._doc.image;
	});
	return order;
};

module.exports.updateOrder = async (id, data) => {
	if (data.id) {
		delete data.id;
	}
	
	let order = await Order.findOneAndUpdate({id}, {$set: data}, {
		new: true,
		fields: '-items.product.image'
	}).populate('user');
	let user = order.user;
	
	if (data.licensePlate
		&& data.company
		&& data.contractId
		&& data.status
		&& data.duration) {
		let now = new Date().getTime();
		let body = {
			type: 'CONTRACT',
			ref: {
				plan: {
					company: data.company,
					id: data.contractId
				},
				userInfo: {
					identityCard: user.identityCard,
					licensePlate: data.licensePlate,
					name: user.name,
					birthday: new Date(parseInt(user.birthday.year),
						parseInt(user.birthday.month - 1),
						parseInt(user.birthday.day), 0, 0, 0).getTime(),
					address: user.address,
					phoneNumber: user.phoneNumber,
					email: user.email
				},
				garaPubKeyHashes: ['368a5b069220e0919d2481f07161c5625ee4167e0a886a9c5c01be81d7b7db12'],
				expireTime: {
					timeStart: now,
					timeEnd: now + 365 * 24 * 36 * data.duration
				}
			},
			action: {
				create: true
			}
		};
		request.post({url: 'http://bcinsurence.herokuapp.com/tx/contract', form: body}, (e, res, body) => {
			if (e) {
				console.error(e);
			}
		});
	}
	
	return order;
};