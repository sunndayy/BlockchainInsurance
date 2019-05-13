const mongoose = require('mongoose');
const Schema = mongoose.Schema;

let orderSchema = new Schema({
	id: {type: String, require: true, unique: true},
	user: {type: Schema.Types.ObjectId, ref: 'user', require: true},
	items: [{
		product: {type: Schema.Types.ObjectId, ref: 'product', require: true},
		prize: {type: Number, require: true},
		licensePlate: {type: String}
	}],
	time: {type: Date, default: new Date()},
	contractHash: {type: String},
	status: {type: Boolean, default: false}
});

module.exports = mongoose.model('order', orderSchema);