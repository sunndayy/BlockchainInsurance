const Crypto = require('../utils/Crypto');

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
        if (!Crypto.Verify(this._creatorSign)) {
            return false;
        }
        for (let i = 0; i < this._validatorSigns.length; i++) {
            if (!Crypto.Verify(this._validatorSigns[i])) {
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
        return Crypto.Sign(privKey, Crypto.Hash(JSON.stringify(privKey)));
    }

    get hash() {
        return Crypto.Hash(JSON.stringify(this));
    }

    get infoNeedAgree() {
        return {
            index: this._index,
            preBlockHash: this._preBlockHash,
            merkleRoot: this._merkleRoot
        }
    }
};