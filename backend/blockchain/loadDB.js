const mongoose = require('mongoose');
mongoose.connect("mongodb://localhost/" + dbPrefix, { useNewUrlParser: true });
const db = mongoose.connection;

db.on('error', err => {
    console.error(err);
});

db.once('open', async () => {
    const Schema = mongoose.Schema;
    const BlockSchema = new Schema({
        blockHeader                     : {
            index                           : { type: Number, require: true },
            preBlockHash                    : { type: String },
            merkleRoot                      : { type: String, require: true },
            validatorSigns                  : [ Schema.Types.Mixed ],
            creatorSign                      : Schema.Types.Mixed
        },
        blockData                         : [ Schema.Types.Mixed ]
    });

    let BlockCache = mongoose.model('block_cache', BlockSchema);

    let values = await BlockCache.find({});
    if (values.length == 0) {
        global.blockCache1 = [new BlockCache({
            blockHeader: {
                index: 0,
                validatorSigns: []
            },
            blockData: []
        })];
        global.blockCache2 = [new BlockCache({
            blockHeader: {
                index: 1,
                validatorSigns: []
            },
            blockData: []
        })];
    } else {
        if (values[0].blockHeader.index < values[1].blockHeader.index) {
            global.blockCache1 = [values[0]];
            global.blockCache2 = [values[1]];
        } else {
            global.blockCache1 = [values[1]];
            global.blockCache2 = [values[0]];
        }
    }
    global.choosenBlock = blockCache2[0];
    const State = require('./blockchain-structure/state');
    global.globalState = new State(true);
    globalState.Init();

    /**
     * Connect DNS server
     */
    const request = require('request');
    request(dnsServer, (err, res, body) => {
        if (err) {
            console.error(err);
        } else {
            request.post(dnsServer).form({ host: myHost });
            try {
                const { MakeConnectRequest } = require('./api/syncBlockChainApi');
                /**
                 * Connect and sync blockchain from every node in network
                 */
                let addrs = JSON.parse(body);
                addrs.forEach(addr => {
                    MakeConnectRequest(addr);
                });
            } catch (err) {
                console.error(err);
            }
        }
    });
});