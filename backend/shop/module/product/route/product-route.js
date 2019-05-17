const express = require('express');
const router = express.Router();
const multer = require('multer');
const upload = multer({dest: 'uploads/'});
const userMiddleware = require('../../../middleware/user-middleware');
const productBusiness = require('../business/product-business');
const fs = require('fs');

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

router.get('/product-image/:id', async (req, res) => {
	let buffer = await productBusiness.GetImage(req.params.id);
	if (buffer) {
		res.set({'Content-Type': 'image/gif'});
		res.end(buffer);
	} else {
		res.json({
			error: 'Image not found'
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

router.post('/create-product', userMiddleware.authMiddleware, upload.single('image'), async (req, res) => {
	if (req.user.role === 0) {
		let cb = async () => {
			try {
				let product = await productBusiness.CreateProduct(req.body);
				res.json(product);
			} catch (e) {
				res.json({
					error: e.message
				});
			}
		};
		
		if (req.file) {
			fs.readFile(req.file.path, async (e, data) => {
				if (e) {
					res.json({
						error: e.message
					});
				} else {
					req.body.image = data;
					fs.unlink(req.file.path, e => {
					});
					await cb();
				}
			});
		} else {
			await cb();
		}
	} else {
		res.json({
			error: 'Not authorization'
		});
	}
});

router.put('/update-product/:id', userMiddleware.authMiddleware, upload.single('image'), async (req, res) => {
	if (req.user.role === 0) {
		let cb = async () => {
			try {
				let product = await productBusiness.UpdateProduct(req.params.id, req.body);
				res.json(product);
			} catch (e) {
				res.json({
					error: e.message
				});
			}
		};
		
		if (req.file) {
			fs.readFile(req.file.path, async (e, data) => {
				if (e) {
					res.json({
						error: e.message
					});
				} else {
					req.body.image = data;
					fs.unlink(req.file.path, e => {
					});
					await cb();
				}
			});
		} else {
			await cb();
		}
	} else {
		res.json({
			error: 'Not authorization'
		});
	}
});

module.exports = router;