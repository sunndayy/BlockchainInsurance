const mongoose = require('mongoose');
const Crypto = require('../../utils/crypto');

let Block = mongoose.model('block');

module.exports.FindByIndex = async index => {
	return await Block.findOne({"blockHeader.index": index});
};

module.exports.FindByHash = async hash => {
	return await Block.findOne({hash: hash});
};