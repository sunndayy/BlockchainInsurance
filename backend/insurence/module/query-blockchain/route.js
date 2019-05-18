const express = require('express');
const router = express.Router();
const authMiddleware = require('../../middleware/authMiddleware');
const queryController = require('./controller');

router.route('/plans')
	.get(authMiddleware, queryController.queryPlan);

router.route('/contracts')
	.get(authMiddleware, queryController.queryContract);

module.exports = router;