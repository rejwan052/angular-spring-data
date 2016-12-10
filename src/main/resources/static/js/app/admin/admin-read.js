define([
	'angular',
	'controllers'
], function(angular, controllers) {
    controllers.controller("adminReadCtrl", ['$scope', 'greeting',
        function($scope, greeting) {	
    		$scope.greeting = greeting;
	}]);
});