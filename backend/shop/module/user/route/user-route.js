const express = require('express');
const router = express.Router();
const userMiddleware = require('../../../middleware/user-middleware');
const userBusiness = require('../business/user-business');

router.post('/sign-up', async (req, res) => {
	try {
		let token = await userBusiness.signUp(req.body);
		res.json({
			token
		});
	} catch (e) {
		res.json({
			error: e.message
		});
	}
});

router.post('/sign-in', async (req, res) => {
	try {
		let token = await userBusiness.signIn(req.body.userName, req.body.password);
		res.json({
			token
		});
	} catch (e) {
		res.json({
			error: e.message
		});
	}
});

module.exports = router;