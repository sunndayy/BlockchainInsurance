const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const userSchema = new Schema({
	username: {type: String, require: true, unique: true},
	passwordHash: {type: String, require: true},
	role: {type: Number, min: 0, max: 1},
	name: {type: String, require: true},
	identityCard: {type: String, require: true},
	birthday: {
		day: {type: Number, require: true},
		month: {type: Number, require: true},
		year: {type: Number, require: true}
	},
	sex: {type: Boolean, require: true},
	address: {type: String, require: true},
	phoneNumber: {type: String},
	email: {type: String, require: true, unique: true},
	favoriteProducts: [{type: Schema.Types.ObjectId, ref: 'product'}],
	avatar: {type: Buffer}
});

module.exports = mongoose.model('user', userSchema);