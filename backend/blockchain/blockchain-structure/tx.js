const mongoose = require('mongoose');
const Plan = mongoose.model('plan');
const Contract = mongoose.model('contract');
const Crypto = require('../utils/crypto');

class Tx {
	constructor({sign, tx}) {
		this.sign = sign;
		this.type = tx.type;
		this.ref = tx.ref;
		this.preStateHash = tx.preStateHash || null;
		this.action = tx.action;
	}
	
	async UpdateDB(state) {
		if (state.txDict[this.uid]) {
			await state.txDict[this.uid].save();
			delete state.txDict[this.uid];
		}
	}
	
	Verify() {
		return Crypto.Verify(this.sign);
	}
	
	get pubKeyHash() {
		return Crypto.Hash(this.sign.pubKey);
	}
}

class PlanTx extends Tx {
	constructor(obj) {
		super(obj);
	}
	
	async Validate(state) {
		if (!super.Verify()) {
			throw new Error('Invalid signature');
		}
		
		let node = state.nodes.find(node => {
			return node.pubKeyHash === this.pubKeyHash;
		});
		
		if (!node || node.company !== this.ref.company) {
			throw new Error('Invalid node');
		}
		
		if (!state.txDict[this.uid]) {
			state.txDict[this.uid] = await Plan.findOne({company: this.ref.company, id: this.ref.id}).populate('contracts');
		}
		
		/*
		CREATE PLAN
		* */
		if (this.action.create && !state.txDict[this.uid]) {
			return true;
		}
		
		/*
		UPDATE TERM
		* */
		if (this.action.update && state.txDict[this.uid]) {
			if (Crypto.Hash(JSON.stringify(state.txDict[this.uid].term)) === this.preStateHash) {
				return true;
			}
		}
		
		throw new Error('Undefined error');
	}
	
	async UpdateState(state) {
		let newPlan = this.action.create ? this.action.create : this.action.update;
		
		if (this.action.create) {
			state.txDict[this.uid] = new Plan({
				company: this.ref.company,
				id: this.ref.id,
				term: newPlan.term
			});
		} else if (this.action.update) {
			if (!state.txDict[this.uid]) {
				state.txDict[this.uid] = await Plan.findOne({
					company: this.ref.company,
					id: this.ref.id
				});
			}
			let prePlan = state.txDict[this.uid];
			let termKeys = Object.keys(newPlan.term);
			termKeys.forEach(key => {
				prePlan.term[key] = newPlan.term[key];
			});
		}
	}
	
	get uid() {
		return this.ref.company + this.ref.id;
	}
}

class ContractTx extends Tx {
	constructor(obj) {
		super(obj);
	}
	
	async Validate(state) {
		if (!super.Verify()) {
			throw new Error('Invalid signature');
		}
		
		// Plan reference
		let plan;
		if (!state.txDict[this.ref.plan.company + this.ref.plan.id]) {
			state.txDict[this.ref.plan.company + this.ref.plan.id] = await Plan.findOne({
				company: this.ref.plan.company,
				id: this.ref.plan.id
			}).populate('contracts');
		}
		plan = state.txDict[this.ref.plan.company + this.ref.plan.id];
		
		if (plan) {
			if (!state.txDict[this.uid]) {
				let compareDate = (date1, date2) => {
					let _date1 = new Date(date1);
					let _date2 = new Date(date2);
					return _date1.getDate() === _date2.getDate() && _date1.getMonth() === _date2.getMonth() && _date1.getFullYear() === _date2.getFullYear();
				};
				
				state.txDict[this.uid] = plan.contracts.find(contract => {
					return JSON.stringify(this.ref.userInfo) === JSON.stringify(contract.userInfo)
						&& JSON.stringify(this.ref.garaPubKeyHashes) === JSON.stringify(contract.garaPubKeyHashes)
						&& compareDate(this.ref.expireTime.timeStart, contract.expireTime.timeStart)
						&& compareDate(this.ref.expireTime.timeEnd, contract.expireTime.timeEnd);
				});
			}
			
			/*
			CREATE CONTRACT
			* */
			if (this.action.create && !state.txDict[this.uid]) {
				let node = state.nodes.find(node => {
					return node.pubKeyHash === this.pubKeyHash;
				});
				
				if (!node || node.company !== this.ref.plan.company) {
					throw new Error('Invalid company');
				}
				
				if (this.preStateHash === Crypto.Hash(JSON.stringify(plan.term))
					&& plan.term.state) {
					return true;
				}
			}
			
			/*
			CREATE REFUND
			* */
			if (this.action.update && state.txDict[this.uid]) {
				if (this.ref.garaPubKeyHashes.indexOf(this.pubKeyHash) < 0) {
					throw new Error('Invalid gara');
				}
				
				if (this.preStateHash === Crypto.Hash(JSON.stringify(plan.term) + JSON.stringify(state.txDict[this.uid].refunds))
				) {
					let sum = 0;
					state.txDict[this.uid].refunds.forEach(refund => {
						sum += refund.refund;
					});
					let newRefund = this.action.update.push;
					if (newRefund.garaPubKeyHash === this.pubKeyHash) {
						let time = new Date(newRefund.time);
						let timeStart = new Date(state.txDict[this.uid].expireTime.timeStart);
						let timeEnd = new Date(state.txDict[this.uid].expireTime.timeEnd);
						if (timeStart < time && time < timeEnd) {
							sum += newRefund.refund;
							if (sum <= plan.term.maxRefund
								&& newRefund.refund / newRefund.total <= plan.term.percentage) {
								return true;
							}
						}
					}
				}
			}
		}
		
		throw new Error('Undefined error');
	}
	
