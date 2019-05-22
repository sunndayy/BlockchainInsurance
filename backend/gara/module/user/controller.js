const userBusiness = require('./business');

class UserController {
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
	
	async signIn() {
		return await userBusiness.signIn(this.req.body.username, this.req.body.password);
	}
}

module.exports.signIn = async (req, res) => {
	let controller = new UserController(req, res);
	await controller.exec(controller.signIn.bind(controller));
};