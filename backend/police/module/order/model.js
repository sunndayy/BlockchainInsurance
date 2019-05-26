const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const orderSchema = new Schema({
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
		email: {type: String, require: true}
	},
	product: {
		name: {type: String, require: true},
		producer: {type: String, require: true},
		type: {type: Number, require: true}
	},
	policeInfo: {
		uid: {type: String, require: true},
		licensePlate: {type: String}
	}
});

module.exports = mongoose.model('order', orderSchema);