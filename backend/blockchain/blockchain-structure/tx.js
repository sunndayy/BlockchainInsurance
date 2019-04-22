const mongoose = require('mongoose');
const { dbPrefix } = require('../config');
const Plan = mongoose.model(dbPrefix + '_plan');
const Contract = mongoose.model(dbPrefix + '_contract');

class Tx {
  constructor(obj) {
    this.targetHash = obj.targetHash;
    this.preStateHash = obj.preStateHash;
    this.action = obj.action; // create ? update
  }
}

class PlanTx extends Tx {
  constructor(obj) {
    super(obj);
  }

  Validate() {

  }
}

class ContractTx extends Tx {
  constructor(obj) {
    super(obj);
  }

  Validate() {

  }
}

module.exports = { PlanTx, ContractTx };