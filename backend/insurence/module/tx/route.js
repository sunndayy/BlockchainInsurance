const express = require('express');
const router = express.Router();
const authMiddleware = require('../../middleware/authMiddleware');
const txController = require('./controller');

router.route('/txs')
	.get(authMiddleware, txController.getAllTx);

router.route('/tx')
	.post(txController.createTx);

router.route('/tx/:id')
	.put(authMiddleware, txController.updateTx);

module.exports = router;