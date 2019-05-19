const Order = require('../model/order-model');
const User = require('../../user/model/user-model');
const Product = require('../../product/model/product-model');

module.exports.GetAllOrders = async () => {
	return await Order.find({}).populate('items.product', '-image');
};

module.exports.GetOrdersByStatus = async status => {
	return await Order.find({status}).populate('items.product', '-image');
};

module.exports.GetOrdersByUser = async username => {
	let user = await User.findOne({username: username});
	return await Order.find({user}).populate('items.product', '-image');
};

module.exports.CreateOrder = async (user, data) => {
	data.user = user;
	for (let i = 0; i < data.items.length; i++) {
		data.items[i].product = await Product.findOne({id: data.items[i].id});
		if (data.items[i].product.price !== data.items[i].price) {
			throw new Error('Invalid price');
		}
	}
	data.status = false;
	return await Order.create(data);
};

module.exports.UpdateOrder = async (id, data) => {
	if (data.id) {
		delete data.id;
	}
	return await Order.findOneAndUpdate({id}, {$set: data}, {new: true, fields: '-image'});
};