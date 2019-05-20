const mongoose = require('mongoose');
const Schema = mongoose.Schema;
const autoIncrement = require('mongoose-auto-increment');
autoIncrement.initialize(mongoose.connection);

const txSchema = new Schema({
	id: {type: Number, unique: true},
	type: {type: String, enum: ['PLAN', 'CONTRACT'], require: true},
	ref: {type: Schema.Types.Mixed, require: true},
	preStateHash: {type: String},
	action: {type: Schema.Types.Mixed},
	time: {type: Date, default: new Date()},
	status: {type: Boolean, default: false}
});

txSchema.plugin(autoIncrement.plugin, {
	model: 'tx',
	field: 'id',
	startAt: 100000,
	incrementBy: 1
});

module.exports = mongoose.model('tx', txSchema);