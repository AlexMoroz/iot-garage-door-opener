
/**
 * Module dependencies
 */

var express = require('express'),
    bodyParser = require('body-parser'),
    morgan = require('morgan'),
    http = require('http'),
    path = require('path');

var app = module.exports = express();


/**
 * Configuration
 */

// all environments
app.set('port', process.env.PORT || 8080);
app.use(morgan('dev'));
app.use(bodyParser());
app.use(express.static(path.join(__dirname, 'app')));


/**
 * Routes
 */

// serve index and view partials
app.get('/', function(req, res){
    res.sendfile('app/index.html');
});

var fs = require('fs');

// JSON API
app.get('/api/status', function(req, res){
    res.sendfile('data/data.json');
});

app.post('/api/toggle', function(req, res){
    var file = fs.readFileSync('data/data.json');
    var data = JSON.parse(file);
    data.open = !data.open;
    fs.writeFileSync('data/data.json', JSON.stringify(data));
    res.sendfile('data/data.json');
});

// redirect all others to the index (HTML5 history)
app.get('*', function(req, res){
    res.sendfile('app/index.html');
});


/**
 * Start Server
 */

http.createServer(app).listen(app.get('port'), function () {
    console.log('Express server listening on port ' + app.get('port'));
});