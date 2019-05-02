const State = require('../blockchain-structure/state');
const Crypto = require('../utils/crypto');
const request = require('request');
const mongoose = require('mongoose');
const Node = mongoose.model('node');

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
                    let msg;
                    try {
                        msg = JSON.parse(sign.msg);
                    } catch (err) {
                        msg = sign.msg;
                    }
                    cb(msg, sign.pubKey);
                }
            } catch (err) {
                console.error(err);
            }
        }
    };
    let sign = Crypto.Sign(privKey, JSON.stringify(msg));
    request.post({ url: 'http://' + url, form: sign }, handleResponse);
}

/**
 * Make connect request
 */
const MakeConnectRequest = host => {
    let msg = {
        header: 'VERSION',
        host: myHost,
        time: new Date()
    };
    MakeRequest(host + '/version', msg, (resMsg, pubKey) => {
        if (resMsg == 'VERACK') {
            Node.findOneAndUpdate(
                {
                    pubKeyHash: Crypto.Hash(pubKey)
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
const MakeSyncHeaderRequest = host => {
    let index = (blockCache2[0].blockHeader.index == 1) ? 2 : blockCache2[0].blockHeader.index - 1;
    let msg = {
        header: 'GETHEADER',
        key: 'index',
        value: index
    };
    MakeRequest(host + '/get-header', msg,  resMsg => {
        let state = new State();
        if (state.ValidateBlockHeader(resMsg)) {
            MakeSyncDataRequest(host, resMsg);
        }
    });
};

/**
 * Make get data request
 */
const MakeSyncDataRequest = (host, blockHeader) => {
    let msg = {
        header: 'GETDATA',
        key: 'hash',
        value: Crypto.Hash(JSON.stringify(blockHeader))
    };
    MakeRequest(host + '/get-data', msg, resMsg => {
        let state = new State();
        if (state.ValidateBlockData(blockHeader, resMsg)) {
            MakeSyncHeaderRequest(host);
        }
    });
};

module.exports = { MakeConnectRequest, MakeSyncHeaderRequest, MakeSyncDataRequest };