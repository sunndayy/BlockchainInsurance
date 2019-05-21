const express = require('express');
const router = express.Router();
const userMiddleware = require('../../../middleware/user-middleware');
const userController = require('../controller/user-controller');

router.post('/sign-up', userController.signUp);

router.post('/sign-in', userController.signIn);

router.get('/user-info', userMiddleware.authMiddleware, userController.getUserInfo);

module.exports = router;