const express = require('express');
const router = express.Router();
const Tx = require('../../blockchain-structure/tx');
const verifyMiddleWare = require('../../middleware/verify-middleware');
global.txCache = [];

router.post('/tx', verifyMiddleWare, async (req, res) => {
    let tx = Tx(req.body.tx);
	if (mySession === WAIT_TO_COLLECT_TX) {
        await globalState.PushTx(tx);
    } else {
        txCache.push(tx, true);
    }
});

module.exports = router;