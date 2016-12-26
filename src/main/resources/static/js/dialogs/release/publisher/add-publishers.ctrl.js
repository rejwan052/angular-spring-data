define([
	'angular',
	'controllers'
	], function(angular, controllers) {
    controllers.controller("addPublishersDialogCtrl", ["$scope", "$publisher", "$controller",
	    function ($scope, $publisher, $controller) {
    	
        	angular.extend(this, $controller('dialogMultipleSelectController', {$scope: $scope}));	    	
	    	$scope.filterResource = $publisher.notPublishers;    	
	    	$scope.saveResource = $publisher.savePublishers;
	    	$scope.saveSuccessMessage = "messages.success.publishersAdded";  

	    }
	]);
});