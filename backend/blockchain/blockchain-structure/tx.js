const mongoose = require('mongoose');
const Plan = mongoose.model('plan');
const Contract = mongoose.model('contract');
const Crypto = require('../utils/crypto');

class Tx {
  constructor(obj) {
    this._type = obj._type;
    this._ref = obj._ref;
    this._preStateHash = obj.preStateHash;
    this._action = obj.action; // create ? update
    this._pubKeyHash = obj._pubKeyHash;
  }
}

class PlanTx extends Tx {
  async Validate(state) {
    let node = state.nodes.find(node => {
      return node.pubKeyHash == this._pubKeyHash;
    });

    if (!node || node.company != this._ref.company) {
      return false;
    }

    let uid = this._ref.company + this._ref.id;

    if (!state.txDict[uid]) {
      state.txDict[uid] = await Plan.findOne({ company: this._ref.company, id: this._ref.id });
    }

    if (this._action.create && !state.txDict[uid]) {
      return true;
    }

    if (this._action.update && state.txDict[uid]) {
      let preStateHash = JSON.stringify(state.txDict[uid]);
      let update = this._action.update;
      if (preStateHash == this._preStateHash) {
        return true;
      }
    }

    return false;
  }

  UpdateStateAfterPushing(state) {
    let newPlan = this._action.create ? this._action.create : this._action.update;
    let uid = this._ref.company + this._ref.id;

    if (this._action.create) {
      state.txDict[uid] = new Plan(newPlan);
    } else if (this._action.update) {
      let prePlan = state.txDict[uid];
      let termKeys = Object.keys(newPlan.term);
      termKeys.forEach(key => {
        prePlan.term[key] = newPlan.term[key];
      });
      newPlan.lastUpdate = new Date()
    }
  }
}

class ContractTx extends Tx {
  async Validate(state) {
    // ref: plan.company.id + plan.company.id + JSON.stringify(refunds)
    let uid = JSON.stringify(this._ref.plan)
        + JSON.stringify(this._ref.userInfo)
        + JSON.stringify(this._ref.garaPubKeyHashes)
        + JSON.stringify(this._ref.expireTime);

    let plan;
    if (!state.txDict[uid]) {
      if (!state.txDict[this._ref.plan.company + this._ref.plan.id]) {
        state.txDict[this._ref.plan.company + this._ref.plan.id] = await Plan.findOne({ company: this._ref.plan.company, id: this._ref.plan.id }).populate('contracts');
      }
      plan = state.txDict[this._ref.plan.company + this._ref.plan.id];

      if (plan) {
        let target = {
          userInfo: this._ref.userInfo,
          garaPubKeyHashes: this._ref.garaPubKeyHashes,
          expireTime: this._ref.expireTime
        };
        state.txDict[uid] = plan.contracts.find(contract => {
          let source = {
            userInfo: contract.userInfo,
            garaPubKeyHashes: contract.garaPubKeyHashes,
            expireTime: contract.expireTime
          }
          return JSON.stringify(source) == JSON.stringify(target);
        });

        if (this._action.create && !state.txDict[uid]) {
          let node = state.nodes.find(node => {
            return node.pubKeyHash == this._pubKeyHash;
          });

          if (!node || node.company != this._ref.plan.company) {
            return false;
          }

          if (this._preStateHash == JSON.stringify(plan) && plan.term.state) {
            return true;
          }
        }

        if (this._action.update && state.txDict[uid]) {
          if (this._ref.garaPubKeyHashes.indexOf(this._pubKeyHash) < 0) {
            return false;
          }

          if (this._preStateHash == Crypto.Hash( JSON.stringify(plan) + JSON.stringify(state.txDict[uid]))) {
            let sum = 0;
            state.txDict[uid].refunds.forEach(refund => {
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

  UpdateStateAfterPushing(state) {
    let newContract = this._action.create ? this._action.create : this._action.update;
    let uid = JSON.stringify(this._ref.plan)
        + JSON.stringify(this._ref.userInfo)
        + JSON.stringify(this._ref.garaPubKeyHashes)
        + JSON.stringify(this._ref.expireTime);

    if (this._action.create) {
      state.txDict[uid] = new Contract(newContract);
    } else if (this._action.update) {
      state.txDict[uid].refunds.push(this._action.update.push);
    }
  }
}

module.exports = { PlanTx, ContractTx };