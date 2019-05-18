const express = require('express');
const router = express.Router();

const {FindByCompany, FindByLicensePlate} = require('./model');
const verifyMiddleware = require('../../middleware/verify-middleware');

router.post('/get-contracts-by-company', verifyMiddleware, async (req, res) => {
	if (req.body.header === 'GET_CONTRACTS_BY_COMPANY') {
		let index = globalState.nodes.findIndex(node => {
			return node.pubKeyHash === req.body.pubKeyHash;
		});
		if (index >= 0 && new Date() - new Date(req.body.time) < DURATION) {
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
	if (req.body.header === 'GET_CONTRACTS_BY_LICENSE_PLATE') {
		let index = globalState.nodes.findIndex(node => {
			return node.pubKeyHash === req.body.pubKeyHash;
		});
		if (index >= 0 && new Date() - new Date(req.body.time) < DURATION) {
			let contracts = await FindByLicensePlate(req.body.company);
			// let contracts = await FindByLicensePlate(req.body.licensePlate);
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