const sha256 = require('sha256');
const EC = require('elliptic').ec;
const ec = new EC('secp256k1');

module.exports.Hash = src => {
    return sha256(src);
};

let key = ec.keyFromPrivate(privKey, 'hex');
module.exports.PUB_KEY_HASH = sha256(key.getPublic('hex'));

module.exports.Sign = msg => {
    let msgHash = sha256(JSON.stringify(msg), { asBytes: true });
    return { msg: msg, pubKey: key.getPublic('hex'), sign: key.sign(msgHash) };
};

module.exports.Verify = ({ pubKey, msg, sign }) => {
    let key = ec.keyFromPublic(pubKey, 'hex');
    let msgHash = sha256(JSON.stringify(msg), { asBytes: true });
    return key.verify(msgHash, sign);
};