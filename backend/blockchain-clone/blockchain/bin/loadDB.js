const mongoose = require('mongoose');
mongoose.connect("mongodb://localhost/" + dbPrefix, { useNewUrlParser: true });
const db = mongoose.connection;

db.on('error', err => {
    console.error(err);
});

db.once('open', async () => {
    require('../module/block/model');
    let BlockCache = mongoose.model('block_cache');

    let blocks = await BlockCache.find({});
    if (blocks.length === 0) {
        global.blockCache1 = [{
	        blockHeader: {
		        index: 0,
		        validatorSigns: []
	        },
	        blockData: []
        }];
        global.blockCache2 = [{
	        blockHeader: {
		        index: 1,
		        validatorSigns: []
	        },
	        blockData: []
        }];
    } else {
        if (blocks[0].blockHeader.index < blocks[1].blockHeader.index) {
            global.blockCache1 = [blocks[0]];
            global.blockCache2 = [blocks[1]];
        } else {
            global.blockCache1 = [blocks[1]];
            global.blockCache2 = [blocks[0]];
        }
    }
    
    global.choosenBlock = blockCache2[0];
    const State = require('../blockchain-structure/state');
    global.globalState = new State(true);
    await globalState.Init();

    /**
     * Connect DNS server
     */
    const request = require('request');
    const syncDNS = () => {
        request.post({ url: dnsServer, form: { host: myHost } }, (err, res, body) => {
            if (err) {
                console.error(err);
            }
        });
    };

    request(dnsServer, (err, res, body) => {
        if (err) {
            console.error(err);
        } else {
            syncDNS();
            try {
                const { MakeConnectRequest } = require('../api/syncBlockChainApi');

                /**
                 * Connect and sync blockchain from every node in network
                 */
                let addrs = JSON.parse(body);
                addrs.forEach(addr => {
                    if (addr !== myHost) {
                        MakeConnectRequest(addr);
                    }
                });
            } catch (err) {
                console.error(err);
            }
        }
    });

    setInterval(() => {
        syncDNS();
    }, 1000 * 60 * 10);
});