const mongoose = require('mongoose');
const Schema = mongoose.Schema;
const Plan = require('../plan/model');

/*
CREATE: reference to plan's last version
UPDATE(add new refund): reference to plan's last version, contract's last version
* */

const ContractSchema = new Schema({
  plan                              : { type: Schema.Types.ObjectId, ref: 'plan' , require: true},
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
    refund                            : { type: Number, require: true }
  }]
});

const Contract = mongoose.model( 'contract', ContractSchema );

module.exports.FindByLicensePlate = async licensePlate => {
  return await Contract.find({ licensePlate: licensePlate });
};

module.exports.FindByCompany = async company => {
  return await Plan.find({ company: company }).populate('contracts').select('contracts');
};