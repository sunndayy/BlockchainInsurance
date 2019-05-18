const express = require('express');
const router = express.Router();
const userMiddleware = require('../../../middleware/user-middleware');
const orderBusiness = require('../business/order-business');
const request = require('request');

router.get('/orders', userMiddleware.authMiddleware, async (req, res) => {
	if (req.user.role === 0) {
		try {
			let orders = await orderBusiness.GetAllOrders();
			orders.forEach(order => {
				order.items.forEach(item => {
					delete item.product._doc.image;
				});
			});
			res.json(orders);
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

router.get('/orders-by-user/:username', userMiddleware.authMiddleware, async (req, res) => {
	if (req.user.role === 0 || req.user.username === req.params.username) {
		try {
			let orders = await orderBusiness.GetOrdersByUser(req.params.username);
			orders.forEach(order => {
				order.items.forEach(item => {
					delete item.product._doc.image;
				});
			});
			res.json(orders);
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

router.get('/orders-by-status/:status', userMiddleware.authMiddleware, async (req, res) => {
	if (req.user.role === 0) {
		try {
			let orders = await orderBusiness.GetOrdersByStatus(req.params.status);
			orders.forEach(order => {
				order.items.forEach(item => {
					delete item.product._doc.image;
				});
			});
			res.json(orders);
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

router.post('/create-order', userMiddleware.authMiddleware, async (req, res) => {
	try {
		let order = await orderBusiness.CreateOrder(req.user, req.body);
		order = order._doc;
		order.items = order.items.map(item => {
			return {
				id: item._doc.product.id,
				price: item._doc.price
			}
		});
		res.json(order);
	} catch (e) {
		res.json({
			error: e.message
		});
	}
});

router.put('/update-order/:id', userMiddleware.authMiddleware, async (req, res) => {
	if (req.user.role === 0) {
		try {
			let order = await orderBusiness.UpdateOrder(req.params.id, req.body);
			order = order._doc;
			order.items = order.items.map(item => {
				return {
					id: item._doc.product.id,
					price: item._doc.price
				}
			});
			res.json(order);
			
			if (order.contract) {
				request.post({url: 'http://bcinsurence.herokuapp.com', form: order.contract}, (e, res, body) => {
					if (e) {
						console.error(e);
					} else {
						try {
							body = JSON.parse(body);
							console.log(body);
						} catch (e) {
							console.error(e);
						}
					}
				});
			}
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