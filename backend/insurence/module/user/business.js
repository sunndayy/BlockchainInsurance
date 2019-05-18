var User = require('./model');
var bcrypt = require('bcryptjs');
var jsonwebtoken = require('jsonwebtoken');
var validator = require('validator');
var moment = require('moment');

module.exports.signIn = async (username, password) => {
	var user = await User.findOne({username: username});
	if (user) {
		if (await bcrypt.compare(password, user.passwordHash)) {
			return await jsonwebtoken.sign({
				username: user.username
			}, 'secretkey');
		}
	}
	throw new Error('Username or password not correct');
};

module.exports.signUp = async (userInfo, isAdmin = false) => {
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
	userInfo.birthday = new Date(userInfo.birthday.year, userInfo.birthday.month, userInfo.birthday.day);
	
	await User.create(userInfo);
	return await jsonwebtoken.sign({
		username: userInfo.username
	}, 'secretkey');
};