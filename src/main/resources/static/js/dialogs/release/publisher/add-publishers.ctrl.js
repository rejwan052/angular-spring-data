define([
	'angular',
	'controllers'
	], function(angular, controllers) {
    controllers.controller("addPublishersDialogCtrl", ["$scope", "$publisher", "$controller", "$security",
	    function ($scope, $publisher, $controller, $security) {
    	
        	angular.extend(this, $controller('dialogMultipleSelectController', {$scope: $scope}));	    	
	    	$scope.filterResource = $security.notPublishers;    	
	    	$scope.saveResource = $publisher.savePublishers;
	    	$scope.saveSuccessMessage = "messages.success.publishersAdded";  

	    }
	]);
});