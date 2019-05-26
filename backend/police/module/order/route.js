const express = require('express');
const router = express.Router();
const orderController = require('./controller');
const userMiddleware = require('../../middleware/user-middleware');

router.get('/order/:uid', orderController.getOrder);

router.get('/orders', userMiddleware.authMiddleware, orderController.getOrders);

router.post('/order', orderController.createOrder);

router.put('/order/:uid', userMiddleware.authMiddleware, orderController.updateOrder);

module.exports = router;