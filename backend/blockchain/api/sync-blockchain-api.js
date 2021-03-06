const request = require('request');
const mongoose = require('mongoose');
const Crypto = require('../utils/crypto');
const Node = mongoose.model('node');

const State = require('../blockchain-structure/state');
const BlockHeader = require('../blockchain-structure/block-header');
const BlockData = require('../blockchain-structure/block-data');

/**
 * Make a request
 */
const MakeRequest = (url, msg, cb) => {
	request.post({url: 'http://' + url, form: Crypto.Sign(msg)}, async (err, res, body) => {
		if (err) {
			console.log(err);
		} else {
			try {
				let sign = JSON.parse(body);
				if (Crypto.Verify(sign)) {
					let msg = JSON.parse(sign.msg);
					await cb(msg, Crypto.Hash(sign.pubKey));
				}
			} catch (e) {
				console.log(e);
			}
		}
	});
};

/**
 * Make connect request
 */
const MakeConnectRequest = host => {
	let msg = {
		header: 'VERSION',
		host: HOST,
		time: new Date()
	};
	MakeRequest(host + '/version', msg, async (resMsg, pubKeyHash) => {
		if (resMsg.header === 'VER_ACK') {
			let node = await Node.findOneAndUpdate(
				{
					pubKeyHash: pubKeyHash
				},
				{
					$set: {
						host: host
					}
				},
				{
					new: true
				});
			if (node) {
				let index = globalState.nodes.findIndex(_node => {
					return _node.pubKeyHash === node.pubKeyHash;
				});
				if (index >= 0) {
					globalState.nodes[index].host = node.host;
				} else {
					globalState.nodes.push(node);
				}
				MakeSyncHeaderRequest(host, blockCache1[0].blockHeader.index);
			}
		}
	});
};

/**
 * Make get header request
 */
const HandleAfterGetHeader = async (host, blockHeader) => {
	let state = new State(false);
	await state.Init();
	blockHeader = new BlockHeader(blockHeader);
	if (await state.ValidateBlockHeader(blockHeader)) {
		MakeSyncDataRequest(host, blockHeader);
	}
};

const MakeSyncHeaderRequest = (host, index) => {
	if (index < 2) {
		index = 2;
	}
	let msg = {
		header: 'GET_HEADER',
		key: 'index',
		value: index,
		time: new Date()
	};
	MakeRequest(host + '/get-header', msg, async resMsg => {
		if (resMsg.header === 'HEADER') {
			await HandleAfterGetHeader(host, resMsg.blockHeader);
		}
	});
};

/**
 * Make get data request
 */
const HandleAfterGetData = async (host, blockHeader, blockData) => {
	let state = new State(false);
	await state.Init();
	blockData = new BlockData(blockData.txs);
	await state.ValidateBlockData(blockHeader, blockData, () => {
		MakeSyncHeaderRequest(host, blockHeader.index + 1);
	});
};

const MakeSyncDataRequest = (host, blockHeader) => {
	let msg = {
		header: 'GET_DATA',
		key: 'hash',
		value: Crypto.Hash(JSON.stringify(blockHeader)),
		time: new Date()
	};
	MakeRequest(host + '/get-data', msg, async resMsg => {
		if (resMsg.header === 'DATA') {
			await HandleAfterGetData(host, blockHeader, resMsg.blockData);
		}
	});
};

module.exports = {
	MakeConnectRequest,
	MakeSyncHeaderRequest,
	MakeSyncDataRequest,
	HandleAfterGetHeader,
	HandleAfterGetData
};