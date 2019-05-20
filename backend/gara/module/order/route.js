const express = require('express');
const router = express.Router();
const orderController = require('./controller');
const userMiddleware = require('../../middleware/user-middleware');

router.route('/orders')
	.get(userMiddleware.authMiddleware, orderController.getOrders);

router.route('/create-order')
	.post(userMiddleware.authMiddleware, orderController.createOrder);

router.route('/update-order/:id')
	.put(userMiddleware.authMiddleware, orderController.updateOrder);

module.exports = router;