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

var BAXTER_HOST = '192.168.1.124'
var BAXTER_PORT = 1010
var ROTARY_HOST = '192.168.1.103'
var ROTARY_PORT = 1010
var client = new net.Socket()
var baxter_socket = new net.Socket()

function connect_client(s,PORT,HOST) {
	s.connect(PORT, HOST, function() {
		console.log('Client connected at ' + HOST+":"+PORT)
	})
}

connect_client(client, ROTARY_PORT, ROTARY_HOST)
connect_client(baxter_socket, BAXTER_PORT, BAXTER_HOST)

client.on('error', function(m) {
	console.log(m.message)
	setTimeout(connect_client.bind(null,client, ROTARY_PORT, ROTARY_HOST),1000)
})
client.on('end', function(){
	console.log('Rotary socket terminated')
	setTimeout(connect_client.bind(null,client, ROTARY_PORT, ROTARY_HOST),1000)
})

baxter_socket.on('error', function(m){
	console.log(m.message)
	setTimeout(connect_client.bind(null,baxter_socket, BAXTER_PORT, BAXTER_HOST), 1000)
})
baxter_socket.on('end', function(){
	console.log('Baxter socket terminated')
	setTimeout(connect_client.bind(null,baxter_socket, BAXTER_PORT, BAXTER_HOST), 1000)
})

app.post('/rotary', function(req,res){
	if(req.query.action == 'rotate') {
		client.write('1\n')
		setTimeout(function(){
			client.write('0\n')
			res.status(200).send()
		}, 100)
	}
})

app.post('/baxter', function(req,res){
	if(req.query.action == 'capped') {
		baxter_socket.write('1\n')
		setTimeout(function(){
			baxter_socket.write('0\n')
			res.status(200).send()
		}, 100)
	}
})

app.listen(8080)

console.log('Server is running at http://127.0.0.1:8080')
