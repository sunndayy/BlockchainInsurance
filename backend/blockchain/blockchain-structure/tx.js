const mongoose = require('mongoose');
const Plan = mongoose.model('plan');
const Contract = mongoose.model('contract');
const Crypto = require('../utils/crypto');

class Tx {
    constructor({ sign, tx }) {
    	this._sign = sign;
    	this._type = tx.type;
    	this._ref = tx.ref;
    	this._preStateHash = tx.preStateHash;
    	this._action = tx.action;
    }

    async UpdateDB(state) {
        if (state.txDict[this.uid]) {
            await state.txDict[this.uid].save();
            delete state.txDict[this.uid];
        }
    }
    
    Verify() {
    	if (Crypto.Verify(this._sign)) {
    		this._pubKeyHash = Crypto.Hash(this._sign.pubKey);
    		return true;
	    }
    	return false;
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
            return node.pubKeyHash === this._pubKeyHash;
        });

        if (!node || node.company !== this._ref.company) {
            return false;
        }

        if (!state.txDict[this.uid]) {
            state.txDict[this.uid] = await Plan.findOne({ company: this._ref.company, id: this._ref.id });
        }

        if (this._action.create && !state.txDict[this.uid]) {
            return true;
        }

        if (this._action.update && state.txDict[this.uid]) {
            let preStateHash = JSON.stringify(state.txDict[this.uid]);
            if (preStateHash === this._preStateHash) {
                return true;
            }
        }

        return false;
    }

    UpdateState(state) {
        let newPlan = this._action.create ? this._action.create : this._action.update;

        if (this._action.create) {
            state.txDict[this.uid] = new Plan({
	            company: this._ref.company,
	            id: this._ref.id,
	            term: newPlan
            });
        } else if (this._action.update) {
            let prePlan = state.txDict[this.uid];
            let termKeys = Object.keys(newPlan.term);
            termKeys.forEach(key => {
                prePlan.term[key] = newPlan.term[key];
            });
            newPlan.lastUpdate = new Date()
        }
    }

    get uid() {
        return this._ref.company + this._ref.id;
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
    	
        let plan;

        if (!state.txDict[this.uid]) {
            // Plan reference
            if (!state.txDict[this._ref.plan.company + this._ref.plan.id]) {
                state.txDict[this._ref.plan.company + this._ref.plan.id] = await Plan.findOne({ company: this._ref.plan.company, id: this._ref.plan.id }).populate('contracts');
            }
            plan = state.txDict[this._ref.plan.company + this._ref.plan.id];

            if (plan) {
                // Contract reference
                let target = {
                    userInfo: this._ref.userInfo,
                    garaPubKeyHashes: this._ref.garaPubKeyHashes,
                    expireTime: this._ref.expireTime
                };

                state.txDict[this.uid] = plan.contracts.find(contract => {
                    let source = {
                        userInfo: contract.userInfo,
                        garaPubKeyHashes: contract.garaPubKeyHashes,
                        expireTime: contract.expireTime
                    };
                    return JSON.stringify(source) === JSON.stringify(target);
                });

                if (this._action.create && !state.txDict[this.uid]) {
                    let node = state.nodes.find(node => {
                        return node.pubKeyHash === this._pubKeyHash;
                    });

                    if (!node || node.company !== this._ref.plan.company) {
                        return false;
                    }

                    if (this._preStateHash === JSON.stringify(plan) && plan.term.state) {
                        return true;
                    }
                }

                if (this._action.update && state.txDict[this.uid]) {
                    if (this._ref.garaPubKeyHashes.indexOf(this._pubKeyHash) < 0) {
                        return false;
                    }

                    if (this._preStateHash === Crypto.Hash( JSON.stringify(plan) + JSON.stringify(state.txDict[this.uid]))) {
                        let sum = 0;
                        state.txDict[this.uid].refunds.forEach(refund => {
                            sum += refund.refund;
                        });
                        let newRefund = this._action.update.push;
                        sum += newRefund.refund;
                        if (sum <= plan.term.maxRefund && newRefund.refund/newRefund.total <= plan.term.percentage) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    UpdateState(state) {
        let newContract = this._action.create ? this._action.create : this._action.update;
        if (this._action.create) {
            state.txDict[this.uid] = new Contract(newContract);
        } else if (this._action.update) {
            state.txDict[this.uid].refunds.push(this._action.update.push);
        }
    }

    get uid() {
        return JSON.stringify(this._ref.plan)
            + JSON.stringify(this._ref.userInfo)
            + JSON.stringify(this._ref.garaPubKeyHashes)
            + JSON.stringify(this._ref.expireTime);
    }
}

module.exports = ({ sign, tx }) => {
	if (tx.type == 'PLAN') {
		return new PlanTx({ sign, tx })
	} else {
		return new ContractTx(({ sign, tx }));
	}
};