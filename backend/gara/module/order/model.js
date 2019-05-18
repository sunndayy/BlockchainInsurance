const mongoose = require('mongoose');
const Schema = mongoose.Schema;
const autoIncrement = require('mongoose-auto-increment');
autoIncrement.initialize(mongoose.connection);

const orderSchema = new Schema({
	id: {type: Number, unique: true},
	user: {
		name: {type: String, require: true},
		birthday: {
			day: {type: Number, require: true},
			month: {type: Number, require: true},
			year: {type: Number, require: true}
		},
		identityCard: {type: String, require: true},
		sex: {type: Boolean, require: true},
		address: {type: String, require: true},
		phoneNumber: {type: String},
		email: {type: String, require: true, unique: true}
	},
	licensePlate: {type: String, require: true},
	image: {type: Buffer, require: true},
	total: {type: Number, require: true},
	status: {type: Boolean, default: false}
});

orderSchema.plugin(autoIncrement.plugin, {
	model: 'order',
	field: 'id',
	startAt: 100000,
	incrementBy: 1
});

module.exports = mongoose.model('order', orderSchema);