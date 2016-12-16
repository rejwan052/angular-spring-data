define([
	'angular',
	'controllers',
], function(angular, controllers) {
    controllers.controller("addReleaseCategoryDialogCtrl", ["$scope", "$formatter", "$category", "ngTableParams",
	    function ($scope, $formatter, $category, ngTableParams) {

	    	$scope.obj = {};

	    	$scope.filter = {
	    		searchQuery: "",
	    		searchFields: "name"
	    	};

	    	$scope.tableParams = new ngTableParams({
                page: 1,
                count: 10
            }, {
                total: 0,
                getData: function($defer, params) {
                	var parameters = $formatter.resourceUrl(params.url(), $scope.filter);
    				$category.categories(parameters, function(data){
    					params.total(data.totalRecords);
			    		$defer.resolve(data.records);
    				});
                }
            });

            $scope.categoryToPublisher = function(obj){
            	$scope.wizard.category = obj;
            	$scope.wizard.step = "publisher";
            };

            if ($scope.wizard.category.id) {
                $scope.obj = $scope.wizard.category;
                $scope.wizard.category = {};
            };

	    }
	]);
});