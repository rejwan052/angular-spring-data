define([
	'angular',
	'controllers'
], function(angular, controllers) {
    controllers.controller("permissionsCtrl", ['$scope', '$state', '$securitySession', '$securityConfig',
        function($scope, $state, $securitySession, $securityConfig) {
    		if (!$securitySession.permissions.authenticated()) {
    			$state.go($securityConfig.config.loginState);
    		}
	}]);
});