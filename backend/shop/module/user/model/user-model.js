const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const userSchema = new Schema({
	username: {type: String, require: true, unique: true},
	passwordHash: {type: String, require: true},
	role: {type: Number, min: 0, max: 1},
	name: {type: String, require: true},
	identityCard: {type: String, require: true},
	birthDay: {type: Date, require: true},
	sex: {type: Boolean, require: true},
	address: {type: String, require: true},
	phoneNumber: {type: String},
	email: {type: String, require: true, unique: true},
});

module.exports = mongoose.model('user', userSchema);