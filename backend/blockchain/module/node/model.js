const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const NodeSchema = new Schema({
  pubKeyHash                        : { type: String, require: true },
  company                           : { type: String },
  host                               : { type: String },
  point                             : { type: Number },
  lastTimeCreateBlock               : { type: Number },
  lastTimeUpdateHost                 : { type: Date }
});

const Node = mongoose.model('node', NodeSchema);

module.exports.GetHost = async pubKeyHash => {
  let node = await Node.findOne({ pubKeyHash: pubKeyHash });
  if (node) {
    return node.host;
  }
  return null;
};

module.exports.FindNode = async pubKeyHash => {
  return await Node.findOne({ pubKeyHash: pubKeyHash });;
};