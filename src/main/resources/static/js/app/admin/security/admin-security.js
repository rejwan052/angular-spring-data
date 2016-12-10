define([	
	'angular',
	'controllers',
	'skgSecurity',
	
	'adminSecurityUsersCtrl',
	'adminSecurityRolesCtrl',
	'adminSecurityTokensCtrl',

    'securityUserDialogCtrl',
    'resetUserPasswordDialogCtrl',
    'userRolesDialogCtrl',
    'securityRoleDialogCtrl'
    
], function(angular, controllers) {
    controllers.controller("adminSecurityCtrl", ['$scope', '$state', '$securityMenuConfig',
	    function ($scope, $state, $securityMenuConfig) {
    	  $scope.viewList = $securityMenuConfig['security.menu'].tabs;
    	  $scope.viewType = $securityMenuConfig.defaultAdminSecurityState();
    	  $scope.$on('$stateChangeSuccess', function() {
    		  if (isNotParentState($state.current.name)) {
        		  $scope.viewType = $state.current.name;    			  
    		  }
    	  });
    	  $scope.$watch('viewType', function() {
    		  if ($scope.viewType) {
        		  $state.go($scope.viewType);    			  
    		  } 
    	  });
    	  
    	  var isNotParentState = function(value) {
    		  return value != 'skyglass.admin.security';
    	  }
	    }
	]);
});