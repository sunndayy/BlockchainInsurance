const crypto = require('../utils/crypto');

module.exports = class BlockData {
  constructor(txs) {
    this._txs = txs || [];
  }

  get merkleRoot() {
    switch (this._txs.length) {
      case 0:
        return '';

      case 1:
        return crypto.Hash(JSON.stringify(this._txs[0]));

      default:
        let tmpArr1 = [];
        this._txs.forEach(tx => {
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