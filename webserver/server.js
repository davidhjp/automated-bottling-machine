var express = require('express'),
	app = express()
var net = require('net')

app.use(express.static('public'))
// app.use('/pages', express.static(__dirname + '/views'))
app.use('/bootstrap', express.static(__dirname + '/node_modules/bootstrap'))
app.use('/jquery', express.static(__dirname + '/node_modules/jquery'))


app.get('/', function(req,res){
	res.sendFile('/pages/index.html')
})

var ROTARY_HOST = '192.168.1.103'
var TURN_PORT = 1010
var CAPPED_PORT = 1020
var client_turn = new net.Socket()
var capped_client = new net.Socket()

function connect_client(s,PORT,HOST) {
	s.connect(PORT, HOST, function() {
		console.log('Client connected at ' + HOST+":"+PORT)
	})
}

client_turn.on('error', function(m) {
	console.log(m.message)
	setTimeout(connect_client.bind(null,client_turn, TURN_PORT, ROTARY_HOST),1000)
})
client_turn.on('end', function(){
	console.log('Rotary socket terminated')
	setTimeout(connect_client.bind(null,client_turn, TURN_PORT, ROTARY_HOST),1000)
})

capped_client.on('error', function(m){
	console.log(m.message)
	setTimeout(connect_client.bind(null,capped_client, CAPPED_PORT, ROTARY_HOST), 1000)
})
capped_client.on('end', function(){
	console.log('Baxter socket terminated')
	setTimeout(connect_client.bind(null,capped_client, CAPPED_PORT, ROTARY_HOST), 1000)
})


connect_client(client_turn, TURN_PORT, ROTARY_HOST)
connect_client(capped_client, CAPPED_PORT, ROTARY_HOST)

app.post('/rotary', function(req,res){
	if(req.query.action == 'rotate') {
		client_turn.write('1\n')
		setTimeout(function(){
			client_turn.write('0\n')
			res.status(200).send()
		}, 100)
	} else if(req.query.action == 'capped') {
		capped_client.write('1\n')
		setTimeout(function(){
			capped_client.write('0\n')
			res.status(200).send()
		}, 100)
	}
})

app.listen(8080)

console.log('Server is running at http://127.0.0.1:8080')
