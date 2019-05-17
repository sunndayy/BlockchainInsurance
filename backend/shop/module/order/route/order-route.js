const express = require('express');
const router = express.Router();
const userMiddleware = require('../../../middleware/user-middleware');
const orderBusiness = require('../business/order-business');
const request = require('request');

router.get('/orders', userMiddleware.authMiddleware, async (req, res) => {
	if (req.user.role === 0) {
		try {
			let orders = await orderBusiness.GetAllOrders();
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
		delete order.user;
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
			let data = await orderBusiness.UpdateOrder(req.params.id, req.body);
			res.json(data);
			if (data.contract) {
				request.post({url: 'http://bcinsurence.herokuapp.com', form: data.contract}, (e, res, body) => {
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