const Product = require('../model/product-model');
const Order = require('../../order/model/order-model');
const fs = require('fs');

module.exports.getProductById = async id => {
	return await Product.findOne({id}, '-image').lean();
};

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

module.exports.getBestSellers = async () => {
	let orders = await Order.find({}, {items: {$slice: 1}}).populate({
		path: 'items.product',
		select: '-image'
	}).lean();
	let products = orders.map(order => order.items[0].product);
	for (let i = 0; i < products.length; i++) {
		if (!products[i].count) {
			products[i].count = 1;
		}
		for (let j = i + 1; j < products.length; j++) {
			if (products[i].id === products[j].id) {
				products[i].count++;
				products.splice(j, 1);
				j--;
			}
		}
	}
	products.sort((product1, product2) => {
		return product2.count - product1.count;
	});
	return products;
};

module.exports.getProductsByKeyword = async keyword => {
	return Product.find({name: new RegExp(keyword.replace(/\\/g, ''), 'gi')}, '-image').lean();
};