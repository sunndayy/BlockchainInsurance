const express = require('express');
const router = express.Router();

const FindNode = require('./model').FindNode;

const verifyMiddleware = require('../../middleware/verify-middleware');
const Crypto = require('../../utils/crypto');

router.post('/version', verifyMiddleware, async (req, res) => {
	if (req.body.header === 'VERSION') {
		let pubKeyHash = req.body.pubKeyHash;
		let node = await FindNode(pubKeyHash);
		if (node) {
			node.host = req.body.host;
			node.save(err => {
				if (err) {
				} else {
					let index = globalState.nodes.findIndex(_node => {
						return _node.pubKeyHash === node.pubKeyHash;
					});
					if (index >= 0) {
						globalState.nodes[index] = node;
					}
					res.json(Crypto.Sign({
						header: 'VER_ACK'
					}));
				}
			});
		} else {
			res.end('Invalid node');
		}
	} else {
		res.end('Invalid header');
	}
});

module.exports = router;