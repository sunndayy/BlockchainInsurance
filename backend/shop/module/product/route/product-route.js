const express = require('express');
const router = express.Router();
const userMiddleware = require('../../../middleware/user-middleware');

router.get('/products', async (req, res) => {

});

router.get('/products-by-types/:type', async (req, res) => {

});

router.get('/products-by-producer/:producer', async (req, res) => {

});

router.post('/create-product', userMiddleware.authMiddleware, async (req, res) => {

});

router.put('/update-product', async (req, res) => {

});

module.exports = router;