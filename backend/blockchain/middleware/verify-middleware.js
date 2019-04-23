const crypto = require('../utils/crypto');

module.exports = (req, res, next) => {
  try {
    let sign = JSON.parse(req.body.sign);
    if (crypto.Verify(sign)) {
      req.body = JSON.parse(sign.msg);
      req.body.pubKey = sign.pubKey;
      next();
    } else {
      res.end('invalid signature');
    }
  } catch (e) {
    res.end('undefined error');
  }
}