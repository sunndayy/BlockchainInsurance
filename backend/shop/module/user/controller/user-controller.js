const userBusiness = require('../business/user-business');

class UserController {
	constructor(req, res) {
		this.req = req;
		this.res = res;
	}
	
	async exec(business) {
		try {
			let result = await business();
			if (Buffer.isBuffer(result)) {
				this.res.set({'Content-Type': 'image/gif'});
				this.res.end(result);
			} else {
				this.res.json(result);
			}
		} catch (e) {
			console.error(e);
			this.res.json({
				error: e.message
			});
		}
	}
	
	async signIn() {
		return await userBusiness.signIn(this.req.body.username, this.req.body.password);
	}
	
	async signUp() {
		return await userBusiness.signUp(this.req.body);
	}
	
	async update() {
		return await userBusiness.update(this.req.body, this.req.file, this.req.user.username);
	}
	
	async getAvatar() {
		return await userBusiness.getAvatar(this.req.params.username);
	}
}

module.exports.signIn = async (req, res) => {
	let controller = new UserController(req, res);
	await controller.exec(controller.signIn.bind(controller));
};

module.exports.signUp = async (req, res) => {
	let controller = new UserController(req, res);
	await controller.exec(controller.signUp.bind(controller));
};

module.exports.getUserInfo = async (req, res) => {
	res.json(req.user);
};

module.exports.update = async (req, res) => {
	let controller = new UserController(req, res);
	await controller.exec(controller.update.bind(controller));
};

module.exports.getAvatar = async (req, res) => {
	let controller = new UserController(req, res);
	await controller.exec(controller.getAvatar.bind(controller));
};