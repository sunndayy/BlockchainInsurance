const mongoose = require('mongoose');

const Plan = mongoose.model('plan');
const Contract = mongoose.model('contract');

module.exports.FindByLicensePlate = async licensePlate => {
	return await Contract.find({licensePlate: licensePlate}).populate('plan');
};

module.exports.FindByCompany = async company => {
	return await Plan.find({company: company}).populate('contracts');
};