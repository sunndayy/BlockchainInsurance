const jsonwebtoken = require('jsonwebtoken');
const User = require('../module/user/model/user-model');

module.exports.authMiddleware = async (req, res, next) => {
	try {
		let accessToken = req.headers['accesstoken'] || req.params['accesstoken'];
		let authData = await jsonwebtoken.verify(accessToken, 'secretkey');
		req.user = await User.findOne({'username': authData.username}, '-avatar -passwordHash').populate({
			path: 'favoriteProducts',
			select: '-image'
		});
		next();
	} catch (e) {
		res.json(e);
	}
};