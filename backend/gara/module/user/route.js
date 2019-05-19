const express = require('express');
const router = express.Router();
const userController = require('./controller');

router.route('/sign-in')
	.post(userController.signIn);

module.exports = router;