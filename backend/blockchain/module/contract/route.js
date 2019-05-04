const express = require('express');
const router = express.Router();

const { FindByCompany, FindByLicensePlate } = require('./model');
const verifyMiddleware = require('../../middleware/verify-middleware');

router.post('/get-contracts-by-company', verifyMiddleware, async (req, res) => {
    if (req.body.header === 'GETCONTRACTSBYCOMPANY') {
        if (nodes.indexOf(Crypto.Hash(req.body.pubKey)) >= 0) {
            let contracts = await FindByCompany(req.body.company);
            if (contracts.length > 0) {
                res.json(contracts);
            } else {
                res.end('No contract was found');
            }
        } else {
            res.end('Invalid node');
        }
    } else {
        res.end('Invalid header');
    }
});

router.post('/get-contracts-by-license-plate', verifyMiddleware, async (req, res) => {
    if (req.body.header === 'GETCONTRACTSBYLICENSEPLATE') {
        if (nodes.indexOf(Crypto.Hash(req.body.pubKey)) >= 0) {
            let contracts = await FindByLicensePlate(req.body.company);
            if (contracts.length > 0) {
                res.json(contracts);
            } else {
                res.end('No contract was found');
            }
        } else {
            res.end('Invalid node');
        }
    } else {
        res.end('Invalid header');
    }
});

module.exports = router;