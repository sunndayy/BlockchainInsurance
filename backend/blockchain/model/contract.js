const mongoose = require('mongoose');
const Schema = mongoose.Schema;
const { dbPrefix } = require('../config');

/*
CREATE: reference to plan's last version
UPDATE(add new refund): reference to plan's last version, contract's last version
* */

const ContractSchema = new Schema({
  plan                              : { type: Schema.Types.ObjectId, ref: dbPrefix + '_plan' , require: true},
  userInfo                          : {
    identityCard                      : { type: String, require: true },
    licensePlate                      : { type: String, require: true },
    name                              : { type: String },
    birthDay                          : { type: Date },
    sex                               : { type: Boolean },
    address                           : { type: String },
    phoneNumber                       : { type: String },
    email                             : { type: String }
  },
  garaPubKeyHashes                  : [{ type: String, require: true }],
  expireTime                        : {
    timeStart                         : { type: Date, require: true },
    timeEnd                           : { type: Date, require: true }
  },
  refunds                           : [{
    total                             : { type: Number, require: true },
    refund                            : { type: Number, require: true },
    time                              : { type: Date, require: true }
  }],
  targetHash                          : { type: String, require: true } // plan + userInfo.identityCard + userInfo.licensePlate + expireTime => unique
});

const Contract = mongoose.model( dbPrefix + '_contract', ContractSchema );

module.exports.getContractsByLicensePlate = async licensePlate => {
  return await Contract.find({ licensePlate: licensePlate }).lean();
};

module.exports = Contract;