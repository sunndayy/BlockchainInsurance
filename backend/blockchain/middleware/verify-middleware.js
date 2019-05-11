const Crypto = require('../utils/crypto');

module.exports = (req, res, next) => {
	let sign = req.body;
	if (Crypto.Verify(sign)) {
		req.body = JSON.parse(sign.msg);
		req.body.pubKeyHash = Crypto.Hash(sign.pubKey);
		if (new Date() - new Date(req.body.time) < DURATION) {
			next();
		} else {
			res.end('Signature timeout');
		}
	} else {
		res.end('Invalid signature');
	}
};