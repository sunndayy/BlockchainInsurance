const express = require('express');
const router = express.Router();
const verifyMiddleware = require('../../middleware/verify-middleware');

router.get('/plans-by-company/:company', (req, res) => {

});

router.get('/plan-by-company-and-id/:company/:id', (req, res) => {

});

module.exports = router;