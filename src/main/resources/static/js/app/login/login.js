define([
	'angular',
	'controllers'
], function(angular, controllers) {
    controllers.controller("loginCtrl", ['$rootScope', '$scope', '$securityService',
        function($rootScope, $scope, $securityService) {
			$scope.error = false;
		
			$scope.credentials = {};
		
			$scope.login = function() {
				$securityService.login($scope.credentials, function(success) { 
					$scope.error = !success;
 			        $rootScope.authorizeMainMenu();
				});
			};   	
	}]);
});