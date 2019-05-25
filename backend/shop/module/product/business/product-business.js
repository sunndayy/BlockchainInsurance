const Product = require('../model/product-model');
const fs = require('fs');

module.exports.getAllProducts = async () => {
	return await Product.find({}, '-image').lean();
};

module.exports.getProductsByType = async type => {
	return await Product.find({type}, '-image').lean();
};

module.exports.getProductsByProducer = async producer => {
	return await Product.find({producer}, '-image').lean();
};

module.exports.getImage = async id => {
	let product = await Product.findOne({id}, 'image').lean();
	return product.image.buffer;
};

module.exports.createProduct = async (data, imageFile) => {
	if (imageFile) {
		data.image = fs.readFileSync(imageFile.path);
	}
	let product = await Product.create(data);
	delete product._doc.image;
	return product;
};

module.exports.updateProduct = async (id, data, imageFile) => {
	if (imageFile) {
		data.image = fs.readFileSync(imageFile.path);
	}
	return await Product.findOneAndUpdate({id}, {$set: data}, {new: true, fields: '-image'}).lean();
};