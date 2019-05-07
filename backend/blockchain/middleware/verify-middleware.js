const Crypto = require('../utils/crypto');

module.exports = (req, res, next) => {
    let sign = req.body;
    if (Crypto.Verify(sign)) {
    	req.body = JSON.parse(sign.msg);
        req.body.pubKeyHash = Crypto.Hash(sign.pubKey);
        next();
    } else {
        res.end('Invalid signature');
    }
};