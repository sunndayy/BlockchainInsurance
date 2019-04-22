module.exports = class BlockHeader {
  constructor(obj) {
    if (obj) {
      this._index = obj.index;
      this._merkleRoot = obj.merkleRoot;
      this._preBlockHash = obj.preBlockHash;
      this._validatorSigns = obj.validatorSigns || [];
      this._creatorSign = obj.creatorSign;
    }
  }

  Verify() {

  }

  get timeStamp() {

  }

  Sign(privKey) {

  }

  get hash() {

  }
};