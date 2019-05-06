const sha256 = require('sha256');
const EC = require('elliptic').ec;
const ec = new EC('secp256k1');

module.exports.Hash = src => {
	return sha256(src);
};

let myKey = ec.keyFromPrivate(privKey, 'hex');
module.exports.PUB_KEY_HASH = sha256(myKey.getPublic('hex'));

module.exports.Sign = msg => {
	let msgHash = sha256(JSON.stringify(msg), { asBytes: true });
	return { msg: msg, pubKey: myKey.getPublic('hex'), sign: JSON.stringify(myKey.sign(msgHash)) };
};

module.exports.Verify = ({ msg, pubKey, sign }) => {
	let key = ec.keyFromPublic(pubKey, 'hex');
	let msgHash = sha256(JSON.stringify(msg), { asBytes: true });
	return key.verify(msgHash, JSON.parse(sign));
};