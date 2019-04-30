const Crypto = require('../utils/crypto');

module.exports = (req, res, next) => {
  let sign = JSON.parse(req.body);
  if (Crypto.Verify(sign)) {
    try {
      req.body = JSON.parse(sign.msg);
    } catch (err) {
      req.body.msg = sign.msg;
    }
    req.body.pubKey = sign.pubKey;
    next();
  } else {
    res.error('Invalid signature');
  }
}