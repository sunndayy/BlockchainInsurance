var createError = require('http-errors');
var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');
var bodyParser = require('body-parser');

var app = express();

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

var hosts = [];

app.get('/', function (req, res) {
  res.json(hosts);
});

app.post('/', function (req, res) {
  if (hosts.indexOf(req.body.host) < 0) {
    hosts.push(req.body.host);
    setTimeout(() => {
      hosts.splice(hosts.indexOf(req.body.host), 1);
    }, 1000 * 60 * 10);
    res.end('Success');
  } else {
    res.end('Host was existed');
  }
});

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.end('Undefined error');
});

module.exports = app;
