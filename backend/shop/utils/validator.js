module.exports = (obj, expectedType, field) => {
	if (typeof obj !== expectedType) {
		throw new Error(`Invalid ${field} data type`);
	}
};