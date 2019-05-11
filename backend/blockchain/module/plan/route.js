const express = require('express');
const router = express.Router();

const {FindByCompany, FindByCompanyAndId} = require('./model');
const verifyMiddleWare = require('../../middleware/verify-middleware');

router.post('/get-plans-by-company', verifyMiddleWare, async (req, res) => {
	if (req.body.header === 'GET_PLANS_BY_COMPANY') {
		let index = globalState.nodes.findIndex(node => {
			return node.pubKeyHash === req.body.pubKeyHash;
		});
		if (index >= 0 && new Date() - new Date(req.body.time) < DURATION) {
			let plans = await FindByCompany(req.body.company);
			if (plans.length > 0) {
				res.json(plans);
			} else {
				res.end('No plan was found');
			}
		} else {
			res.end('Invalid node');
		}
	} else {
		res.end('Invalid header');
	}
});

router.post('/get-plan-by-company-and-id', verifyMiddleWare, async (req, res) => {
	if (req.body.header === 'GET_PLAN_BY_COMPANY_AND_ID') {
		let index = globalState.nodes.findIndex(node => {
			return node.pubKeyHash === req.body.pubKeyHash;
		});
		if (index >= 0 && new Date() - new Date(req.body.time) < DURATION) {
			let plan = await FindByCompanyAndId(req.body.company, req.body.id);
			if (plan) {
				res.json(plan);
			} else {
				res.end('No plan was found');
			}
		} else {
			res.end('Invalid node');
		}
	} else {
		res.end('Invalid header');
	}
});

module.exports = router;