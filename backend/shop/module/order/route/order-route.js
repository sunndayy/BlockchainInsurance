const express = require('express');
const router = express.Router();
const userMiddleware = require('../../../middleware/user-middleware');
const orderController = require('../controller/order-controller');

router.get('/orders', userMiddleware.authMiddleware, orderController.getAllOrders);

router.get('/orders-by-user/:username', userMiddleware.authMiddleware, orderController.getOrdersByUser);

router.get('/orders-by-status/:status', userMiddleware.authMiddleware, orderController.getOrdersByStatus);

router.post('/create-order', userMiddleware.authMiddleware, orderController.createOrder);

router.put('/update-order/:id', userMiddleware.authMiddleware, orderController.updateOrder);

module.exports = router;