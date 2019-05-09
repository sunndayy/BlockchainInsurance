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

const SyncBlockChainAPI = require('../api/sync-blockchain-api');

module.exports = class State {
	constructor(isGlobalState = false) {
		this.txDict = {};
		this.isGlobalState = isGlobalState;
	}
	
	async Init() {
		console.log("Khởi tạo lại trạng thái và load danh sách node");
		this.nodes = await Node.find({});
		if (this.isGlobalState) {
			console.log("Khởi tạo trạng thái toàn cục");
			let preBlock;
			if (choosenBlock.blockHeader.index > 1) {
				preBlock = blockCache1.find(block => {
					return block.blockHeader.hash === choosenBlock.blockHeader.preBlockHash;
				});
			} else {
				preBlock = blockCache1[0];
			}
			console.log("Thêm prePreBlock");
			await this.AddBlock(preBlock.blockHeader, preBlock.blockData);
			console.log("Thêm preBlock");
			await this.AddBlock(choosenBlock.blockHeader, choosenBlock.blockData);
			console.log("Xóa tất cả block trong blockCache database");
			await BlockCache.remove({});
			console.log("Thêm 2 block tạm vào blockCache database");
			await BlockCache.insertMany([preBlock, choosenBlock]);
			console.log("Reset mảng transaction");
			this.txCache = [];
		}
		
	}
	
	HandleAfterNewBlock(blockHeader, blockData, cb) {
		console.log("Tìm preBlock");
		let preBlock = blockCache2.find(block => {
			return block.blockHeader.hash === blockHeader.preBlockHash;
		});
		
		console.log("Tìm prePreBlock");
		let prePreBlock;
		if (preBlock.blockHeader.index > 1) {
			prePreBlock = blockCache1.find(block => {
				return block.blockHeader.hash === preBlock.blockHeader.preBlockHash;
			});
		} else {
			prePreBlock = blockCache1[0];
		}
		prePreBlock.hash = prePreBlock.blockHeader.hash;
		
		console.log("Thêm prePreBlock vào database");
		(new Block(prePreBlock)).save(async (err, doc) => {
			if (err) {
				console.error(err);
			} else {
				console.log("Cập nhật lại session thành chờ sau khi nhận/tạo block mới, preBlock và prePreBlock");
				mySession = WAIT_AFTER_NEW_BLOCK;
				blockCache1 = blockCache2;
				blockCache2 = [{
					blockHeader: blockHeader,
					blockData: blockData
				}];
				
				console.log("Set timeout sau khi nhận/tạo block mới");
				setTimeout( async () => {
					console.log("Cập nhật session thành đang thu thập giao dịch và chữ ký");
					mySession = WAIT_TO_COLLECT_SIGN;
					console.log("Chọn block có thời gian thu thập chữ ký nhanh nhất làm nextBlock");
					blockCache2.sort((a, b) => {
						return a.blockHeader.firstTimeSign - b.blockHeader.firstTimeSign;
					});
					choosenBlock = blockCache2[0];
					globalState = new State(true);
					await globalState.Init();
					console.log("Thêm các giao dịch trong cache vào block mới tạo");
					txCache.forEach( async tx => {
						await globalState.PushTx(tx, true);
					});
				}, DURATION);
				
				console.log("Duyệt qua từng giao dịch trong block vào cập nhật lại database");
				for (let i = 0; i < prePreBlock.blockData.txs.length; i++) {
					let tx = prePreBlock.blockData.txs[i];
					await tx.UpdateDB(this);
				}
				
				console.log("Cộng điểm cho node thu thập");
				if (prePreBlock.blockHeader.creatorSign) {
					let creatorPubKeyHash = Crypto.Hash(prePreBlock.blockHeader.creatorSign.pubKey);
					await Node.findOneAndUpdate({ pubKeyHash: creatorPubKeyHash }, { $inc: { point: CREATOR_PRIZE } });
				}
				
				console.log("Cộng điểm cho node ký tên");
				let validatorPubKeyHashes = prePreBlock.blockHeader.validatorSigns.map(sign => Crypto.Hash(sign.pubKey));
				Node.updateMany({pubKeyHash: { $in: validatorPubKeyHashes }}, { $inc: { point: VALIDATOR_PRIZE } }, (err, stats) => {
					if (err) {
						console.error(err);
					} else {
						cb();
					}
				});
				
			}
		});
	}
	
	async PushTx(tx, addToCache = false) {
		console.log("Kiểm tra giao dịch hợp lệ")
		if (await tx.Validate(this)) {
			console.log("Cập nhật trạng thái");
			tx.UpdateState(this);
			if (addToCache) {
				console.log("Thêm giao dịch vào cache");
				this.txCache.push(tx);
				
				console.log("Nếu thu thập đủ giao dịch");
				if (this.txCache.length === NUM_TX_PER_BLOCK) {
					console.log("Tìm danh sách node trong top");
					let nodesOnTop = this.GetNodesOnTop();
					console.log("Map danh sách node trong top thành danh sách pubKeyHash");
					let nodesOnTopPubKeyHashes = nodesOnTop.map(node => node.pubKeyHash);
					if (nodesOnTopPubKeyHashes.indexOf(Crypto.PUB_KEY_HASH) < 0) {
						console.log("Kiểm tra đã tích lũy đủ điểm");
						if (this.CalTimeMustWait(Crypto.PUB_KEY_HASH) <= 0) {
							let blockData = new BlockData(this.txCache);
							let blockHeader = new BlockHeader({
								index: choosenBlock.blockHeader.index + 1,
								preBlockHash: Crypto.Hash(JSON.stringify(choosenBlock.blockHeader)),
								merkleRoot: blockData.merkleRoot
							});
							
							let _this = this;
							console.log("Thu thập chữ ký của các node xác nhận");
							nodesOnTop.forEach(node => {
								if (node.pubKeyHash !== Crypto.PUB_KEY_HASH && node.host) {
									request.post({
										url: 'http://' + node.host + '/agree',
										form: Crypto.Sign({ nextBlockHash: Crypto.Hash(JSON.stringify(blockHeader.infoNeedAgree)) })
									}, (err, res, body) => {
										if (err) {
											console.error(err);
										} else {
											try {
												if (mySession === WAIT_TO_COLLECT_SIGN) {
													let sign = JSON.parse(body);
													let msg = JSON.parse(sign.msg);
													
													if (msg.curBlockHash === choosenBlock.blockHeader.hash
														&& msg.nextBlockHash === Crypto.Hash(JSON.stringify(blockHeader.infoNeedAgree))) {
														if (!blockHeader.validatorSigns) {
															blockHeader.validatorSigns = [];
														}
														console.log("Thêm chữ ký node xác nhận vào block mới tạo");
														blockHeader.validatorSigns.push(sign);
														
														if (blockHeader.validatorSigns.length === NUM_SIGN_PER_BLOCK) {
															let validatorPubKeyHashes = blockHeader.validatorSigns.map(sign => {
																return Crypto.Hash(sign.pubKey);
															});
															console.log("Node thu thập ký tên lên danh sách node xác nhận");
															blockHeader.Sign();
															
															console.log("Xử lý sau khi tạo block mới");
															_this.HandleAfterNewBlock(blockHeader, blockData, () => {
																console.log('Success');
																// _this.nodes.forEach(node => {
																// 	if (node.host !== HOST && node.host) {
																// 		request.post('http://' + node.host + '/header', { form: Crypto.Sign(blockHeader)}, (err, res, body) => {
																//
																// 		});
																// 	}
																// });
															});
															
														}
													}
												}
											} catch (e) {
												console.error(e);
											}
										}
									});
								}
							});
						}
					}
				}
			}
		}
	}
	
	async AddBlock(blockHeader, blockData) {
		let _this = this;
		console.log("Duyệt qua từng giao dịch và cập nhật trạng thái");
		for (let i = 0; i < blockData.txs.length; i++) {
			let tx = blockData.txs[i];
			await this.PushTx(tx);
		}
		
		if (blockHeader.creatorSign) {
			console.log("Cộng điểm cho node thu thập");
			let creator = this.nodes.find(node => {
				return node.pubKeyHash === Crypto.Hash(blockHeader.creatorSign.pubKey);
			});
			creator.point += CREATOR_PRIZE;
		}
		
		console.log("Cộng điểm cho node xác nhận");
		blockHeader.validatorSigns.forEach(sign => {
			let validator = _this.nodes.find(node => {
				return node.pubKeyHash === Crypto.Hash(sign.pubKey);
			});
			validator.point += VALIDATOR_PRIZE;
		});
	}
	
	CalTimeMustWait(pubKeyHash, time = new Date()) {
		let node = this.nodes.find(node => {
			return node.pubKeyHash === pubKeyHash;
		});
		
		if (node) {
			if (!node.lastTimeCreateBlock) {
				return 0;
			}
			
			let s = node.point * (time - new Date(node.lastTimeCreateBlock));
			if (s > 0) {
				return (NEED_POINT - s) / node.point;
			}
		}
		
		return 3600000;
	}
	
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
			console.log("PreBlockHash không hợp lệ");
			return false;
		}
		
		if (preBlock.blockHeader.timestamp) {
			if (blockHeader.firstTimeSign - preBlock.blockHeader.timestamp < DURATION) {
				console.log("Chưa chờ đủ thời gian giữa 2 block liên tiếp");
				return false;
			}
		}
		
		/*
		Verify and validate validatorSigns
		* */
		let nodesOntTop = this.GetNodesOnTop();
		for (let i = 0; i < NUM_SIGN_PER_BLOCK; i++) {
			if (!Crypto.Verify(blockHeader.validatorSigns[i])) {
				console.log("Chữ ký node xác nhận không hợp lệ");
				return false;
			}
			
			let pubKeyHash = Crypto.Hash(blockHeader.validatorSigns[i].pubKey);
			if (!nodesOntTop.find(node => {
				return node.pubKeyHash === pubKeyHash;
			})) {
				console.log("Node xác nhận không nằm trong top");
				return false;
			}
			
			let msg = JSON.parse(blockHeader.validatorSigns[i].msg);
			
			if (msg.curBlockHash !== preBlock.blockHeader.hash
				|| msg.nextBlockHash !== Crypto.Hash(JSON.stringify(blockHeader.infoNeedAgree))) {
				console.log("Thông tin cần xác nhận không đúng");
				return false;
			}
		}
		
		/*
		Check no node signs 2 sequence blocks
		* */
		let validatorPubKeyHashes = blockHeader.validatorSigns.map(sign => {
			return Crypto.Hash(sign.pubKey);
		});
		
		let preBlockValidatorPubkeyHashes = preBlock.blockHeader.validatorSigns.map(sign => {
			return Crypto.Hash(sign.pubKey);
		});
		
		if (_.union(validatorPubKeyHashes, preBlockValidatorPubkeyHashes).length
			< validatorPubKeyHashes.length + preBlockValidatorPubkeyHashes.length) {
			console.log("Node xác nhận ký 2 block liên tiếp");
			return false;
		}
		
		/*
		Verify and validate creatorSign
		* */
		if (!Crypto.Verify(blockHeader.creatorSign)) {
			console.log("Chữ ký node thu thập không hợp lệ");
			return false;
		}
		
		// if (blockHeader.creatorSign.msg !== Crypto.Hash(JSON.stringify(validatorPubKeyHashes))) {
		// 	console.log("Thông tin node thu thập ký không hợp lệ");
		// 	return false;
		// }
		
		/*
		Check creator is not on top and has waited enough
		* */
		let creatorPubKeyHash = Crypto.Hash(blockHeader.creatorSign.pubKey);
		
		if (nodesOntTop.indexOf(creatorPubKeyHash) >= 0) {
			console.log("Node thu thập nằm trong top");
			return false;
		}
		
		if (this.CalTimeMustWait(creatorPubKeyHash, new Date(blockHeader.timeStamp)) > 0) {
			console.log("Node thu thập chưa chờ đủ điểm");
			return false;
		}
		
		console.log("Block header hợp lệ");
		return true;
	}
	
	async ValidateBlockData(blockHeader, blockData, host) {
		let cb;
		let preBlock;
		switch (blockHeader.index) {
			case blockCache1[0].index:
				cb = () => {
					blockCache1.push({
						blockHeader: blockHeader,
						blockData: blockData
					});
				};
				break;

			case blockCache2[0].index:
				preBlock = blockCache1.find(block => {
					return block.blockHeader.hash === blockHeader.preBlockHash;
				});
				if (preBlock) {
					await this.AddBlock(preBlock.blockHeader, preBlock.blockData);
				} else {
					return false;
				}
				cb = () => {
					blockCache2.push({
						blockHeader: blockHeader,
						blockData: blockData
					});
				};
				break;

			case blockCache2[0].index + 1:
				preBlock = blockCache2.find(block => {
					return block.blockHeader.hash === blockHeader.preBlockHash;
				});
				if (preBlock) {
					let prePreBlock = blockCache1.find(block => {
						return block.blockHeader.hash === preBlock.blockHeader.preBlockHash;
					});
					await this.AddBlock(prePreBlock.blockHeader, prePreBlock.blockData);
					await this.AddBlock(preBlock.blockHeader, preBlock.blockData);
				} else {
					return false;
				}
				cb = () => {
					this.HandleAfterNewBlock(blockHeader, blockData, () => {
						SyncBlockChainAPI.MakeConnectRequest(host);
					});
				};
				break;

			default:
				return false;
		}

		if (blockData.length !== NUM_TX_PER_BLOCK) {
			console.log("Số lượng transaction không đúng");
			return false;
		}

		if (blockData.merkleRoot !== blockHeader.merkleRoot) {
			console.log("Merkleroot không đúng");
			return false;
		}

		for (let i = 0; i < NUM_TX_PER_BLOCK; i++) {
			if (await !blockData[i].Validate(this)) {
				console.log("Giao dịch không hợp lệ");
				return false;
			}
		}

		cb();
		console.log("Block data hợp lệ");
		return true;
	}
};