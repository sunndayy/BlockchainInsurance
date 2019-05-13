const jsonwebtoken = require('jsonwebtoken');
const User = require('../module/user/model/user-model');

module.exports.authMiddleware = async (req, res, next) => {
	try {
		let authData = await jsonwebtoken.verify(req.headers['accesstoken'], 'secretkey');
		req.user = await User.findOne({'userName': authData.userName});
		next();
	} catch (e) {
		res.json(e);
	}
};