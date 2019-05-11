const Crypto = require('../utils/Crypto');

module.exports = class BlockHeader {
	constructor(obj) {
		if (obj) {
			this.index = obj.index;
			this.preBlockHash = obj.preBlockHash || null;
			this.merkleRoot = obj.merkleRoot || null;
			this.validatorSigns = obj.validatorSigns || [];
			this.creatorSign = obj.creatorSign || null;
		}
	}
	
	get timeStamp() {
		if (this.validatorSigns.length === 0) {
			return 0;
		}
		let max = 0;
		this.validatorSigns.forEach(sign => {
			let timeSign = JSON.parse(sign.msg).timeSign;
			max = (timeSign > max) ? timeSign : max;
		});
		return max;
	}
	
	get firstTimeSign() {
		if (this.validatorSigns.length === 0) {
			return 0;
		}
		let min;
		this.validatorSigns.forEach(sign => {
			let timeSign = JSON.parse(sign.msg).timeSign;
			if (!min) {
				min = timeSign;
			} else {
				min = (timeSign < min) ? timeSign : min;
			}
		});
		return min;
	}
	
	Sign() {
		let valPubKeyHashes = this.validatorSigns.map(sign => {
			return Crypto.Hash(sign.pubKey);
		});
		this.creatorSign = Crypto.Sign(valPubKeyHashes);
	}
	
	get infoNeedAgree() {
		return {
			index: this.index,
			preBlockHash: this.preBlockHash,
			merkleRoot: this.merkleRoot
		}
	}
	
	get hash() {
		return Crypto.Hash(JSON.stringify(this));
	}
};