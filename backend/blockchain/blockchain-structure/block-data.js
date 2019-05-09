const crypto = require('../utils/crypto');
const TX = require('./tx');

module.exports = class BlockData {
	constructor(txs) {
		this.txs = Array.isArray(txs) ? txs.map(tx => TX({ sign: tx.sign, tx: tx })) : [];
	}
	
	get merkleRoot() {
		switch (this.txs.length) {
			case 0:
				return '';
			
			case 1:
				return crypto.Hash(JSON.stringify(this.txs[0]));
			
			default:
				let tmpArr1 = [];
				this.txs.forEach(tx => {
					tmpArr1.push(crypto.Hash(JSON.stringify(tx)));
				});
				
				do {
					let tmpArr2 = [];
					for (let i = 0; i < tmpArr1.length; i += 2) {
						let s1 = tmpArr1[i];
						let s2 = (i + 1 < tmpArr1.length) ? tmpArr1[i + 1] : s1;
						tmpArr2.push(crypto.Hash(s1 + s2));
					}
					tmpArr1 = tmpArr2;
				} while (tmpArr1.length > 1);
				
				return tmpArr1[0];
		}
	}
};