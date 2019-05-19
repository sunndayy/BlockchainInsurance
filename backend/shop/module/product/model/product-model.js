const mongoose = require('mongoose');
const Schema = mongoose.Schema;

let productSchema = new Schema({
	id: {type: String, require: true, unique: true},
	name: {type: String, require: true},
	describe: {type: String},
	type: {type: Number, min: 0, max: 2},
	price: {type: Number, require: true},
	producer: {type: String, require: true},
	image: {type: Buffer}
});

module.exports = mongoose.model('product', productSchema);