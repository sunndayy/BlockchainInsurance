const crypto = require('../utils/crypto');

module.exports = class BlockHeader {
  constructor(obj) {
    if (obj) {
      this._index = obj.index;
      this._preBlockHash = obj.preBlockHash;
      this._merkleRoot = obj.merkleRoot;
      this._validatorSigns = obj.validatorSigns || [];
      this._creatorSign = obj.creatorSign;
    }
  }

  Verify() {
    if (!crypto.Verify(this._creatorSign)) {
      return false;
    }
    for (let i = 0; i < this._validatorSigns.length; i++) {
      if (!crypto.Verify(this._validatorSigns[i])) {
        return false;
      }
    }
    return true;
  }

  get timeStamp() {
    if (this._validatorSigns.length == 0) {
      return 0;
    }
    let max = 0;
    this._validatorSigns.forEach(sign => {
      let timeSign = JSON.parse(sign.msg).timeSign;
      max = (timeSign > max) ? timeSign : max;
    });
    return max;
  }

  get firstTimeSign() {
    if (this._validatorSigns.length == 0) {
      return 0;
    }
    let min;
    this._validatorSigns.forEach(sign => {
      let timeSign = JSON.parse(sign.msg).timeSign;
      if (!min) {
        min = timeSign;
      } else {
        min = (timeSign < min) ? timeSign : min;
      }
    });
    return min;
  }

  Sign(privKey) {
    let info = {
      index: this._index,
      merkleRoot: this._merkleRoot,
      preBlockHash: this._preBlockHash,
      validatorSigns: this._validatorSigns
    };
    return crypto.Sign(privKey, crypto.Hash(JSON.stringify(privKey)));
  }

  get hash() {
    return crypto.Hash(JSON.stringify(this));
  }
};