const express = require('express');
const router = express.Router();
const userMiddleware = require('../../../middleware/user-middleware');
const userController = require('../controller/user-controller');
const multer = require('multer');
const upload = multer({dest: 'uploads/'});

router.post('/sign-up', userController.signUp);

router.post('/sign-in', userController.signIn);

router.route('/user-info')
	.get(userMiddleware.authMiddleware, userController.getUserInfo)
	.put(userMiddleware.authMiddleware, upload.single('avatar'), userController.update);

router.route('/user-avatar')
	.get(userMiddleware.authMiddleware, userController.getAvatar);

module.exports = router;