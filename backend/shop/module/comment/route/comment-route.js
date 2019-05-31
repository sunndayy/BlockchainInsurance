const express = require('express');
const router = express.Router();
const userMiddleware = require('../../../middleware/user-middleware');
const commentController = require('../controller/comment-controller');

router.get('/comments', commentController.getAllComments);

router.post('/comment', userMiddleware.authMiddleware, commentController.postComment);

module.exports = router;