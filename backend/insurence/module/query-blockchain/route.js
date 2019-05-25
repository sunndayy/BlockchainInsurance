const express = require('express');
const router = express.Router();
const authMiddleware = require('../../middleware/authMiddleware');
const queryController = require('./controller');

router.route('/plans')
	.get(queryController.queryPlan);

router.route('/contracts')
	.get(authMiddleware, queryController.queryContract);

router.route('/contracts-by-license-plate/:licensePlate')
	.get(queryController.queryContractByLicensePlate);

module.exports = router;