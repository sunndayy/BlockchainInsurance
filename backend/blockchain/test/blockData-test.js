const BlockData = require('../blockchain-structure/blockData');

let blockData = new BlockData([{a: 1}, 1, ['1', 'abc', 3]]);
console.log(blockData.merkleRoot);