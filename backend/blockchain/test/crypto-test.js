const crypto = require('../utils/crypto');
let msg = JSON.stringify( {
  name: 'Pham Huy Hoang'
});
console.log(JSON.stringify(crypto.Sign('dffddfe476931ba592ae94da5c625689cdad0f60c5ccc4dbcdfc03d522b291c8', msg)));