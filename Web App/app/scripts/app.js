'use strict';

angular
    .module('doorOpenerApp', [
        'ngRoute',
        'frapontillo.bootstrap-switch'
    ])
    .config(function ($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'views/main.html',
                controller: 'MainCtrl',
                controllerAs: 'main'
            })
            .otherwise({
                redirectTo: '/'
            });
    });
