const express = require('express');
const router = express.Router();
const Tx = require('../../blockchain-structure/tx');
global.txCache = [];

router.post('/tx', (req, res) => {
    let tx = Tx(req.body.tx);
    if (session == WAIT_TO_COLLECT_TX) {
        globalState.PushTx(tx);
    } else {
        txCache.push(tx, true);
    }
});

module.exports = router;