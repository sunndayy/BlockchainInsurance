const express = require('express');
const router = express.Router();
const debug = require('debug');

const {FindByIndex, FindByHash} = require('./model');
const GetHost = require('../node/model').GetHost;

const SyncBlockChainApi = require('../../api/sync-blockchain-api');
const State = require('../../blockchain-structure/state');
const Crypto = require('../../utils/crypto');

const verifyMiddleware = require('../../middleware/verify-middleware');

const findBlock = async (key, value) => {
	let block;
	if (key === 'index') {
		switch (value) {
			case blockCache1[0].blockHeader.index:
				block = blockCache1.find(block => {
					return Crypto.Hash(JSON.stringify(block.blockHeader)) === choosenBlock.blockHeader.preBlockHash;
				});
				break;
			case blockCache2[0].blockHeader.index:
				block = choosenBlock;
				break;
			default:
				block = await FindByIndex(value);
				break;
		}
	} else {
		block = await FindByHash(value);
		if (!block) {
			block = blockCache1.find(block => {
				return Crypto.Hash(JSON.stringify(block.blockHeader)) === value;
			});
		}
		if (!block) {
			block = blockCache2.find(block => {
				return Crypto.Hash(JSON.stringify(block.blockHeader)) === value;
			});
		}
	}
	return block;
}

router.post('/get-header', verifyMiddleware, async (req, res) => {
	if (req.body.header === 'GET_HEADER') {
		let index = globalState.nodes.findIndex(node => {
			return node.pubKeyHash === req.body.pubKeyHash;
		});
		if (index >= 0) {
			let block = await findBlock(req.body.key, req.body.value);
			if (block) {
				let msg = {
					header: 'HEADER',
					blockHeader: block.blockHeader
				};
				res.json(Crypto.Sign(msg));
			} else {
				res.end('No block was found');
			}
		} else {
			res.end('Invalid node');
		}
	} else {
		res.end('Invalid header');
	}
});

router.post('/header', verifyMiddleware, async (req, res) => {
	try {
		let host = await GetHost(req.body.pubKeyHash);
		await SyncBlockChainApi.HandleAfterGetHeader(host, req.body);
		res.end();
	} catch (e) {
		debug(e);
		res.end(e.message);
	}
});

router.post('/get-data', verifyMiddleware, async (req, res) => {
	if (req.body.header === 'GET_DATA') {
		let index = globalState.nodes.findIndex(node => {
			return node.pubKeyHash === req.body.pubKeyHash;
		});
		if (index >= 0) {
			let block = await findBlock(req.body.key, req.body.value);
			if (block) {
				let msg = {
					header: 'DATA',
					blockData: block.blockData
				};
				res.json(Crypto.Sign(JSON.stringify(msg)));
			} else {
				res.end('No block was found');
			}
		} else {
			res.end('Invalid node');
		}
	} else {
		res.end('Invalid header');
	}
});

router.post('/agree', verifyMiddleware, (req, res) => {
	let msg = {
		header: 'AGREE',
		curBlockHash: Crypto.Hash(JSON.stringify(choosenBlock.blockHeader)),
		nextBlockHash: req.body.nextBlockHash,
		timeSign: (new Date()).getTime()
	};
	res.json(Crypto.Sign(msg));
});

module.exports = router;