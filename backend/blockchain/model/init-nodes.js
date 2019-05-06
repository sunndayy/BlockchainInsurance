require('./model');
const mongoose = require('mongoose');

let Node = mongoose.model('node');

module.exports = async () => {
	const initNodePromise = (pubKeyHash, company) => {
		let node = new Node({
			pubKeyHash,
			company
		});
		return new Promise((resolve, reject) => {
			node.save(err => {
				resolve();
			});
		});
	};
	
	let promises = [];
	promises.push(initNodePromise('1e095aff6eef007cb07577f0646e31b3756e6fe8d505462b477cdd273bc2243a', 'phhoang'));
	promises.push(initNodePromise('2dedf231bb53757027f475dc6a37259348004875cc9882df46b8e1ce3a36c773', 'pvduong'));
	
	await Promise.all(promises);
};