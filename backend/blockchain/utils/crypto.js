const sha256 = require('sha256');
const EC = require('elliptic').ec;
const ec = new EC('secp256k1');

module.exports.Hash = src => {
  return sha256(src);
};

module.exports.Sign = (privKey, msg) => {
  let key = ec.keyFromPrivate(privKey, 'hex');
  let msgHash = sha256(msg, { asBytes: true });
  return { msg: msg, pubKey: key.getPublic('hex'), sign: key.sign(msgHash) };
};

module.exports.Verify = ({ pubKey, msg, sign }) => {
  let key = ec.keyFromPublic(pubKey, 'hex');
  let msgHash = sha256(msg, { asBytes: true });
  return key.verify(msgHash, sign);
};

module.exports.GenKey = () => {
  return ec.genKeyPair().getPrivate('hex');
};