const express = require('express');
const router = express.Router();
const verifyMiddleware = require('../../middleware/verify-middleware');

router.post('/tx', verifyMiddleware, (req, res) => {
  // Add tx to txPool
});

module.exports = router;