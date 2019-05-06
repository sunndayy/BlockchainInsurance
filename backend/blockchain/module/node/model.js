const mongoose = require('mongoose');
const Node = mongoose.model('node');

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