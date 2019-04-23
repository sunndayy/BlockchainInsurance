const express = require('express');
const router = express.Router();
const verifyMiddleware = require('../../middleware/verify-middleware');

router.get('/plans-by-company/:company', (req, res) => {

});

router.get('/plan-by-company-and-id/:company/:id', (req, res) => {

});

router.get('/get-contracts-by-company/:company', (req, res) => {

});

router.get('/get-contract-by-license-plate/:license-plate', (req, res) => {

});

module.exports = router;