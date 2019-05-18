const UserBusiness = require('./business');

class UserController {
	constructor(req, res) {
		this.req = req;
		this.res = res;
	}
	
	async signIn() {
		let token = await UserBusiness.signIn(this.req.body.username, this.req.body.password);
		this.res.json({
			token
		});
	}
	
	async signUp() {
		let token = await UserBusiness.signUp(this.req.body.userInfo);
		this.res.json({
			token
		});
	}
}

module.exports.signIn = async (req, res) => {
	try {
		let controller = new UserController(req, res);
		await controller.signIn();
	} catch (e) {
		res.json({
			error: e.message
		});
	}
};

module.exports.signUp = async (req, res) => {
	try {
		let controller = new UserController(req, res);
		await controller.signUp();
	} catch (e) {
		res.json({
			error: e.message
		})
	}
};