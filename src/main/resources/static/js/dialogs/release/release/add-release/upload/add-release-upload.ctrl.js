define([
	'angular',
	'controllers'    
], function(angular, controllers) {
    controllers.controller("addReleaseUploadDialogCtrl", ["$scope", "$formatter", "ngTableParams", "$q", "$release",
	    function ($scope, $formatter, ngTableParams, $q, $release) {

            $scope.obj = {
                file: ''
            };

            $scope.uploadToPublisher = function(){
                $scope.wizard.step = "publisher";
            };

	    }
	]);
});