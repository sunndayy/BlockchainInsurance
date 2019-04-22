class Tx {
  constructor(obj) {
    this.type = obj.type; // plan ? contract
    this.targetHash = obj.targetHash;
    this.preStateHash = obj.preStateHash;
    this.action = obj.action; // create ? update
  }
}

class PlanTx extends Tx {
  
}

class ContractTx extends Tx {

}

module.exports = { PlanTx, ContractTx };