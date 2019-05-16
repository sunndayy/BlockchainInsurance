const Product = require('../model/product-model');

module.exports.GetAllProducts = async () => {
	return await Product.find({}, '-image');
};

module.exports.GetProductsByType = async type => {
	return await Product.find({type}, '-image');
};

module.exports.GetProductsByProducer = async producer => {
	return await Product.find({producer}, '-image');
};

module.exports.CreateProduct = async data => {
	// data.type = parseInt(data.type);
	// data.price = parseInt(data.price);
	// data.amount = parseInt(data.amount);
	return await Product.create(data);
};

module.exports.GetImage = async id => {
	let product = await Product.findOne({id}, 'image');
	return product.image;
};

module.exports.UpdateProduct = async (id, data) => {
	return await Product.findOneAndUpdate({id}, {$set: data}, {new: true, fields: '-image'});
};