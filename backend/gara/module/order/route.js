const express = require('express');
const router = express.Router();
const orderController = require('./controller');
const userMiddleware = require('../../middleware/user-middleware');
const multer = require('multer');
const upload = multer({dest: 'uploads/'});

router.route('/orders')
	.get(userMiddleware.authMiddleware, orderController.getOrders);

router.route('/create-order')
	.post(userMiddleware.authMiddleware, upload.single('image'), orderController.createOrder);

router.route('/order-image/:id')
	.get(userMiddleware.authMiddleware, orderController.getImage);

router.route('/update-order/:id')
	.put(userMiddleware.authMiddleware, orderController.updateOrder);

module.exports = router;