	async UpdateState(state) {
		let newContract = this.action.create ? this.action.create : this.action.update;
		
		if (this.action.create) {
			if (!state.txDict[this.ref.plan.company + this.ref.plan.id]) {
				state.txDict[this.ref.plan.company + this.ref.plan.id] = await Plan.findOne({
					company: this.ref.plan.company,
					id: this.ref.plan.id
				}).populate('contracts');
			}
			state.txDict[this.uid] = new Contract({
				plan: state.txDict[this.ref.plan.company + this.ref.plan.id],
				userInfo: this.ref.userInfo,
				garaPubKeyHashes: this.ref.garaPubKeyHashes,
				expireTime: this.ref.expireTime
			});
			state.txDict[this.ref.plan.company + this.ref.plan.id].contracts.push(state.txDict[this.uid]);
		} else if (this.action.update) {
			if (!state.txDict[this.uid]) {
				let contracts = await Contract.find({
					userInfo: this.ref.userInfo,
					garaPubKeyHashes: this.ref.garaPubKeyHashes,
					// expireTime: this.ref.expireTime
				}).populate('plan');
				let compareDate = (date1, date2) => {
					let _date1 = new Date(date1);
					let _date2 = new Date(date2);
					return _date1.getDate() === _date2.getDate() && _date1.getMonth() === _date2.getMonth() && _date1.getFullYear() === _date2.getFullYear();
				};
				for (let i = 0; i < contracts.length; i++) {
					if (contracts[i].plan.company === this.ref.plan.company
						&& contracts[i].plan.id === this.ref.plan.id
						&& compareDate(contracts[i].expireTime.timeStart, this.ref.expireTime.timeStart)
						&& compareDate(contracts[i].expireTime.timeEnd, this.ref.expireTime.timeEnd)) {
						state.txDict[this.uid] = contracts[i];
						break;
					}
				}
			}
			if (state.txDict[this.uid]) {
				state.txDict[this.uid].refunds.push(this.action.update.push);
			}
		}
	}
	
	get uid() {
		return JSON.stringify(this.ref.plan)
			+ JSON.stringify(this.ref.userInfo)
			+ JSON.stringify(this.ref.garaPubKeyHashes)
			+ JSON.stringify(this.ref.expireTime);
	}
	
	async UpdateDB(state) {
		super.UpdateDB(state);
		if (state.txDict[this.ref.plan.company + this.ref.plan.id]) {
			await state.txDict[this.ref.plan.company + this.ref.plan.id].save();
			delete state.txDict[this.ref.plan.company + this.ref.plan.id];
		}
	}
}

module.exports = ({sign, tx}) => {
	if (tx.type === 'PLAN') {
		return new PlanTx({sign, tx})
	} else if (tx.type === 'CONTRACT') {
		return new ContractTx(({sign, tx}));
	}
	return null;
};