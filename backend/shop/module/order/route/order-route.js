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
			
			if (req.body.company && req.body.contractId) {
				let now = new Date().getTime();
				let body = {
					type: 'CONTRACT',
					ref: {
						plan: {
							company: req.body.company,
							id: req.body.contractId
						},
						userInfo: {
							identityCard: req.user.identityCard,
							licensePlate: req.body.licensePlate,
							name: req.user.name,
							birthday: new Date(parseInt(req.user.birthday.year),
								parseInt(req.user.birthday.month - 1),
								parseInt(req.user.birthday.day), 0, 0, 0).getTime(),
							sex: req.user.sex,
							address: req.user.address,
							phoneNumber: req.user.phoneNumber,
							email: req.user.email
						},
						garaPubKeyHashes: ['368a5b069220e0919d2481f07161c5625ee4167e0a886a9c5c01be81d7b7db12'],
						expireTime: {
							timeStart: now,
							timeEnd: now + 365 * 24 * 36 * req.body.duration
						}
					},
					action: {
						create: true
					}
				};
				request.post({url: 'http://bcinsurence.herokuapp.com/tx/contract', form: body}, (e, res, body) => {
					if (e) {
						console.error(e);
					} else {
						try {
							body = JSON.parse(body.toString());
							console.log(body);
						} catch (e) {
							console.error(e);
						}
					}
				});
			}
		} catch (e) {
			console.error()
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