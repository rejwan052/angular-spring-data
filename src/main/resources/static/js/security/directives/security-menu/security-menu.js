define(['angular'], function(angular) {
    return ['$securityService', '$securityMenuConfig', function($securityService, $securityMenuConfig) {

    	return {
            restrict: 'E',
            templateUrl: function(elem, attrs) {
                return "js/security/directives/security-menu/security-menu.html";
            },
	        link: function(scope, el, attrs) {        	
	        	scope.menuTabs = $securityService.authorizeMenu($securityMenuConfig[attrs.name]);
	        }
	    };
    }];
});