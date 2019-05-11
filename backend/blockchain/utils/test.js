const Crypto = require('./crypto');
let sign = Crypto.Sign({
	header: 'Alo'
});
sign = JSON.stringify(sign);
sign = JSON.parse(sign);