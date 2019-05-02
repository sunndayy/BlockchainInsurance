const express = require('express');
const router = express.Router();

const { FindByIndex, FindByHash } = require('./model');
const GetHost = require('../node/model').GetHost;

const SyncBlockChainApi = require('../../api/syncBlockChainApi');
const State = require('../../blockchain-structure/state');
const Crypto = require('../../utils/crypto');

const verifyMiddleware = require('../../middleware/verify-middleware');

const findBlock = async (key, value) => {
    let block;
    if (key == 'index') {
        block = await FindByIndex(parseInt(req.params.value));
    } else {
        block = await FindByHash(value);
        if (!block) {
            block = blockCache1.find(block => {
                return Crypto.Hash(JSON.stringify(block.blockHeader)) == value;
            });
        }
        if (!block) {
            block = blockCache2.find(block => {
                return Crypto.Hash(JSON.stringify(block.blockHeader)) == value;
            });
        }
    }
    return block;
}

router.post('/get-header', verifyMiddleware, async (req, res) => {
    if (req.body.header == 'GETHEADER') {
        if (nodes.indexOf(Crypto.Hash(req.body.pubKey)) >= 0) {
            let block = await findBlock(req.body.key, req.body.value);
            if (block) {
                let msg = {
                    blockHeader: block.blockHeader
                };
                res.json(Crypto.Sign(privKey, JSON.stringify(msg)));
            } else {
                res.error('No block was found');
            }
        } else {
            res.error('Invalid node');
        }
    } else {
        res.error('Invalid header');
    }
});

router.post('/header', async (req, res) => {
    let state = new State();
    if (state.ValidateBlockHeader(req.body.blockHeader)) {
        let host = await GetHost(Crypto.Hash(req.body.pubKey));
        if (host) {
            SyncBlockChainApi.MakeSyncDataRequest(host, req.body.blockHeader);
        }
    }
});

router.post('/get-data', verifyMiddleware, async (req, res) => {
    if (req.body.header == 'GETHEADER') {
        if (nodes.indexOf(Crypto.Hash(req.body.pubKey)) >= 0) {
            let block = await findBlock(req.body.key, req.body.value);
            if (block) {
                let msg = {
                    blockData: block.blockData
                };
                res.json(Crypto.Sign(privKey, JSON.stringify(msg)));
            } else {
                res.error('No block was found');
            }
        } else {
            res.error('Invalid node');
        }
    } else {
        res.error('Invalid header');
    }
});

router.post('/agree', verifyMiddleware, (req, res) => {
    let msg = {
        curBlockHash: Crypto.Hash(JSON.stringify(choosenBlock.blockHeader)),
        nextBlockHash: req.body.nextBlockHash,
        timeSign: (new Date()).getTime()
    };
    res.json({
        sign: Crypto.Sign(privKey, JSON.stringify(msg))
    });
});

module.exports = router;