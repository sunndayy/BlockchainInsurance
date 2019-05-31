const mongoose = require('mongoose');
const Schema = mongoose.Schema;
const autoIncrement = require('mongoose-auto-increment');
autoIncrement.initialize(mongoose.connection);

let commentSchema = new Schema({
	id: {type: Number, unique: true},
	user: {type: Schema.Types.ObjectId, ref: 'user', require: true},
	content: {type: String},
	time: {type: Date, default: new Date()}
});

commentSchema.plugin(autoIncrement.plugin, {
	model: 'comment',
	field: 'id',
	startAt: 100000,
	incrementBy: 1
});

module.exports = mongoose.model('comment', commentSchema);