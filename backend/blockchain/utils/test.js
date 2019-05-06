const Crypto = require('./crypto');
let sign = Crypto.Sign({
	header: 'Alo'
});
sign = JSON.stringify(sign);
console.log(sign);
sign = JSON.parse(sign);
console.log(Crypto.Verify(sign));