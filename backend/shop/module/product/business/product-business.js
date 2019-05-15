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
	data.type = parseInt(data.type);
	data.price = parseInt(data.price);
	data.amount = parseInt(data.amount);
	return await Product.create(data);
};

module.exports.GetImage = async id => {
	let product = await Product.findOne({id}, {image: 1});
	return product.image;
};

module.exports.UpdateProduct = async (id, data) => {
	return await Product.findOneAndUpdate({id}, {$set: data}, {new: true});
};