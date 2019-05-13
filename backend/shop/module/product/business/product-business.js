const Product = require('../model/product-model');

module.exports.GetAllProducts = async () => {
	return Product.find({});
};

module.exports.GetProductsByType = async type => {
	return Product.find({type});
};

module.exports.GetProductsByProducer = async producer => {
	return Product.find({producer});
};

module.exports.CreateProduct = async data => {
	return await Product.create(data);
};

module.exports.UpdateProduct = async (id, data) => {
	return await Product.findOneAndUpdate({id}, {$set: data}, {new: true});
};