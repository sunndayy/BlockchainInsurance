const Order = require('./model');
const uuidv1 = require('uuid/v1');

module.exports.getOrder = async uid => {
	return await Order.findOne({'policeInfo.uid': uid}).lean();
};

module.exports.getOrders = async () => {
	return await Order.find({}).lean();
};

module.exports.createOrder = async data => {
	data.policeInfo = {
		uid: uuidv1()
	};
	return await Order.create(data);
};

module.exports.updateOrder = async (uid, data) => {
	return await Order.findOneAndUpdate({'policeInfo.uid': uid}, {$set: data}).lean();
};