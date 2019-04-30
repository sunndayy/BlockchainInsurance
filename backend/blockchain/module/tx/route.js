const express = require('express');
const router = express.Router();
global.txCache = [];

router.post('/tx', (req, res) => {
    let tx = req.body.tx;
    if (session == WAIT_TO_COLLECT_TX) {
        globalState.PushTx(tx);
    } else {
        txCache.push(tx, true);
    }
});

module.exports = router;