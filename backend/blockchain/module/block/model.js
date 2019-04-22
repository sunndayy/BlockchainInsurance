const mongoose = require('mongoose');
const Schema = mongoose.Schema;
const { dbPrefix } = require('../../config');

const BlockSchema = new Schema({
  blockHeader                     : {
    index                           : { type: Number, require: true },
    preBlockHash                    : { type: String },
    merkleRoot                      : { type: String, require: true },
    validatorSigns                  : [ Schema.Types.Mixed ],
    createSign                      : Schema.Types.Mixed
  },
  blockData                         : [ Schema.Types.Mixed ],
  hash                              : { type: String, require: true }
});

module.exports = mongoose.model(dbPrefix + '_block', BlockSchema);