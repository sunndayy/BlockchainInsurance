const mongoose = require('mongoose');
const Schema = mongoose.Schema;
const { dbPrefix } = require('../config');

/*
CREATE: no reference(ensure no plans have the same company and id)
UPDATE: reference to last version
* */

const PlanSchema = new Schema({
  company                         : { type: String, require: true },
  id                              : { type: String, require: true },
  term                            : {
    pricePerYear                    : { type: Number, require: true },
    percentage                      : { type: Number, require: true },
    maxRefund                       : { type: Number, require: true }
  },
  status                          : { type: Boolean, default: true },
  contracts                       : [{ type: Schema.Types.ObjectId, ref: dbPrefix + '_contract' }],
  lastUpdate                      : { type: Date, require: true }
});

const Plan = mongoose.model( dbPrefix + '_plan', PlanSchema );

module.exports.getPlansByCompany = async company => {
  return await Plan.find({ company: company }).lean();
};

module.exports.getPlansByCompanyAndId = async (company, id) => {
  return await Plan.findOne({ company: company, id: id }).lean();
};

module.exports.getContractsByCompany = async (company) => {
  return await Plan.find({ company: company }).populate('contracts').lean();
};

module.exports = PlanSchema;