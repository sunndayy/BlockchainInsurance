const express = require('express');
const router = express.Router();
const debug = require('debug');

const TX = require('../../blockchain-structure/tx');
global.txCache = [];

router.post('/tx', async (req, res) => {
	try {
		let tx = TX({
			sign: req.body,
			tx: JSON.parse(req.body.msg)
		});
		if (mySession === WAIT_TO_COLLECT_SIGN && await tx.Validate(globalState)) {
			if (await globalState.PushTx(tx)) {
				res.end('Valid tx');
				return;
			}
		}
		txCache.push(tx);
		res.end();
	} catch (e) {
		res.end(e.message);
	}
});

module.exports = router;