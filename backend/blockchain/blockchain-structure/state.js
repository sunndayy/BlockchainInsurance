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
	constructor(isGlobalState) {
		this.txDict = {};
		this.isGlobalState = isGlobalState;
		this.txCache = [];
	}

	async PushTx(tx, addToCache = true) {
		if (addToCache && this.txCache.length < NUM_TX_PER_BLOCK) {
			this.txCache.push(tx);
			await tx.UpdateState(this);

			if (this.txCache.length === NUM_TX_PER_BLOCK) {
				let nodesOnTop = this.GetNodesOnTop();
				let nodesOnTopPubKeyHashes = nodesOnTop.map(node => node.pubKeyHash);

				if (nodesOnTopPubKeyHashes.indexOf(Crypto.PUB_KEY_HASH) < 0) {
					// if (this.CalTimeMustWait(Crypto.PUB_KEY_HASH) <= 0) {
					let blockData = new BlockData(this.txCache);
					let blockHeader = new BlockHeader({
						index: choosenBlock.blockHeader.index + 1,
						preBlockHash: Crypto.Hash(JSON.stringify(choosenBlock.blockHeader)),
						merkleRoot: blockData.merkleRoot
					});

					let preBlockValPubKeyHashes = choosenBlock.blockHeader.valSigns.map(sign => {
						return Crypto.Hash(sign.pubKey);
					});
					
					let _this = this;
					nodesOnTop.forEach(node => {
						if (node.pubKeyHash !== Crypto.PUB_KEY_HASH && node.host) {
							request.post({
								url: 'http://' + node.host + '/agree',
								form: Crypto.Sign({nextBlockHash: Crypto.Hash(JSON.stringify(blockHeader.infoNeedAgree))})
							}, async (err, res, body) => {
								if (err) {
									console.log(err);
								} else {
									try {
										if (mySession === WAIT_TO_COLLECT_SIGN
											&& blockHeader.valSigns.length < NUM_SIGN_PER_BLOCK) {
											let sign = JSON.parse(body);
											let msg = JSON.parse(sign.msg);
											let valPubKeyHash = Crypto.Hash(sign.pubKey);

											if (msg.curBlockHash === choosenBlock.blockHeader.hash
												&& msg.nextBlockHash === Crypto.Hash(JSON.stringify(blockHeader.infoNeedAgree))
												&& preBlockValPubKeyHashes.indexOf(valPubKeyHash) < 0) {
												if (!blockHeader.valSigns) {
													blockHeader.valSigns = [];
												}
												blockHeader.valSigns.push(sign);

												if (blockHeader.valSigns.length === NUM_SIGN_PER_BLOCK) {
													let valPubKeyHashes = blockHeader.valSigns.map(sign => {
														return Crypto.Hash(sign.pubKey);
													});
													blockHeader.Sign();

													await _this.HandleAfterNewBlock(blockHeader, blockData, () => {
														_this.nodes.forEach(node => {
															if (node.host !== HOST && node.host) {
																request.post('http://' + node.host + '/header', {form: Crypto.Sign(blockHeader)}, (err, res, body) => {
																	if (err) {
																		console.log(err);
																	}
																});
															}
														});
													});
												}
											}
										}
									} catch (e) {
										console.log(e);
									}
								}
							});
						}
					});
					// }
				}
			}
			return true;
		} else {
			await tx.UpdateState(this);
		}
		return false;
	}

	async AddBlock(blockHeader, blockData) {
		for (let i = 0; i < blockData.txs.length; i++) {
			await this.PushTx(blockData.txs[i], false);
		}

		if (blockHeader.creatorSign) {
			let creator = this.nodes.find(node => {
				return node.pubKeyHash === Crypto.Hash(blockHeader.creatorSign.pubKey);
			});
			creator.point += CREATOR_PRIZE;
		}

		let _this = this;
		blockHeader.valSigns.forEach(sign => {
			let val = _this.nodes.find(node => {
				return node.pubKeyHash === Crypto.Hash(sign.pubKey);
			});
			val.point += VAL_PRIZE;
		});
	}

	async Init() {
		// Load nodes
		this.nodes = await Node.find({}).sort({company: 1});

		if (this.isGlobalState) {
			let preBlock;
			if (choosenBlock.blockHeader.index > 1) {
				preBlock = blockCache1.find(block => {
					return block.blockHeader.hash === choosenBlock.blockHeader.preBlockHash;
				});
			} else {
				preBlock = blockCache1[0];
			}

			await this.AddBlock(preBlock.blockHeader, preBlock.blockData);
			await this.AddBlock(choosenBlock.blockHeader, choosenBlock.blockData);

			await BlockCache.remove({});
			await BlockCache.insertMany([
				Object.assign({
					hash: preBlock.blockHeader.hash
				}, preBlock),
				Object.assign({
					hash: choosenBlock.blockHeader.hash
				}, choosenBlock)
			]);

			let i = 0;
			while (i < txCache.length) {
				if (await txCache[i].Validate(this)) {
					if (!await this.PushTx(txCache[i], true)) {
						return;
					}
					txCache.splice(i, 1);
				} else {
					i++;
				}
			}
		}
	}

	async HandleAfterNewBlock(blockHeader, blockData, cb) {
		let preBlock = blockCache2.find(block => {
			return block.blockHeader.hash === blockHeader.preBlockHash;
		});

		let prePreBlock;
		if (preBlock.blockHeader.index > 1) {
			prePreBlock = blockCache1.find(block => {
				return block.blockHeader.hash === preBlock.blockHeader.preBlockHash;
			});
		} else {
			prePreBlock = blockCache1[0];
		}
		prePreBlock.hash = prePreBlock.blockHeader.hash;

		try {
			await (new Block(prePreBlock)).save();
			mySession = WAIT_AFTER_NEW_BLOCK;
			blockCache1 = blockCache2;
			blockCache2 = [{
				blockHeader: blockHeader,
				blockData: blockData
			}];

			for (let i = 0; i < prePreBlock.blockData.txs.length; i++) {
				let tx = prePreBlock.blockData.txs[i];
				await tx.UpdateDB(this);
			}

			if (prePreBlock.blockHeader.creatorSign) {
				let creatorPubKeyHash = Crypto.Hash(prePreBlock.blockHeader.creatorSign.pubKey);
				await Node.findOneAndUpdate({pubKeyHash: creatorPubKeyHash}, {$inc: {point: CREATOR_PRIZE}});
			}

			let valPubKeyHashes = prePreBlock.blockHeader.valSigns.map(sign => Crypto.Hash(sign.pubKey));
			await Node.updateMany({pubKeyHash: {$in: valPubKeyHashes}}, {$inc: {point: VAL_PRIZE}});

			setTimeout(async () => {
				mySession = WAIT_TO_COLLECT_SIGN;
				blockCache2.sort((a, b) => {
					return a.blockHeader.firstTimeSign - b.blockHeader.firstTimeSign;
				});
				choosenBlock = blockCache2[0];
				globalState = new State(true);
				await globalState.Init();
			}, DURATION);

			cb();
		} catch (e) {
			console.log(e);
		}
	}

	// CalTimeMustWait(pubKeyHash, time = new Date()) {
	// 	let node = this.nodes.find(node => {
	// 		return node.pubKeyHash === pubKeyHash;
	// 	});
	//
	// 	if (node) {
	// 		if (!node.lastTimeCreateBlock) {
	// 			return 0;
	// 		}
	//
	// 		let s = node.point * (time - new Date(node.lastTimeCreateBlock));
	// 		if (s > 0) {
	// 			return (NEED_POINT - s) / node.point;
	// 		}
	// 	}
	//
	// 	return 3600000;
	// }

	GetNodesOnTop() {
		this.nodes.sort((a, b) => {
			return b.point - a.point;
		});
		return this.nodes.slice(0, TOP);
	}

	/*
	* Only for new block from other nodes
	* */
	async ValidateBlockHeader(blockHeader) {
		/*
		Find previous block
		* */
		let preBlock;

		switch (blockHeader.index) {
			case blockCache1[0].blockHeader.index:
				preBlock = await FindByIndex(blockHeader.index - 1);
				break;

			case blockCache2[0].blockHeader.index:
				preBlock = blockCache1.find(block => {
					return block.blockHeader.hash === blockHeader.preBlockHash;
				});
				if (preBlock) {
					await this.AddBlock(preBlock.blockHeader, preBlock.blockData);
				} else {
					return false;
				}
				break;

			case blockCache2[0].blockHeader.index + 1:
				preBlock = blockCache2.find(block => {
					return block.blockHeader.hash === blockHeader.preBlockHash;
				});
				if (preBlock) {
					let prePreBlock;
					if (preBlock.blockHeader.index > 1) {
						prePreBlock = blockCache1.find(block => {
							return block.blockHeader.hash === preBlock.blockHeader.preBlockHash;
						});
					} else {
						prePreBlock = blockCache1[0];
					}
					await this.AddBlock(prePreBlock.blockHeader, prePreBlock.blockData);
					await this.AddBlock(preBlock.blockHeader, preBlock.blockData);
				} else {
					return false;
				}
				break;

			default:
				return false;
		}

		/*
		Validate preBlockHash and timeSign
		* */
		if (blockHeader.preBlockHash !== preBlock.blockHeader.hash) {
			return false;
		}

		if (blockHeader.index > 1) {
			if (blockHeader.firstTimeSign - preBlock.blockHeader.time < DURATION) {
				return false;
			}

			if (blockHeader.valSigns.length !== NUM_SIGN_PER_BLOCK) {
				return false;
			}

			/*
			Verify and validate valSigns
			* */
			let nodesOntTop = this.GetNodesOnTop();
			for (let i = 0; i < NUM_SIGN_PER_BLOCK; i++) {
				if (!Crypto.Verify(blockHeader.valSigns[i])) {
					return false;
				}

				let pubKeyHash = Crypto.Hash(blockHeader.valSigns[i].pubKey);
				if (!nodesOntTop.find(node => {
					return node.pubKeyHash === pubKeyHash;
				})) {
					return false;
				}

				let msg = JSON.parse(blockHeader.valSigns[i].msg);

				if (msg.curBlockHash !== preBlock.blockHeader.hash
					|| msg.nextBlockHash !== Crypto.Hash(JSON.stringify(blockHeader.infoNeedAgree))) {
					return false;
				}
			}

			/*
			Check no node signs 2 sequence blocks
			* */
			let valPubKeyHashes = blockHeader.valSigns.map(sign => {
				return Crypto.Hash(sign.pubKey);
			});

			let preBlockValPubKeyHashes = preBlock.blockHeader.valSigns.map(sign => {
				return Crypto.Hash(sign.pubKey);
			});

			if (_.union(valPubKeyHashes, preBlockValPubKeyHashes).length
				< valPubKeyHashes.length + preBlockValPubKeyHashes.length) {
				return false;
			}

			/*
			Verify and validate creatorSign
			* */
			if (!Crypto.Verify(blockHeader.creatorSign)) {
				return false;
			}

			if (blockHeader.creatorSign.msg !== JSON.stringify(valPubKeyHashes)) {
				return false;
			}

			/*
			Check creator is not on top and has waited enough
			* */
			let creatorPubKeyHash = Crypto.Hash(blockHeader.creatorSign.pubKey);

			if (nodesOntTop.indexOf(creatorPubKeyHash) >= 0) {
				return false;
			}

			// if (this.CalTimeMustWait(creatorPubKeyHash, new Date(blockHeader.time)) > 0) {
			// 	return false;
			// }
		}

		return true;
	}

	async ValidateBlockData(blockHeader, blockData, syncHeader) {
		let cb;
		let preBlock;

		switch (blockHeader.index) {
			case blockCache1[0].blockHeader.index:
				cb = () => {
					blockCache1.push({
						blockHeader: blockHeader,
						blockData: blockData
					});
					syncHeader();
				};
				break;

			case blockCache2[0].blockHeader.index:
				if (blockCache2[0].blockHeader.index === 1) {
					preBlock = blockCache1[0];
				} else {
					preBlock = blockCache1.find(block => {
						return block.blockHeader.hash === blockHeader.preBlockHash;
					});
				}

				if (preBlock) {
					await this.AddBlock(preBlock.blockHeader, preBlock.blockData);
				} else {
					return;
				}

				cb = () => {
					blockCache2.push({
						blockHeader: blockHeader,
						blockData: blockData
					});
					syncHeader();
				};
				break;

			case blockCache2[0].blockHeader.index + 1:
				let prePreBlock;

				if (blockCache2[0].blockHeader.index === 1) {
					preBlock = blockCache2[0];
					prePreBlock = blockCache1[0];
				} else {
					preBlock = blockCache2.find(block => {
						return block.blockHeader.hash === blockHeader.preBlockHash;
					});
					if (preBlock) {
						prePreBlock = blockCache1.find(block => {
							return block.blockHeader.hash === preBlock.blockHeader.preBlockHash;
						});
					} else {
						return;
					}
				}

				if (preBlock && prePreBlock) {
					await this.AddBlock(prePreBlock.blockHeader, prePreBlock.blockData);
					await this.AddBlock(preBlock.blockHeader, preBlock.blockData);
				} else {
					return;
				}
				cb = async () => {
					await this.HandleAfterNewBlock(blockHeader, blockData, syncHeader);
				};
				break;

			default:
				return;
		}

		if (blockHeader.index > 1) {
			if (blockData.txs.length !== NUM_TX_PER_BLOCK || blockData.merkleRoot !== blockHeader.merkleRoot) {
				return;
			}
		}

		for (let i = 0; i < NUM_TX_PER_BLOCK; i++) {
			if (await !blockData.txs[i].Validate(this)) {
				return;
			}
		}

		await cb();
	}
};