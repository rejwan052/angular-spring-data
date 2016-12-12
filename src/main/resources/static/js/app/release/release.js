define([
	'angular',
	'controllers',
	'skgSecurity',
	
	'categoryListCtrl',
	'categoryReleasesCtrl',
	'categorySubscriptionsCtrl',	
	'publisherListCtrl',
	'publisherReleasesCtrl',
	'releasesCtrl',
	'subscriberListCtrl',
	'subscriberSubscriptionsCtrl',
	'subscriptionsCtrl',
	
	'saveCategoryDialogCtrl',
	'savePublisherDialogCtrl',
	'saveSubscriberDialogCtrl',
	'addReleaseDialogCtrl',
	'addSubscriptionDialogCtrl'
	
	
], function(angular, controllers) {
    controllers.controller("releaseCtrl", ['$scope', '$http', '$securitySession', '$state',
        function($scope, $http, $securitySession, $state) {	
			$scope.authenticated = $securitySession.permissions.authenticated();			
			$scope.user = $securitySession.user();			
	}]);
});
