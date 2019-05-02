const Crypto = require('../utils/crypto');

const FindByIndex = require('../module/block/model').FindByIndex;
const BlockData = require('./blockData');

const mongoose = require('mongoose');
const Node = mongoose.model('node');
const Block = mongoose.model('block');

const request = require('request');
const _ = require('lodash');

module.exports = class State {
    constructor(isGlobalState = false) {
        this._txDict = {};
        this._isGlobalState = isGlobalState;
    }

    async Init() {
        if (this._isGlobalState) {
            let preBlock;
            if (choosenBlock.blockHeader.index > 1) {
                preBlock = blockCache1.find(block => {
                    return block.blockHeader.hash == choosenBlock.blockHeader.preBlockHash;
                });
            } else {
                preBlock = blockCache1[0];
            }
            this.AddBlock(preBlock.blockHeader, preBlock.blockData);
            this.AddBlock(choosenBlock.blockHeader, choosenBlock.blockData);
            this._txCache = [];
        }
        this._nodes = await Node.find();
    }

    HandleAfterNewBlock(blockHeader, blockData, cb) {
        let preBlock = blockCache2.find(block => {
            return block.blockHeader.hash == blockHeader.preBlockHash;
        });

        let prePreBlock = blockCache1.find(block => {
            return block.blockHeader.hash == preBlock.blockHeader.preBlockHash;
        });

        prePreBlock.hash = prePreBlock.blockHeader.hash;

        (new Block(prePreBlock)).save(async (err, doc) => {
            if (err) {
                console.error(err);
            } else {
                session = WAIT_AFTER_NEW_BLOCK;
                blockCache1 = blockCache2;
                blockCache2 = [{
                    blockHeader: blockHeader,
                    blockData: blockData
                }];

                setTimeout( async () => {
                    blockCache2.sort((a, b) => {
                        return a.blockHeader.firstTimeSign - b.blockHeader.firstTimeSign;
                    });
                    choosenBlock = blockCache2[0];
                    session = WAIT_TO_COLLECT_SIGN;
                    globalState = new State(true);
                    txCache.forEach( async tx => {
                        await globalState.PushTx(tx, true);
                    });
                }, DURATION);

                doc.blockData.forEach(async tx => {
                    await tx.UpdateDB(this);
                });

                let creatorPubKeyHash = Crypto.Hash(doc.blockHeader.creatorSign.pubKey);
                await Node.findOneAndUpdate({ pubKeyHash: creatorPubKeyHash }, { $inc: { point: 10 } });

                doc.blockHeader.validatorSigns.forEach(async sign => {
                    let validatorPubKeyHash = Crypto.Hash(sign.pubKey);
                    await Node.findOneAndUpdate({ pubKeyHash: validatorPubKeyHash }, { $inc: { point: 2 } });
                });

                if (cb) {
                    cb();
                }
            }
        });
    }

    async PushTx(tx, addToCache = false) {
        if (await tx.Validate(this)) {
            tx.UpdateState(this);

            if (addToCache) {
                this._txCache.push(tx);

                if (this._txCache.length == NUM_TX_PER_BLOCK) {
                    let nodesOnTop = this.GetNodesOnTop();
                    let pubKeyHash = Crypto.Hash(Crypto.GetPubKey(privKey));

                    if (nodesOnTop.indexOf(pubKeyHash) < 0) {
                        if (this.CalTimeMustWait(pubKeyHash) <= 0) {
                            let blockData = new BlockData(this._txCache);
                            let blockHeader = new BlockHeader({
                                index: choosenBlock.blockHeader.index + 1,
                                preBlockHash: JSON.stringify(choosenBlock.blockHeader),
                                merkleRoot: blockData.merkleRoot
                            });

                            nodesOnTop.forEach(node => {
                                request.post({
                                    url: 'http://' + node.host + '/agree',
                                    form: {
                                        nextBlockHash: Crypto.Hash(JSON.stringify(blockHeader.infoNeedAgree))
                                    }
                                }, (err, res, body) => {
                                    if (err) {
                                        console.error(err);
                                    } else {
                                        try {
                                            if (this.state == WAIT_TO_COLLECT_SIGN) {
                                                let sign = JSON.parse(body);
                                                let msg = JSON.parse(sign.msg);

                                                if (msg.curBlockHash == choosenBlock.blockHeader.hash
                                                    && msg.nextBlockHash == Crypto.Hash(JSON.stringify(blockHeader.infoNeedAgree))) {
                                                    if (!blockHeader.validatorSigns) {
                                                        blockHeader.validatorSigns = [];
                                                    }
                                                    blockHeader.validatorSigns.push(sign);
                                                    if (blockHeader.validatorSigns.length == NUM_SIGN_PER_BLOCK) {
                                                        let validatorPubKeyHashes = blockHeader.validatorSigns.map(sign => {
                                                            return Crypto.Hash(sign.pubKey);
                                                        });
                                                        blockHeader.creatorSign = Crypto.Sign(privKey, JSON.stringify(validatorPubKeyHashes));

                                                        this.HandleAfterNewBlock(blockHeader, blockData, () => {
                                                            this._nodes.forEach(node => {
                                                                request.post('http://' + node.host + '/header', { form: blockHeader });
                                                            });
                                                        });
                                                    }
                                                }
                                            }
                                        } catch (e) {
                                            console.error(e);
                                        }
                                    }
                                });
                            });
                        }
                    }
                }
            }
        }
    }

    AddBlock(blockHeader, blockData) {
        let _this = this;
        blockData.forEach(tx => {
            _this.PushTx(tx);
        });

        if (!this._isGlobalState) {
            if (blockHeader.creatorSign) {
                let creator = this._nodes.find(node => {
                    return node.pubKeyHash == Crypto.Hash(blockHeader.creatorSign.pubKey);
                });
                creator.point += 5;
            }

            blockHeader.validatorSigns.forEach(sign => {
                let validator = _this._nodes.find(node => {
                    return node.pubKeyHash == Crypto.Hash(sign.pubKey);
                });
                validator.point += 1;
            });
        }
    }

    CalTimeMustWait(pubKeyHash, time = new Date()) {
        let node = this._nodes.find(node => {
            return node.pubKeyHash == pubKeyHash;
        });

        if (node) {
            let s = node.point * (time - new Date(node.lastTimeCreateBlock));
            if (s > 0) {
                return (NEED_POINT - s) / node.point;
            }
        }

        return 3600000;
    }

    GetNodesOnTop() {
        this._nodes.sort((a, b) => {
            return b.point - a.point;
        });
        return this._nodes.splice(0, 2);
    }

    get txDict() {
        return this._txDict;
    }

    get nodes() {
        return this._nodes;
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
            case blockCache1[0].index: {
                preBlock = await FindByIndex(blockHeader.index - 1);
            }
                break;

            case blockCache2[0].index: {
                preBlock = blockCache1.find(block => {
                    return block.blockHeader.hash == blockHeader.preBlockHash;
                });
                if (preBlock) {
                    this.AddBlock(preBlock.blockHeader, preBlock.blockData);
                } else {
                    return false;
                }
            }
                break;

            case blockCache2[0].index + 1: {
                preBlock = blockCache2.find(block => {
                    return block.blockHeader.hash == blockHeader.preBlockHash;
                });
                if (preBlock) {
                    let prePreBlock = blockCache1.find(block => {
                        return block.blockHeader.hash == preBlock.blockHeader.preBlockHash;
                    });
                    this.AddBlock(prePreBlock.blockHeader, prePreBlock.blockData);
                    this.AddBlock(preBlock.blockHeader, preBlock.blockData);
                } else {
                    return false;
                }
            }
                break;

            default:
                return false;
        }

        /*
        Validate preBlockHash and timeSign
        * */
        if (blockHeader.preBlockHash != preBlock.blockHeader.hash) {
            return false;
        }

        if (blockHeader.firstTimeSign - preBlock.blockHeader.timestamp < DURATION) {
            return false;
        }

        /*
        Verify and validate validatorSigns
        * */
        let nodesOntTop = this.GetNodesOnTop();
        for (let i = 0; i < NUM_SIGN_PER_BLOCK; i++) {
            if (!Crypto.Verify(blockHeader.validatorSigns[i])) {
                return false;
            }

            let pubKeyHash = Crypto.Hash(blockHeader.validatorSigns[i].pubKey);
            if (!nodesOntTop.find(node => {
                return node.pubKeyHash == pubKeyHash
            })) {
                return false;
            }

            let msg = JSON.stringify(blockHeader.validatorSigns[i].msg);

            if (msg.curBlockHash != preBlock.blockHeader.hash
                || msg.nextBlockHash != Crypto.Hash(JSON.stringify(blockHeader.infoNeedAgree))) {
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
            return false;
        }

        /*
        Verify and validate creatorSign
        * */
        if (!Crypto.Verify(blockHeader.creatorSign)) {
            return false;
        }

        if (blockHeader.creatorSign.msg != JSON.stringify(validatorPubKeyHashes)) {
            return false;
        }

        /*
        Check creator is not on top and has waited enough
        * */
        let creatorPubKeyHash = Crypto.Hash(blockHeader.creatorSign.pubKey);

        if (nodesOntTop.indexOf(creatorPubKeyHash) >= 0) {
            return false;
        }

        if (this.CalTimeMustWait(creatorPubKeyHash, new Date(blockHeader.timeStamp)) > 0) {
            return false;
        }

        return true;
    }

    async ValidateBlockData(blockHeader, blockData) {
        let cb;

        switch (blockHeader.index) {
            case blockCache1[0].index:
                cb = () => {
                    blockCache1.push({
                        blockHeader: blockHeader,
                        blockData: blockData
                    });
                };
                break;

            case blockCache2[0].index: {
                let preBlock = blockCache1.find(block => {
                    return block.blockHeader.hash == blockHeader.preBlockHash;
                });
                if (preBlock) {
                    this.AddBlock(preBlock.blockHeader, preBlock.blockData);
                } else {
                    return false;
                }
                cb = () => {
                    blockCache2.push({
                        blockHeader: blockHeader,
                        blockData: blockData
                    });
                };
            }
                break;

            case blockCache2[0].index + 1: {
                let preBlock = blockCache2.find(block => {
                    return block.blockHeader.hash == blockHeader.preBlockHash;
                });
                if (preBlock) {
                    let prePreBlock = blockCache1.find(block => {
                        return block.blockHeader.hash == preBlock.blockHeader.preBlockHash;
                    });
                    this.AddBlock(prePreBlock.blockHeader, prePreBlock.blockData);
                    this.AddBlock(preBlock.blockHeader, preBlock.blockData);
                } else {
                    return false;
                }
                cb = () => {
                    this.HandleAfterNewBlock(blockHeader, blockData);
                };
            }
                break;
            default:
                return false;
        }

        if (blockData.length != 1) {
            return false;
        }

        if (blockData.merkleRoot != blockHeader.merkleRoot) {
            return false;
        }

        for (let i = 0; i < 1; i++) {
            if (await !blockData[i].Validate(this)) {
                return false;
            }
        }

        cb();
        return true;
    }
};