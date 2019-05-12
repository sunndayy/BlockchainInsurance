const express = require('express');
const router = express.Router();
const TX = require('../../blockchain-structure/tx');
global.txCache = [];

router.post('/tx', async (req, res) => {
	res.end();
	let tx = TX({
		sign: req.body,
		tx: JSON.parse(req.body.msg)
	});
	if (mySession === WAIT_TO_COLLECT_SIGN && await tx.Validate(globalState)) {
		if (await globalState.PushTx(tx)) {
			return;
		}
	}
	txCache.push(tx);
});

module.exports = router;