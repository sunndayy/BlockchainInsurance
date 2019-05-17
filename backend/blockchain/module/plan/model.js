const mongoose = require('mongoose');

const Plan = mongoose.model('plan');

module.exports.FindByCompany = async company => {
	return await Plan.find({company: company}).populate('contracts');
};

module.exports.FindByCompanyAndId = async (company, id) => {
	return await Plan.findOne({company: company, id: id}).populate('contracts');
};