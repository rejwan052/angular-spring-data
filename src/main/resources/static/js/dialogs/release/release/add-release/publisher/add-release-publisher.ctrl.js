define([
	'angular',
	'controllers'
], function(angular, controllers) {
    controllers.controller("addReleasePublisherDialogCtrl", ["$scope", "$formatter", "$release", "ngTableParams", "$translate",
	    function ($scope, $formatter, $release, ngTableParams, $translate) {

	    	$scope.obj = {};

	    	$scope.filter = {
	    		searchQuery: "",
	    		searchFields: "name",
                id: $scope.wizard.category.id
	    	};

	    	$scope.tableParams = new ngTableParams({
                page: 1,
                count: 10
            }, {
                total: 0,
                getData: function($defer, params) {
                    var parameters = $formatter.resourceUrl(params.url(), $scope.filter);
                	$release.publishers(parameters, function(data){
                        params.total(data.totalRecords);
                        $defer.resolve(data.records);
                    });
                }
            });

            $scope.publisherToUpload = function(obj){
            	$scope.wizard.publisher = obj;
            	$scope.wizard.step = "upload";
            };

            $scope.publisherToCategory = function(obj){
                $scope.wizard.step = "category";
            };

            if ($scope.wizard.publisher.id) {
                $scope.obj = $scope.wizard.publisher;
                $scope.wizard.publisher = {};
            }

            $translate([
                "table.headers.name"
            ]).then(function (str) {
                $scope.tableHeaders = {
                    name: str["table.headers.name"]
                };
            });

	    }
	]);
});