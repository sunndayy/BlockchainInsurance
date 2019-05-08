const express = require('express');
const router = express.Router();

const FindNode = require('./model').FindNode;

const verifyMiddleware = require('../../middleware/verify-middleware');
const Crypto = require('../../utils/crypto');

router.post('/version', verifyMiddleware, async (req, res) => {
    // if (req.body.header === 'VERSION') {
    //     let pubKeyHash = req.body.pubKeyHash;
    //     if (nodes.indexOf(pubKeyHash) >= 0) {
    //         let node = await FindNode(pubKeyHash);
    //         if (node) {
    //         	let add = true;
    //         	if (node.lastTimeUpdateHost) {
	// 	            let lastTime = node.lastTimeUpdateHost;
	// 	            let newTime = new Date(req.body.time);
	// 	            if (newTime <= lastTime) {
	// 	            	add = false;
	// 	            }
	//             }
    //         	if (add) {
	// 	            res.json(Crypto.Sign({
	// 		            header: 'VER_ACK'
	// 	            }));
	// 	            node.host = req.body.host;
	// 	            if (!node.lastTimeUpdateHost) {
	// 		            node.lastTimeUpdateHost = new Date();
	// 	            }
	// 	            await node.save();
	//             }
    //         }
    //     } else {
	//         res.end('Invalid node');
    //     }
    // } else {
    //     res.end('Invalid header');
    // }
});

module.exports = router;