define([
	'angular',
	'controllers'
], function(angular, controllers) {
    controllers.controller("adminChangesCtrl", ['$scope', '$http',
        function($scope, $http) {
			$http.get('/resource/changes').success(function(data) {
				$scope.data = data;
			})
    }]);
});