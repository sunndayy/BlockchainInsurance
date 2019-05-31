const productBusiness = require('../business/product-business');
const mongoose = require('mongoose');
const User = mongoose.model('user');

class ProductController {
	constructor(req, res) {
		this.req = req;
		this.res = res;
	}
	
	async exec(business) {
		try {
			let result = await business();
			if (Buffer.isBuffer(result)) {
				this.res.set({'Content-Type': 'image/gif'});
				this.res.end(result);
			} else {
				this.res.json(result);
			}
		} catch (e) {
			this.res.json({
				error: e.message
			})
		}
	}
	
	static async getAllProducts() {
		return await productBusiness.getAllProducts();
	}
	
	async getProductImage() {
		return await productBusiness.getImage(this.req.params.id);
	}
	
	async getProductsByType() {
		return await productBusiness.getProductsByType(parseInt(this.req.params.type));
	}
	
	async getProductsByProducer() {
		return await productBusiness.getProductsByProducer(this.req.params.producer);
	}
	
	async createProduct() {
		if (this.req.user.role !== 0) {
			throw new Error('Not authorization');
		}
		return await productBusiness.createProduct(this.req.body, this.req.file);
	}
	
	async updateProduct() {
		if (this.req.user.role !== 0) {
			throw new Error('Not authorization');
		}
		return await productBusiness.updateProduct(this.req.params.id, this.req.body, this.req.file);
	}
	
	static async getBestSellers() {
		return await productBusiness.getBestSellers();
	}
	
	async getProductsByKeyword() {
		return await productBusiness.getProductsByKeyword(this.req.params.keyword);
	}
	
	async likeProduct() {
		let product = await productBusiness.getProductById(this.req.body.id);
		if (product) {
			this.req.user.favoriteProducts.push(product);
			await this.req.user.save();
		}
		return product;
	}
	
	async unlikeProduct() {
		let product = await productBusiness.getProductById(this.req.body.id);
		if (product) {
			this.req.user = await User.findOneAndUpdate(
				{
					username: this.req.user.username
				},
				{
					$pull: {
						favoriteProducts: product._id
					}
				},
				{
					new: true
				})
				.populate({
					path: 'favoriteProducts',
					select: '_id',
					model: 'product'
				});
		}
		return product;
	}
	
	async getFavoriteProducts() {
		return this.req.user.favoriteProducts;
	}
}

module.exports.getAllProducts = async (req, res) => {
	let controller = new ProductController(req, res);
	await controller.exec(ProductController.getAllProducts);
};

module.exports.getProductImage = async (req, res) => {
	let controller = new ProductController(req, res);
	await controller.exec(controller.getProductImage.bind(controller));
};

module.exports.getProductsByType = async (req, res) => {
	let controller = new ProductController(req, res);
	await controller.exec(controller.getProductsByType.bind(controller));
};

module.exports.getProductsByProducer = async (req, res) => {
	let controller = new ProductController(req, res);
	await controller.exec(controller.getProductsByProducer.bind(controller));
};

module.exports.createProduct = async (req, res) => {
	let controller = new ProductController(req, res);
	await controller.exec(controller.createProduct.bind(controller));
};

module.exports.updateProduct = async (req, res) => {
	let controller = new ProductController(req, res);
	await controller.exec(controller.updateProduct.bind(controller));
};

module.exports.getBestSellers = async (req, res) => {
	let controller = new ProductController(req, res);
	await controller.exec(ProductController.getBestSellers);
};

module.exports.getProductsByKeyword = async (req, res) => {
	let controller = new ProductController(req, res);
	await controller.exec(controller.getProductsByKeyword.bind(controller));
};

module.exports.likeProduct = async (req, res) => {
	let controller = new ProductController(req, res);
	await controller.exec(controller.likeProduct.bind(controller));
};

module.exports.unlikeProduct = async (req, res) => {
	let controller = new ProductController(req, res);
	await controller.exec(controller.unlikeProduct.bind(controller));
};

module.exports.getFavoriteProducts = async (req, res) => {
	let controller = new ProductController(req, res);
	await controller.exec(controller.getFavoriteProducts.bind(controller));
};