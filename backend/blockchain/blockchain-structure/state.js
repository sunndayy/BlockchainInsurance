const Crypto = require('../utils/crypto');
const mongoose = require('mongoose');
const Node = mongoose.model('node');
const FindByIndex = require('../module/block/model').FindByIndex;
const _ = require('lodash');
const BlockData = require('./blockData');
const request = require('request');

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
          return Crypto.Hash(JSON.stringify(block.blockHeader)) == choosenBlock.blockHeader.preBlockHash;
        });
      } else {
        preBlock = blockCache1[0];
      }
      this.AddBlock(preBlock.blockHeader, preBlock.blockData);
      this.AddBlock(choosenBlock.blockHeader, choosenBlock.blockData);
      this._txCache = [];
    } else {
      this._nodes = await Node.find();
    }
  }

  PushTx(tx, addToCache = false) {
    tx.UpdateStateAfterPushing(this);
    if (addToCache) {
      this._txCache.push(tx);
      let nodesOnTop = this.GetNodesOnTop();
      let pubKeyHash = Crypto.Hash(Crypto.GetPubKey(privKey));
      if (nodesOnTop.indexOf(pubKeyHash) < 0) {
        if (this.CalTimeMustWait(pubKeyHash) < 0) {
          let blockData = new BlockData(this._txCache);
          let blockHeader = {
            index: choosenBlock.blockHeader.index + 1,
            preBlockHash: JSON.stringify(choosenBlock.blockHeader),
            merkleRoot: blockData.merkleRoot
          };
          let nodesOnTop = this.GetNodesOnTop();
          nodesOnTop.forEach(node => {
            request.post({
              url: 'http://' + node.host + '/agree',
              form: {
                nextBlockHash: Crypto.Hash(JSON.stringify(blockHeader))
              }
            }, (err, res, body) => {
              if (err) {
                console.error(err);
              } else {
                try {
                  if (this.state == WAIT_TO_COLLECT_SIGN) {
                    let sign = JSON.parse(body);
                    let msg = JSON.parse(sign.msg);
                    if (msg.curBlockHash == Crypto.Hash(JSON.stringify(choosenBlock.blockHeader))
                        && msg.nextBlockHash == Crypto.Hash(JSON.stringify(blockHeader))) {
                      if (!blockHeader.validatorSigns) {
                        blockHeader.validatorSigns = [];
                      }
                      blockHeader.validatorSigns.push(sign);
                      if (blockHeader.validatorSigns.length == 2) {
                        blockCache1 = blockCache2;
                        blockCache2 = [{
                          blockHeader: blockHeader,
                          blockData: blockData
                        }];
                        state = WAIT_AFTER_NEW_BLOCK;
                        let validatorPubKeyHashes = blockHeader.validatorSigns.map(sign => {
                          return Crypto.Hash(sign.pubKey);
                        });
                        blockHeader.creatorSign = Crypto.Sign(privKey, JSON.stringify(validatorPubKeyHashes));
                        this._nodes.forEach(node => {
                          request.post('http://' + node.host + '/header', { form: blockHeader });
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
    // AddBlock, CalAge
    let node = this._nodes.find(node => {
      return node.pubKeyHash == pubKeyHash;
    });
    if (node) {
      let s = node.point * (time - new Date(node.lastTimeCreateBlock));
      if (s > 0) {
        return (10000000 - s) / node.point;
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
    let preBlock;
    switch (blockHeader.index) {
      case blockCache1[0].index: {
        preBlock = await FindByIndex(blockHeader.index - 1);
      }
        break;
      case blockCache2[0].index: {
        preBlock = blockCache1.find(block => {
          return Crypto.Hash(JSON.stringify(block.blockHeader)) == blockHeader.preBlockHash;
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
          return Crypto.Hash(JSON.stringify(block.blockHeader)) == blockHeader.preBlockHash;
        });
        if (preBlock) {
          let prePreBlock = blockCache1.find(block => {
            return Crypto.Hash(JSON.stringify(block.blockHeader)) == preBlock.blockHeader.preBlockHash;
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

    if (blockHeader.preBlockHash != Crypto.Hash(JSON.stringify(preBlock.blockHeader))) {
      return false;
    }

    if (blockHeader.firstTimeSign - preBlock.blockHeader.timestamp < 5000) {
      return false;
    }

    let nodesOntTop = this.GetNodesOnTop();
    for (let i = 0; i < 2; i++) {
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
      if (msg.curBlockHash != Crypto.Hash(JSON.stringify(preBlock.blockHeader))
          || msg.nextBlockHash != Crypto.Hash(JSON.stringify(blockHeader))) {
        return false;
      }
    }

    if (!Crypto.Verify(blockHeader.creatorSign)) {
      return false;
    }

    for (let i = 0; i < blockHeader.validatorSigns.length; i++) {
      if (!Crypto.Verify(blockHeader.validatorSigns[i])) {
        return false;
      }
      let msg = JSON.parse(blockHeader.validatorSigns[i].msg);
      if (msg.curBlockHash != Crypto.Hash(JSON.stringify(preBlock.blockHeader))) {
        return false;
      }
      let _msg = {
        index: blockHeader.index,
        preBlockHash: blockHeader.preBlockHash,
        merkleRoot: blockHeader.merkleRoot
      };
      if (msg.nextBlockHash != Crypto.Hash(JSON.stringify(_msg))) {
        return false;
      }
    }

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

    if (!Crypto.Verify(blockHeader.creatorSign)) {
      return false;
    }

    if (blockHeader.creatorSign.msg != JSON.stringify(validatorPubKeyHashes)) {
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
          return Crypto.Hash(JSON.stringify(block.blockHeader)) == blockHeader.preBlockHash;
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
          return Crypto.Hash(JSON.stringify(block.blockHeader)) == blockHeader.preBlockHash;
        });
        if (preBlock) {
          let prePreBlock = blockCache1.find(block => {
            return Crypto.Hash(JSON.stringify(block.blockHeader)) == preBlock.blockHeader.preBlockHash;
          });
          this.AddBlock(prePreBlock.blockHeader, prePreBlock.blockData);
          this.AddBlock(preBlock.blockHeader, preBlock.blockData);
        } else {
          return false;
        }
        cb = () => {
          state = WAIT_AFTER_NEW_BLOCK;
          blockCache1 = blockCache2;
          blockCache2 = [{
            blockHeader: blockHeader,
            blockData: blockData
          }];
          setTimeout(() => {
            blockCache2.sort((a, b) => {
              return a.blockHeader.firstTimeSign - b.blockHeader.firstTimeSign;
            });
            choosenBlock = blockCache2[0];
            state = WAIT_TO_COLLECT_SIGN;
          }, 5000);
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

    return true;
  }
  ///////////////////////////////////////////////////////////////////////
};