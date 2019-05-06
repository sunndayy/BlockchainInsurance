const request = require('request');
const mongoose = require('mongoose');
const Crypto = require('../utils/crypto');

const Node = mongoose.model('node');

const State = require('../blockchain-structure/state');
const BlockHeader = require('../blockchain-structure/blockHeader');
const BlockData = require('../blockchain-structure/blockData');

/**
 * Make a request
 */
const MakeRequest = (url, msg, cb) => {
    let handleResponse = (err, res, body) => {
        if (err) {
            console.error(err);
        } else {
            try {
                let sign = JSON.parse(body);
                if (Crypto.Verify(sign)) {
                    let msg = sign.msg;
                    cb(msg, Crypto.Hash(sign.pubKey));
                }
            } catch (err) {
                console.error(err);
            }
        }
    };
    let sign = Crypto.Sign(msg);
    request.post({ url: 'http://' + url, form: sign }, handleResponse);
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
    MakeRequest(host + '/version', msg, (resMsg, pubKeyHash) => {
        if (resMsg.header === 'VER_ACK') {
            Node.findOneAndUpdate(
                {
                    pubKeyHash: pubKeyHash
                },
                {
                    $set: {
                        host: host
                    }
                },
                {
                    upsert: true
                },
                (err, doc) => {
                if (err) {
                    console.error(err);
                } else {
                    MakeSyncHeaderRequest(host);
                }
            });
        }
    });
};

/**
 * Make get header request
 */
const HandleAfterGetHeader = async (host, blockHeader) => {
	let state = new State();
	await state.Init();
	blockHeader = new BlockHeader(blockHeader);
	if (state.ValidateBlockHeader(blockHeader)) {
		MakeSyncDataRequest(host, blockHeader);
	}
};

const MakeSyncHeaderRequest = host => {
    let index = (blockCache2[0].blockHeader.index === 1) ? 2 : blockCache1[0].blockHeader.index;
    let msg = {
        header: 'GET_HEADER',
        key: 'index',
        value: index
    };
    MakeRequest(host + '/get-header', msg, async resMsg => {
    	if (resMsg.header == 'HEADER') {
		    await HandleAfterGetHeader(host, resMsg.blockHeader);
	    }
    });
};

/**
 * Make get data request
 */
const HandleAfterGetData = async (host, blockHeader, blockData) => {
	let state = new State();
	await state.Init();
	blockData = new BlockData(blockData);
	if (state.ValidateBlockData(blockHeader, blockData)) {
		MakeSyncHeaderRequest(host);
	}
};

const MakeSyncDataRequest = (host, blockHeader) => {
    let msg = {
        header: 'GET_DATA',
        key: 'hash',
        value: Crypto.Hash(JSON.stringify(blockHeader))
    };
    MakeRequest(host + '/get-data', msg, async resMsg => {
    	if (resMsg.header == 'DATA') {
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