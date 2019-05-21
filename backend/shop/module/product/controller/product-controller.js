const productBusiness = require('../business/product-business');

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