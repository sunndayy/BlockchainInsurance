const express = require('express');
const router = express.Router();
const verifyMiddleware = require('../../middleware/verify-middleware');

router.get('/addrs', (req, res) => {
  // Reply with list of addresses
});

router.post('/version', verifyMiddleware, verifyMiddleware, (req, res) => {
  // let { header, url, signature } = req.body;
  // if (header == 'version') {
  //   // Verify signature
  //   // Check if peer is in node-dictionary
  //   // If not, add peer to node-dictionary
  //   // Reply with verack message
  //   // Send version http request
  // }
});

module.exports = router;