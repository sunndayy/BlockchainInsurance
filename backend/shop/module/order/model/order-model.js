const mongoose = require('mongoose');
const Schema = mongoose.Schema;
const autoIncrement = require('mongoose-auto-increment');
autoIncrement.initialize(mongoose.connection);

let orderSchema = new Schema({
	id: {type: Number, unique: true},
	user: {type: Schema.Types.ObjectId, ref: 'user', require: true},
	items: [{
		product: {type: Schema.Types.ObjectId, ref: 'product', require: true},
		price: {type: Number, require: true},
	}],
	policeInfo: {
		licensePlate: {type: String},
		uid: {type: String, require: true}
	},
	time: {type: Date, default: new Date()},
	status: {type: Boolean}
});

orderSchema.plugin(autoIncrement.plugin, {
	model: 'order',
	field: 'id',
	startAt: 100000,
	incrementBy: 1
});

module.exports = mongoose.model('order', orderSchema);