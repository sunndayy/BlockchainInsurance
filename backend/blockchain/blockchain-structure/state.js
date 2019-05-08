const Crypto = require('../utils/crypto');

const FindByIndex = require('../module/block/model').FindByIndex;
const BlockData = require('./block-data');
const BlockHeader = require('./block-header');

const mongoose = require('mongoose');
const Node = mongoose.model('node');
const Block = mongoose.model('block');
const BlockCache = mongoose.model('block_cache');

const request = require('request');
const _ = require('lodash');

module.exports = class State {
	constructor(isGlobalState = false) {
		// this.txDict = {};
		// this.isGlobalState = isGlobalState;
	}
	
	async Init() {
		// this.nodes = await Node.find({});
		// if (this.isGlobalState) {
		// 	let preBlock;
		// 	if (choosenBlock.blockHeader.index > 1) {
		// 		preBlock = blockCache1.find(block => {
		// 			return block.blockHeader.hash === choosenBlock.blockHeader.preBlockHash;
		// 		});
		// 	} else {
		// 		preBlock = blockCache1[0];
		// 	}
		// 	this.AddBlock(preBlock.blockHeader, preBlock.blockData);
		// 	this.AddBlock(choosenBlock.blockHeader, choosenBlock.blockData);
		// 	await BlockCache.remove({});
		// 	await BlockCache.insertMany([preBlock, choosenBlock]);
		// 	this.txCache = [];
		// }
	}
	
	HandleAfterNewBlock(blockHeader, blockData, cb) {
		// let preBlock = blockCache2.find(block => {
		// 	return block.blockHeader.hash === blockHeader.preBlockHash;
		// });
		//
		// let prePreBlock;
		// if (preBlock.blockHeader.index > 1) {
		// 	prePreBlock = blockCache1.find(block => {
		// 		return block.blockHeader.hash === preBlock.blockHeader.preBlockHash;
		// 	});
		// } else {
		// 	prePreBlock = blockCache1[0];
		// }
		// prePreBlock.hash = prePreBlock.blockHeader.hash;
		//
		//
		// (new Block(prePreBlock)).save(async (err, doc) => {
		// 	if (err) {
		// 		console.error(err);
		// 	} else {
		// 		mySession = WAIT_AFTER_NEW_BLOCK;
		// 		blockCache1 = blockCache2;
		// 		blockCache2 = [{
		// 			blockHeader: blockHeader,
		// 			blockData: blockData
		// 		}];
		//
		// 		setTimeout( async () => {
		// 			blockCache2.sort((a, b) => {
		// 				return a.blockHeader.firstTimeSign - b.blockHeader.firstTimeSign;
		// 			});
		// 			choosenBlock = blockCache2[0];
		// 			mySession = WAIT_TO_COLLECT_SIGN;
		// 			globalState = new State(true);
		// 			await globalState.Init();
		// 			txCache.forEach( async tx => {
		// 				await globalState.PushTx(tx, true);
		// 			});
		// 		}, DURATION);
		//
		// 		for (let i = 0; i < prePreBlock.blockData.txs.length; i++) {
		// 			let tx = prePreBlock.blockData.txs[i];
		// 			await tx.UpdateDB(this);
		// 		}
		//
		// 		if (prePreBlock.blockHeader.creatorSign) {
		// 			let creatorPubKeyHash = Crypto.Hash(prePreBlock.blockHeader.creatorSign.pubKey);
		// 			await Node.findOneAndUpdate({ pubKeyHash: creatorPubKeyHash }, { $inc: { point: 10 } });
		// 		}
		//
		// 		let validatorPubKeyHashes = prePreBlock.blockHeader.validatorSigns.map(sign => Crypto.Hash(sign.pubKey));
		// 		await Node.updateMany({pubKeyHash: { $in: validatorPubKeyHashes }}, { $inc: { point: 2 } });
		// 		cb();
		// 	}
		// });
	}
	
	async PushTx(tx, addToCache = false) {
		// if (await tx.Validate(this)) {
		// 	tx.UpdateState(this);
		//
		// 	if (addToCache) {
		// 		this.txCache.push(tx);
		//
		// 		if (this.txCache.length === NUM_TX_PER_BLOCK) {
		// 			let nodesOnTop = this.GetNodesOnTop();
		// 			let nodesOnTopPubKeyHashes = nodesOnTop.map(node => node.pubKeyHash);
		// 			if (nodesOnTopPubKeyHashes.indexOf(Crypto.PUB_KEY_HASH) < 0) {
		// 				if (this.CalTimeMustWait(Crypto.PUB_KEY_HASH) <= 0) {
		// 					let blockData = new BlockData(this.txCache);
		// 					let blockHeader = new BlockHeader({
		// 						index: choosenBlock.blockHeader.index + 1,
		// 						preBlockHash: Crypto.Hash(JSON.stringify(choosenBlock.blockHeader)),
		// 						merkleRoot: blockData.merkleRoot
		// 					});
		//
		// 					let _this = this;
		// 					nodesOnTop.forEach(node => {
		// 						if (node.pubKeyHash !== Crypto.PUB_KEY_HASH && node.host) {
		// 							request.post({
		// 								url: 'http://' + node.host + '/agree',
		// 								form: Crypto.Sign({ nextBlockHash: Crypto.Hash(JSON.stringify(blockHeader.infoNeedAgree)) })
		// 							}, (err, res, body) => {
		// 								if (err) {
		// 									console.error(err);
		// 								} else {
		// 									try {
		// 										if (mySession === WAIT_TO_COLLECT_SIGN) {
		// 											let sign = JSON.parse(body);
		// 											let msg = JSON.parse(sign.msg);
		//
		// 											if (msg.curBlockHash === choosenBlock.blockHeader.hash
		// 												&& msg.nextBlockHash === Crypto.Hash(JSON.stringify(blockHeader.infoNeedAgree))) {
		// 												if (!blockHeader.validatorSigns) {
		// 													blockHeader.validatorSigns = [];
		// 												}
		// 												blockHeader.validatorSigns.push(sign);
		//
		// 												if (blockHeader.validatorSigns.length === NUM_SIGN_PER_BLOCK) {
		// 													let validatorPubKeyHashes = blockHeader.validatorSigns.map(sign => {
		// 														return Crypto.Hash(sign.pubKey);
		// 													});
		// 													blockHeader.Sign();
		//
		// 													_this.HandleAfterNewBlock(blockHeader, blockData, () => {
		// 														_this.nodes.forEach(node => {
		// 															if (node.host !== HOST && node.host) {
		// 																request.post('http://' + node.host + '/header', { form: Crypto.Sign(blockHeader)}, (err, res, body) => {
		//
		// 																});
		// 															}
		// 														});
		// 													});
		// 												}
		// 											}
		// 										}
		// 									} catch (e) {
		// 										console.error(e);
		// 									}
		// 								}
		// 							});
		// 						}
		// 					});
		// 				}
		// 			}
		// 		}
		// 	}
		// }
	}
	
	AddBlock(blockHeader, blockData) {
		// let _this = this;
		// blockData.txs.forEach(tx => {
		// 	_this.PushTx(tx);
		// });
		//
		// if (blockHeader.creatorSign) {
		// 	let creator = this.nodes.find(node => {
		// 		return node.pubKeyHash === Crypto.Hash(blockHeader.creatorSign.pubKey);
		// 	});
		// 	creator.point += CREATOR_PRIZE;
		// }
		//
		// blockHeader.validatorSigns.forEach(sign => {
		// 	let validator = _this.nodes.find(node => {
		// 		return node.pubKeyHash === Crypto.Hash(sign.pubKey);
		// 	});
		// 	validator.point += VALIDATOR_PRIZE;
		// });
	}
	
	CalTimeMustWait(pubKeyHash, time = new Date()) {
		// let node = this.nodes.find(node => {
		// 	return node.pubKeyHash === pubKeyHash;
		// });
		//
		// if (node) {
		// 	if (!node.lastTimeCreateBlock) {
		// 		return 0;
		// 	}
		//
		// 	let s = node.point * (time - new Date(node.lastTimeCreateBlock));
		// 	if (s > 0) {
		// 		return (NEED_POINT - s) / node.point;
		// 	}
		// }
		//
		// return 3600000;
	}
	
	GetNodesOnTop() {
		// this.nodes.sort((a, b) => {
		// 	return b.point - a.point;
		// });
		// return this.nodes.slice(0, TOP);
	}
	
	/*
	* Only for new block from other nodes
	* */
	async ValidateBlockHeader(blockHeader) {
		// /*
		// Find previous block
		// * */
		// let preBlock;
		//
		// switch (blockHeader.index) {
		// 	case blockCache1[0].blockHeader.index:
		// 		preBlock = await FindByIndex(blockHeader.index - 1);
		// 		break;
		//
		// 	case blockCache2[0].blockHeader.index:
		// 		preBlock = blockCache1.find(block => {
		// 			return block.blockHeader.hash === blockHeader.preBlockHash;
		// 		});
		// 		if (preBlock) {
		// 			this.AddBlock(preBlock.blockHeader, preBlock.blockData);
		// 		} else {
		// 			return false;
		// 		}
		// 		break;
		//
		// 	case blockCache2[0].blockHeader.index + 1:
		// 		preBlock = blockCache2.find(block => {
		// 			return block.blockHeader.hash === blockHeader.preBlockHash;
		// 		});
		// 		if (preBlock) {
		// 			let prePreBlock;
		// 			if (preBlock.blockHeader.index > 1) {
		// 				prePreBlock = blockCache1.find(block => {
		// 					return block.blockHeader.hash === preBlock.blockHeader.preBlockHash;
		// 				});
		// 			} else {
		// 				prePreBlock = blockCache1[0];
		// 			}
		// 			this.AddBlock(prePreBlock.blockHeader, prePreBlock.blockData);
		// 			this.AddBlock(preBlock.blockHeader, preBlock.blockData);
		// 		} else {
		// 			return false;
		// 		}
		// 		break;
		//
		// 	default:
		// 		return false;
		// }
		//
		// /*
		// Validate preBlockHash and timeSign
		// * */
		// if (blockHeader.preBlockHash !== preBlock.blockHeader.hash) {
		// 	return false;
		// }
		//
		// if (blockHeader.firstTimeSign - preBlock.blockHeader.timestamp < DURATION) {
		// 	return false;
		// }
		//
		// /*
		// Verify and validate validatorSigns
		// * */
		// let nodesOntTop = this.GetNodesOnTop();
		// for (let i = 0; i < NUM_SIGN_PER_BLOCK; i++) {
		// 	if (!Crypto.Verify(blockHeader.validatorSigns[i])) {
		// 		return false;
		// 	}
		//
		// 	let pubKeyHash = Crypto.Hash(blockHeader.validatorSigns[i].pubKey);
		// 	if (!nodesOntTop.find(node => {
		// 		return node.pubKeyHash === pubKeyHash
		// 	})) {
		// 		return false;
		// 	}
		//
		// 	let msg = JSON.parse(blockHeader.validatorSigns[i].msg);
		//
		// 	if (msg.curBlockHash !== preBlock.blockHeader.hash
		// 		|| msg.nextBlockHash !== Crypto.Hash(JSON.stringify(blockHeader.infoNeedAgree))) {
		// 		return false;
		// 	}
		// }
		//
		// /*
		// Check no node signs 2 sequence blocks
		// * */
		// let validatorPubKeyHashes = blockHeader.validatorSigns.map(sign => {
		// 	return Crypto.Hash(sign.pubKey);
		// });
		//
		// let preBlockValidatorPubkeyHashes = preBlock.blockHeader.validatorSigns.map(sign => {
		// 	return Crypto.Hash(sign.pubKey);
		// });
		//
		// if (_.union(validatorPubKeyHashes, preBlockValidatorPubkeyHashes).length
		// 	< validatorPubKeyHashes.length + preBlockValidatorPubkeyHashes.length) {
		// 	return false;
		// }
		//
		// /*
		// Verify and validate creatorSign
		// * */
		// if (!Crypto.Verify(blockHeader.creatorSign)) {
		// 	return false;
		// }
		//
		// if (blockHeader.creatorSign.msg !== Crypto.Hash(JSON.stringify(validatorPubKeyHashes))) {
		// 	return false;
		// }
		//
		// /*
		// Check creator is not on top and has waited enough
		// * */
		// let creatorPubKeyHash = Crypto.Hash(blockHeader.creatorSign.pubKey);
		//
		// if (nodesOntTop.indexOf(creatorPubKeyHash) >= 0) {
		// 	return false;
		// }
		//
		// if (this.CalTimeMustWait(creatorPubKeyHash, new Date(blockHeader.timeStamp)) > 0) {
		// 	return false;
		// }
		//
		// return true;
	}
	
	async ValidateBlockData(blockHeader, blockData) {
		// let cb;
		// let preBlock;
		// switch (blockHeader.index) {
		// 	case blockCache1[0].index:
		// 		cb = () => {
		// 			blockCache1.push({
		// 				blockHeader: blockHeader,
		// 				blockData: blockData
		// 			});
		// 		};
		// 		break;
		//
		// 	case blockCache2[0].index:
		// 		preBlock = blockCache1.find(block => {
		// 			return block.blockHeader.hash === blockHeader.preBlockHash;
		// 		});
		// 		if (preBlock) {
		// 			this.AddBlock(preBlock.blockHeader, preBlock.blockData);
		// 		} else {
		// 			return false;
		// 		}
		// 		cb = () => {
		// 			blockCache2.push({
		// 				blockHeader: blockHeader,
		// 				blockData: blockData
		// 			});
		// 		};
		// 		break;
		//
		// 	case blockCache2[0].index + 1:
		// 		preBlock = blockCache2.find(block => {
		// 			return block.blockHeader.hash === blockHeader.preBlockHash;
		// 		});
		// 		if (preBlock) {
		// 			let prePreBlock = blockCache1.find(block => {
		// 				return block.blockHeader.hash === preBlock.blockHeader.preBlockHash;
		// 			});
		// 			this.AddBlock(prePreBlock.blockHeader, prePreBlock.blockData);
		// 			this.AddBlock(preBlock.blockHeader, preBlock.blockData);
		// 		} else {
		// 			return false;
		// 		}
		// 		cb = () => {
		// 			this.HandleAfterNewBlock(blockHeader, blockData);
		// 		};
		// 		break;
		//
		// 	default:
		// 		return false;
		// }
		//
		// if (blockData.length !== NUM_TX_PER_BLOCK) {
		// 	return false;
		// }
		//
		// if (blockData.merkleRoot !== blockHeader.merkleRoot) {
		// 	return false;
		// }
		//
		// for (let i = 0; i < NUM_TX_PER_BLOCK; i++) {
		// 	if (await !blockData[i].Validate(this)) {
		// 		return false;
		// 	}
		// }
		//
		// cb();
		// return true;
	}
};