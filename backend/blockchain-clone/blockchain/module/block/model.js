const mongoose = require('mongoose');
const Schema = mongoose.Schema;
const Crypto = require('../../utils/crypto');

const BlockSchema = new Schema({
    blockHeader                     : {
        index                           : { type: Number, require: true },
        preBlockHash                    : { type: String },
        merkleRoot                      : { type: String, require: true },
        validatorSigns                  : [ Schema.Types.Mixed ],
        creatorSign                      : Schema.Types.Mixed
    },
    blockData                         : {
    	txs: [ Schema.Types.Mixed ]
    },
    hash                              : { type: String, require: true }
});

BlockSchema.virtual('blockHeader.hash').get(() => {
    return this.hash;
});

let Block = mongoose.model('block', BlockSchema);
mongoose.model('block_cache', BlockSchema);

module.exports.FindByIndex = async index => {
    return await Block.findOne({ "blockHeader.index": index });
};

module.exports.FindByHash = async hash => {
    return await Block.findOne({ hash: hash });
};

module.exports = BlockSchema;