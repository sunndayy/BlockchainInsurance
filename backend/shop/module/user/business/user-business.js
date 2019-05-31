const User = require('../model/user-model');
const bcrypt = require('bcryptjs');
const jsonwebtoken = require('jsonwebtoken');
const validator = require('validator');
const moment = require('moment');
const myValidator = require('../../../utils/validator');
const fs = require('fs');

module.exports.signIn = async (username, password) => {
	myValidator(username, 'string', 'username');
	myValidator(password, 'string', 'password');
	
	let user = await User.findOne({username: username}).lean();
	if (user) {
		if (await bcrypt.compare(password, user.passwordHash)) {
			let token = await jsonwebtoken.sign({
				username: user.username
			}, 'secretkey');
			return {token};
		}
	}
	
	throw new Error('Username or password not correct');
};

module.exports.signUp = async (userInfo, isAdmin = false) => {
	myValidator(userInfo.username, 'string', 'username');
	myValidator(userInfo.password, 'string', 'password');
	myValidator(userInfo.name, 'string', 'name');
	myValidator(userInfo.identityCard, 'string', 'identityCard');
	myValidator(userInfo.birthday, 'object', 'birthday');
	myValidator(userInfo.phoneNumber, 'string', 'phone number');
	myValidator(userInfo.address, 'string', 'address');
	myValidator(userInfo.email, 'string', 'email');
	
	if (userInfo.username === '') {
		throw new Error('Empty username');
	}
	if (userInfo.password.length < 6) {
		throw new Error('Short password');
	}
	if (userInfo.name.length === 0) {
		throw new Error('Empty name');
	}
	if (!/^[0-9]{9}/.test(userInfo.identityCard)) {
		throw new Error('Invalid identity card');
	}
	if (!moment(userInfo.birthday.year + '-' + userInfo.birthday.month + '-' + userInfo.birthday.day, 'YYYY-MM-DD').isValid()) {
		throw new Error('Invalid birthday');
	}
	if (userInfo.address === '') {
		throw new Error('Empty address');
	}
	if (!/^[0-9]{10,11}/.test(userInfo.phoneNumber)) {
		throw new Error('Invalid phone number');
	}
	if (!validator.isEmail(userInfo.email)) {
		throw new Error('Invalid email');
	}
	if (await User.findOne({username: userInfo.username})) {
		throw new Error('username was used');
	}
	if (await User.findOne({email: userInfo.email})) {
		throw new Error('Email was used');
	}
	
	userInfo.role = isAdmin ? 0 : 1;
	userInfo.passwordHash = await bcrypt.hash(userInfo.password, 10);
	
	await User.create(userInfo);
	let token = await jsonwebtoken.sign({
		username: userInfo.username
	}, 'secretkey');
	return {token};
};

module.exports.update = async (userInfo, avatarFile, username) => {
	Object.keys(userInfo).forEach(key => {
		switch (key) {
			case 'name':
				if (userInfo.name.length === 0) {
					throw new Error('Empty name');
				}
				break;
			case 'identityCard':
				if (!/^[0-9]{9}/.test(userInfo.identityCard)) {
					throw new Error('Invalid identity card');
				}
				break;
			case 'birthday':
				if (!moment(userInfo.birthday.year + '-' + userInfo.birthday.month + '-' + userInfo.birthday.day, 'YYYY-MM-DD').isValid()) {
					throw new Error('Invalid birthday');
				}
				break;
			case 'address':
				if (userInfo.address === '') {
					throw new Error('Empty address');
				}
				break;
			case 'phoneNumber':
				if (!/^[0-9]{10,11}/.test(userInfo.phoneNumber)) {
					throw new Error('Invalid phone number');
				}
				break;
			case 'email':
				if (!validator.isEmail(userInfo.email)) {
					throw new Error('Invalid email');
				}
				break;
		}
	});
	
	delete userInfo.username;
	delete userInfo.password;
	
	return new Promise((resolve, reject) => {
		if (!avatarFile) {
			avatarFile = {
				path: ""
			};
		}
		fs.readFile(avatarFile.path, (e, body) => {
			if (e) {
				console.error(e);
			} else {
				userInfo.avatar = body;
			}
			User.findOneAndUpdate({username}, {$set: userInfo}, {new: true, fields: '-avatar -passwordHash'}, (e, doc) => {
				if (e) {
					reject(e);
				} else {
					resolve(doc);
				}
			});
		});
	});
};

module.exports.getAvatar = async username => {
	let user = await User.findOne({username}, 'avatar');
	return user.avatar;
};