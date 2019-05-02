const mongoose = require('mongoose');
const Schema = mongoose.Schema;

/*
CREATE: no reference
UPDATE: reference to plan's last version
* */

const PlanSchema = new Schema({
    company                         : { type: String, require: true },
    id                              : { type: String, require: true },
    term                            : {
        pricePerYear                    : { type: Number, require: true },
        percentage                      : { type: Number, require: true },
        maxRefund                       : { type: Number, require: true },
        state                           : { type: Boolean, default: true },
    },
    contracts                       : [{ type: Schema.Types.ObjectId, ref: 'contract' }]
});

const Plan = mongoose.model( 'plan', PlanSchema );

module.exports.FindByCompany = async company => {
    return await Plan.find({ company: company });
};

module.exports.FindByCompanyAndId = async (company, id) => {
    return await Plan.findOne({ company: company, id: id });
};

module.exports = Plan;