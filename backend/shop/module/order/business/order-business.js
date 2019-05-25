const Order = require('../model/order-model');
const User = require('../../user/model/user-model');
const Product = require('../../product/model/product-model');
const request = require('request');
const CronJob = require('cron').CronJob;

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
	
	let order = await Order.findOneAndUpdate({id}, {$set: data}, {new: true})
		.populate('user')
		.populate({path: 'items.product', select: '-image'});
	
	if (order.status) {
		let msgToPolice = {
			user: order.user._doc,
			product: order.items[0].product._doc
		};
		
		request.post({url: 'http://bcpolice.herokuapp.com/order', form: msgToPolice}, async (e, res, body) => {
			if (e) {
				console.error(e);
			} else {
				try {
					let msg = JSON.parse(body.toString());
					if (msg.policeInfo.uid) {
						await Order.findOneAndUpdate({id}, {$set: {policeInfo: {uid: msg.policeInfo.uid}}});
						let cronJob;
						cronJob = new CronJob('*/60 * * * * *', () => {
							request(`http://bcpolice.herokuapp.com/order/${msg.policeInfo.uid}`, async (e, res, body) => {
								if (e) {
									console.error(e);
								} else {
									try {
										let msg = JSON.parse(body.toString());
										if (msg.policeInfo.licensePlate) {
											await Order.findOneAndUpdate({id}, {$set: {'policeInfo.licensePlate': msg.policeInfo.licensePlate}});
											cronJob.stop();
										}
									} catch (e) {
										console.error(e);
									}
								}
							});
						});
						cronJob.start();
					}
				} catch (e) {
					console.error(e);
				}
			}
		});
	}
	
	return order;
};