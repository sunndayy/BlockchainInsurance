const createError = require('http-errors');
const express = require('express');
const path = require('path');
const cookieParser = require('cookie-parser');
const logger = require('morgan');
const bodyParser = require('body-parser');

const app = express();

const mongoose = require('mongoose');
mongoose.connect('mongodb://phhoang:phhoang1512180@ds155916.mlab.com:55916/shop', {useNewUrlParser: true});
const db = mongoose.connection;
db.on('error', err => {
	console.error(err);
});

db.once('open', async () => {
	try {
		try {
			const userBusiness = require('./module/user/business/user-business');
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
		
		app.use(bodyParser.json());
		app.use(bodyParser.urlencoded({extended: true}));
		app.use(logger('dev'));
		app.use(express.json());
		app.use(express.urlencoded({extended: false}));
		app.use(cookieParser());
		
		app.use('/', require('./module/user/route/user-route'));
		app.use('/', require('./module/product/route/product-route'));
		app.use('/', require('./module/order/route/order-route'));

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
			res.end();
		});
	} catch (e) {
		console.error(e);
	}
});

module.exports = app;