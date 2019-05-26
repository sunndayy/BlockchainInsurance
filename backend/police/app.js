const createError = require('http-errors');
const express = require('express');
const path = require('path');
const cookieParser = require('cookie-parser');
const logger = require('morgan');
const mongoose = require('mongoose');
const userBusiness = require('./module/user/business');
const config = require('./config');
const bodyParser = require('body-parser');

mongoose.connect(config.db, {useNewUrlParser: true});
const db = mongoose.connection;
db.on('error', e => {
	console.error(e);
});

db.once('open', async () => {
	try {
		await userBusiness.signUp({
			'username': 'phhoang',
			'password': 'phhoang',
			'identityCard': '352421937',
			'name': 'Pham Huy Hoang',
			'birthday': {
				'day': 21,
				'month': 9,
				'year': 1997
			},
			'address': 'AG',
			'phoneNumber': '0123456789',
			'email': 'phamhuyhoang2109@gmail.com'
		}, true);
	} catch (e) {
		console.error(e);
	}
});

const app = express();

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));
app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({extended: false}));
app.use(cookieParser());

app.use('/', require('./module/user/route'));
app.use('/', require('./module/order/route'));

// catch 404 and forward to error handler
app.use(function (req, res, next) {
	next(createError(404));
});

// error handler
app.use(function (err, req, res, next) {
	// set locals, only providing error in development
	res.locals.message = err.message;
	res.locals.error = req.app.get('env') === 'development' ? err : {};
	
	// render the error page
	res.status(err.status || 500);
	res.json({
		error: err.message
	});
});

module.exports = app;