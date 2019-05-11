const Crypto = require('../utils/crypto');

module.exports = (req, res, next) => {
	let sign = req.body;
	if (Crypto.Verify(sign)) {
		req.body = JSON.parse(sign.msg);
		req.body.pubKeyHash = Crypto.Hash(sign.pubKey);
		if (req.body.header) {
			if (new Date() - new Date(req.body.time) >= DURATION) {
				return res.end('Signature timeout');
			}
		}
		next();
	} else {
		res.end('Invalid signature');
	}
};