/*
* =====================================
* coapObserveServer.js
* =====================================
*/
const coap    = require('coap')   
    , server  = coap.createServer()

server.on('request', function(req, res) {
  if (req.headers['Observe'] !== 0)
    return res.end(new Date().toISOString() + '\n')

  var interval = setInterval(function() {
    res.write(new Date().toISOString() + '\n')
  }, 2000)

  res.on('finish', function(err) {
    clearInterval(interval)
  })
})

server.listen(function() {
  console.log('server started')
})