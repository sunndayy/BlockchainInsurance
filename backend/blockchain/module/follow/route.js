const express = require('express');
const router = express.Router();
const verifyMiddleware = require('../../middleware/verify-middleware');

router.post('/follow', verifyMiddleware, (req, res) => {
  // Add peer to follow dictionary
});

module.exports = router;