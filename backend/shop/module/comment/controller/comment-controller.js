const commentBusiness = require('../business/comment-business');

class CommentController {
	constructor(req, res) {
		this.req = req;
		this.res = res;
	}
	
	async exec(business) {
		try {
			this.res.json(await business());
		} catch (e) {
			this.res.json({
				error: e.message
			});
		}
	}
	
	static async getAllComments() {
		return commentBusiness.getAllComments();
	}
	
	async postComment() {
		return commentBusiness.postComment(this.req.user, this.req.body.content);
	}
}

module.exports.getAllComments = async (req, res) => {
	let controller = new CommentController(req, res);
	await controller.exec(CommentController.getAllComments);
};

module.exports.postComment = async (req, res) => {
	let controller = new CommentController(req, res);
	await controller.exec(controller.postComment.bind(controller));
};