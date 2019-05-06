const mongoose = require('mongoose');

const Plan = mongoose.model( 'plan' );

module.exports.FindByCompany = async company => {
    return await Plan.find({ company: company });
};

module.exports.FindByCompanyAndId = async (company, id) => {
    return await Plan.findOne({ company: company, id: id });
};