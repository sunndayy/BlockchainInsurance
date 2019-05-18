const express = require('express');
const router = express.Router();
const orderController = require('./controller');

router.route('/create-order')
	.post(orderController.createOrder);

router.route('/update-order/:id')
	.put(orderController.updateOrder);

module.exports = router;