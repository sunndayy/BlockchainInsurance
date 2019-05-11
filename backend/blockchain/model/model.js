const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const NodeSchema = new Schema({
	pubKeyHash: {type: String, require: true, unique: true},
	company: {type: String, require: true, unique: true},
	host: {type: String},
	point: {type: Number, default: 0},
	lastTimeCreateBlock: {type: Number},
	lastTimeUpdateHost: {type: Date}
});

const BlockSchema = new Schema({
	blockHeader: {
		index: {type: Number, require: true, unique: true},
		preBlockHash: {type: String},
		merkleRoot: {type: String, require: true},
		validatorSigns: [Schema.Types.Mixed],
		creatorSign: Schema.Types.Mixed,
		hash: {type: String, require: true}
	},
	blockData: {
		txs: [Schema.Types.Mixed]
	}
});

BlockSchema.virtual('blockHeader.hash').get(() => {
	return this.hash;
});

const PlanSchema = new Schema({
	company: {type: String, require: true},
	id: {type: String, require: true},
	term: {
		pricePerYear: {type: Number, require: true},
		percentage: {type: Number, require: true},
		maxRefund: {type: Number, require: true},
		state: {type: Boolean, default: true},
	},
	contracts: [{type: Schema.Types.ObjectId, ref: 'contract'}]
});

const ContractSchema = new Schema({
	plan: {PlanSchema},
	userInfo: {
		identityCard: {type: String, require: true},
		licensePlate: {type: String, require: true},
		name: {type: String},
		birthDay: {type: Date},
		sex: {type: Boolean},
		address: {type: String},
		phoneNumber: {type: String},
		email: {type: String}
	},
	garaPubKeyHashes: [{type: String, require: true}],
	expireTime: {
		timeStart: {type: Date, require: true},
		timeEnd: {type: Date, require: true}
	},
	refunds: [{
		total: {type: Number, require: true},
		refund: {type: Number, require: true}
	}]
});

mongoose.model('node', NodeSchema);

mongoose.model('block_cache', BlockSchema);

mongoose.model('block', BlockSchema);

mongoose.model('plan', PlanSchema);

mongoose.model('contract', ContractSchema);