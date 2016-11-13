'use strict';

/**
 * @ngdoc function
 * @name doorOpenerApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the doorOpenerApp
 */
angular.module('doorOpenerApp')
    .controller('MainCtrl', function ($scope, $http, $interval, $timeout) {
        function getStatus() {
            $http.get('/api/status').then(function (response) {
                $scope.isSelected = response.data.open;
                $scope.alarm = response.data.alarm;
                if($scope.alarm) {
                    $timeout(function () {
                        $scope.alarm = false;
                    }, 3000);
                }
            });
        }

        getStatus();
        $interval(getStatus, 2000);

        $scope.changeStatus = function(status) {
            console.log(status);
            if($scope.isSelected == undefined) {
                return;
            }
            $http.post('/api/toggle', null).then(function(response) {
                if(response.data.open == status) {
                    $scope.isSelected = response.data.open;
                } else {
                    $scope.isSelected = undefined;
                }
            });
        };
    });
