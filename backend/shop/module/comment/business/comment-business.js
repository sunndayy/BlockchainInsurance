const Comment = require('../model/comment-model');

module.exports.getAllComments = async () => {
	return await Comment.find({}).sort({id: -1}).lean();
};

module.exports.postComment = async (user, content) => {
	return await Comment.create({user, content});
};