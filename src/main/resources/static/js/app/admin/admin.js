define([
	'angular',
	'controllers'
], function(angular, controllers) {
    controllers.controller("adminCtrl", ['$scope', '$http', '$securitySession', '$state',
        function($scope, $http, $securitySession, $state) {	
			$scope.authenticated = $securitySession.permissions.authenticated();			
			$scope.user = $securitySession.user();			
	}]);
});
