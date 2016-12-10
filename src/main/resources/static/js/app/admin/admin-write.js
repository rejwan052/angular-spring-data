define([
	'angular',
	'controllers'
], function(angular, controllers) {
    controllers.controller("adminWriteCtrl", ['$scope', 'greeting', '$http',
        function($scope, greeting, $http) {
    		$scope.greeting = greeting;	
			$scope.update = function() {
				$http.post('/resource/', {content: $scope.greeting.content}).success(function(data) {
					$scope.greeting = data;
				})
			}
	}]);
});