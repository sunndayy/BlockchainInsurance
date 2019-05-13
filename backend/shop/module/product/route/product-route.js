const express = require('express');
const router = express.Router();
const userMiddleware = require('../../../middleware/user-middleware');
const productBusiness = require('../business/product-business');

router.get('/products', async (req, res) => {
	try {
		let products = await productBusiness.GetAllProducts();
		res.json(products);
	} catch (e) {
		res.json({
			error: e.message
		});
	}
});

router.get('/products-by-types/:type', async (req, res) => {
	try {
		let products = await productBusiness.GetProductsByType(parseInt(req.params.type));
		res.json(products);
	} catch (e) {
		res.json({
			error: e.message
		});
	}
});

router.get('/products-by-producer/:producer', async (req, res) => {
	try {
		let products = await productBusiness.GetProductsByProducer(req.params.producer);
		res.json(products);
	} catch (e) {
		res.json({
			error: e.message
		});
	}
});

router.post('/create-product', userMiddleware.authMiddleware, async (req, res) => {
	if (req.user.role === 0) {
		try {
			let product = await productBusiness.CreateProduct(req.body);
			res.json(product);
		} catch (e) {
			res.json({
				error: e.message
			});
		}
	} else {
		res.json({
			error: 'Not authorization'
		});
	}
});

router.put('/update-product/:id', userMiddleware.authMiddleware, async (req, res) => {
	if (req.user.role === 0) {
		try {
			let product = await productBusiness.UpdateProduct(req.params.id, req.body);
			res.json(product);
		} catch (e) {
			res.json({
				error: e.message
			});
		}
	} else {
		res.json({
			error: 'Not authorization'
		});
	}
});

module.exports = router;