const express = require('express');
const router = express.Router();
const authMiddleware = require('../../middleware/authMiddleware');
const txController = require('./controller');

router.route('/txs')
	.get(authMiddleware, txController.getAllTx);

// router.route('/tx/plan')
// 	.post(txController.createTx);

router.route('/tx/plan')
	.post(authMiddleware, txController.createPlanTx);

router.route('/tx/contract')
	.post(txController.createContractTx);

router.route('/tx/:id')
	.put(authMiddleware, txController.updateTx);

module.exports = router;