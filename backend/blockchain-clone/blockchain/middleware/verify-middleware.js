const Crypto = require('../utils/crypto');

module.exports = (req, res, next) => {
    let sign = JSON.parse(req.body);
    if (Crypto.Verify(sign)) {
    	req.body = sign.msg;
        req.body.pubKeyHash = Crypto.Hash(sign.pubKey);
        next();
    } else {
        res.end('Invalid signature');
    }
};