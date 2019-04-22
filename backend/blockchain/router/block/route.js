const express = require('express');
const router = express.Router();

router.get('/header/:type/:key', (req, res) => {
  // Search block by key (index/hash)
  // Reply with blockHeader
});

router.post('/header', (req, res) => {
  // Check if blockHeader is valid
  // Add blockHeader to a tmpArray
  // Send getBlock http request
});

router.get('/data/:type/:key', (req, res) => {
  // Search block by key (index/hash)
  // Reply with blockData
});

router.post('/data', (req, res) => {
  // Check if blockData is valid
  // Combine blockHeader with blockData
  // Add new block to blockChain
  // Send get next blockHeader http request
});

router.post('/agree', (req, res) => {
  // Check if preBlockHash is same to me
  // Reply with agree message
});

module.exports = router;