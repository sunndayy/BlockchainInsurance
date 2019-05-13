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
			try {
				await state.txDict[this.uid].save();
			} catch (e) {
			}
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
			return false;
		}
		
		let node = state.nodes.find(node => {
			return node.pubKeyHash === this.pubKeyHash;
		});
		
		if (!node || node.company !== this.ref.company) {
			return false;
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
		
		return false;
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
			return false;
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
				// Contract reference
				let target = {
					userInfo: this.ref.userInfo,
					garaPubKeyHashes: this.ref.garaPubKeyHashes,
					expireTime: this.ref.expireTime
				};
				
				state.txDict[this.uid] = plan.contracts.find(contract => {
					let source = {
						userInfo: contract.userInfo,
						garaPubKeyHashes: contract.garaPubKeyHashes,
						expireTime: contract.expireTime
					};
					return JSON.stringify(source) === JSON.stringify(target);
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
					return false;
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
					return false;
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
		
		return false;
	}
	
	async UpdateState(state) {
		let newContract = this.action.create ? this.action.create : this.action.update;
		
		if (this.action.create) {
			state.txDict[this.ref.plan.company + this.ref.plan.id].contracts.push(state.txDict[this.uid]);
			newContract.plan = state.txDict[this.ref.plan.company + this.ref.plan.id];
			state.txDict[this.uid] = new Contract(newContract);
		} else if (this.action.update) {
			if (!state.txDict[this.uid]) {
				let contracts = await Contract.find({
					userInfo: this.ref.userInfo,
					garaPubKeyHashes: this.ref.garaPubKeyHashes,
					expireTime: this.ref.expireTime
				}).populate('plan');
				for (let i = 0; i < contracts.length; i++) {
					if (contracts[i].plan.company === this.ref.plan.company
						&& contracts[i].plan.id === this.ref.plan.id) {
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
		if (state.txDict[this.ref.company + this.ref.id]) {
			try {
				await state.txDict[this.ref.company + this.ref.id].save();
				delete state.txDict[this.ref.company + this.ref.id];
			} catch (e) {
			}
		}
	}
}

module.exports = ({sign, tx}) => {
	if (tx.type === 'PLAN') {
		return new PlanTx({sign, tx})
	} else {
		return new ContractTx(({sign, tx}));
	}
};