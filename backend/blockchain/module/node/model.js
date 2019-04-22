const mongoose = require('mongoose');
const Schema = mongoose.Schema;
const { dbPrefix } = require('../../config');

const Model = new Schema({
  pubKeyHash                        : { type: String, require: true },
  company                           : { type: String },
  url                               : { type: String },
  point                             : { type: Number },
  lastTimeCreateBlock               : { type: Number },
  lastTimeUpdateUrl                 : { type: Date }
});

module.exports.GetNodesOnTop = function () {

};

module.exports.CalAge = function (time = new Date()) {

};

module.exports.CalTimeMustWait = function (pubKeyhash) {

};

module.exports = Model;