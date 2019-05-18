const config = require('../config');
const sha256 = require('sha256');
const EC = require('elliptic').ec;
const ec = new EC('secp256k1');

module.exports.sign = msg => {
	let msgHash = sha256(JSON.stringify(msg), {asBytes: true});
	let key = ec.keyFromPrivate(config.myPrivKey, 'hex');
	return {
		msg: JSON.stringify(msg),
		pubKey: key.getPublic('hex'),
		sign: JSON.stringify(key.crypto(msgHash))
	};
};

module.exports.getPubKeyHash = privKey => {
	let key = ec.keyFromPrivate(config.myPrivKey, 'hex');
	return sha256(key.getPublic('hex'));
};

module.exports.hash = msg => {
	return sha256(msg);
};