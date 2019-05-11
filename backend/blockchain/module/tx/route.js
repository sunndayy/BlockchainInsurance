const express = require('express');
const router = express.Router();
const TX = require('../../blockchain-structure/tx');
global.txCache = [];

router.post('/tx', async (req, res) => {
	let tx = TX({
		sign: req.body,
		tx: JSON.parse(req.body.msg)
	});
	if (mySession === WAIT_TO_COLLECT_SIGN && await tx.Validate(globalState)) {
		if (globalState.PushTx(tx, true)) {
			return ;
		}
	}
	txCache.push(tx, true);
});

module.exports = router;