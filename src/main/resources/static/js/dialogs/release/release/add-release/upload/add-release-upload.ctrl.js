define([
	'angular',
	'controllers'    
], function(angular, controllers) {
    controllers.controller("addReleaseUploadDialogCtrl", ["$scope", "$formatter", "ngTableParams", "$q",
	    function ($scope, $formatter, ngTableParams, $q) {

            $scope.obj = {
                file: ''
            };

            $scope.uploadToPublisher = function(){
                $scope.wizard.step = "publisher";
            };

	    }
	]);
});