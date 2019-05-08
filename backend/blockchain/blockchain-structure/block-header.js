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

    get timeStamp() {
        if (this._validatorSigns.length === 0) {
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
        if (this._validatorSigns.length === 0) {
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

    Sign() {
	    this._creatorSign = Crypto.Sign(Crypto.Hash(JSON.stringify(this._validatorSigns)));
    }

    get infoNeedAgree() {
        return {
            index: this._index,
            preBlockHash: this._preBlockHash,
            merkleRoot: this._merkleRoot
        }
    }
    
    get validatorSigns() {
    	return this._validatorSigns;
    }
    
    get json() {
    	return {
    		index: this._index,
		    preBlockHash: this._preBlockHash,
		    merkleRoot: this._merkleRoot,
		    validatorSigns: this._validatorSigns,
		    creatorSign: this._creatorSign
	    }
    }
};