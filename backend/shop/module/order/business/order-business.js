const Order = require('../model/order-model');
const User = require('../../user/model/user-model');
const Product = require('../../product/model/product-model');
const request = require('request');
const CronJob = require('cron').CronJob;
const nodemailer = require('nodemailer');
const mailConfig = require('../../../config/mail-config');
const swig = require('swig');
const moment = require('moment');

module.exports.getAllOrders = async () => {
	return await Order.find({})
		.populate({path: 'items.product', select: '-image'})
		.populate({path: 'user', select: '-passwordHash'})
		.lean();
};

module.exports.getOrdersByStatus = async status => {
	return await Order.find({status})
		.populate({path: 'items.product', select: '-image'})
		.populate({path: 'user', select: '-passwordHash'})
		.lean();
};

module.exports.getOrdersByUser = async user => {
	if (typeof user === 'string') {
		user = await User.findOne({username: user});
	}
	if (!user) {
		throw new Error('Invalid username');
	}
	return await Order.find({user})
		.populate({path: 'items.product', select: '-image'})
		.populate({path: 'user', select: '-passwordHash'})
		.lean();
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
		.populate({path: 'items.product', select: '-image'})
		.populate({path: 'user', select: '-passwordHash'})
		.lean();
	
	if (data.status) {
		let template = swig.compileFile('./template/order-confirmed.html');
		
		let emailInfo = {
			name: order.user.name,
			id: order.id,
			product_id: order.items[0].product.id,
			product_name: order.items[0].product.name,
			producer: order.items[0].product.producer,
			time: moment(new Date(order.time)).format('DD-MM-YYYY'),
			total: order.items[0].price
		};
		switch (order.items[0].product.type) {
			case 0:
				emailInfo.type = 'Xe số';
				break;
			case 1:
				emailInfo.type = 'Xe tay ga';
				break;
			case 2:
				emailInfo.type = 'Xe tay côn';
				break;
		}
		
		let emailBody = template(emailInfo);
		let transporter = nodemailer.createTransport(mailConfig);
		let mailOptions = {
			to: order.user.email,
			subject: 'Đơn hàng ' + order.id + ' đã được xác nhận',
			html: emailBody
		};
		
		transporter.sendMail(mailOptions, (e, info) => {
			if (e) {
				console.error(e);
			} else {
				console.log(info.response);
			}
		});
		
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