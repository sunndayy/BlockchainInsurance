const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const BlockSchema = new Schema({
  blockHeader                     : {
    index                           : { type: Number, require: true },
    preBlockHash                    : { type: String },
    merkleRoot                      : { type: String, require: true },
    validatorSigns                  : [ Schema.Types.Mixed ],
    creatorSign                      : Schema.Types.Mixed
  },
  blockData                         : [ Schema.Types.Mixed ],
  hash                              : { type: String, require: true }
});

let Block = mongoose.model('block', BlockSchema);

module.exports.FindByIndex = async index => {
  return await Block.findOne({ "blockHeader.index": index });
};

module.exports.FindByHash = async hash => {
  return await Block.findOne({ hash: hash });
};