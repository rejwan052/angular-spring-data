define([
	'angular',
	'controllers',
	'translate',
	'formatterService',
	'messageService'
], function(angular, controllers) {
    controllers.controller("categoryListCtrl", ["$scope", "$stateFilter", "$translate", "$skgGrid", 
                           "$category", "ngTableParams", "$formatter", "$confirm", "$message", 
                           "$securitySession", "$q",
	    function ($scope, $stateFilter, $translate, $skgGrid, $category, ngTableParams, 
	    		$formatter, $confirm, $message, $securitySession, $q) {
    	
        	$scope.adminWriter = $securitySession.permissions.adminWriter();

            $scope.filter = $stateFilter.getFilter({
	    		searchQuery: ""
	    	});

	    	var renderGrid = function(){
		    		$scope.tableParams = $skgGrid({
		                page: 1,
		                count: 10,
		                sorting: {
		                    name: "asc"
		                }
		            }, {
		                total: 0,
		                getData: function($defer, params) {
		                	var parameters = $formatter.resourceUrl(params.url(), $scope.filter);
		                	categoriesFilter(parameters, params, $defer);
		                }
		            });
	    	};

	    	var categoriesFilter = function(parameters, params, defer) {
				$category.categories(parameters, function(data) {
			    	params.total(data.totalRecords);
	    			defer.resolve(data.results);
				});
	    	};

            $scope.deleteCategory = function(item){
                $translate([
                    'confirms.deleteCategory',
                    'messages.success.itemDeleted'
                ], {
                    name: item.name,
                }).then(function (str) {
                    $confirm(str['confirms.deleteCategory'], function(){
                        $category.deleteCategory({
                            id: item.id
                        }, function(){
                            $message("success", str["messages.success.itemDeleted"]);
              				$scope.tableParams.reload();
                        }, function (error) {
                            $message("error", $formatter.error(error.data));
                        });
                    });
                });
            };

            $translate(
                [
                    'table.headers.name'
                ]
            ).then(function (data) {
                $scope.tableHeaders = {
                    name: data["table.headers.name"]
                };
	    		renderGrid();
            });
            
  			$scope.$on('refreshTab', function() {
  				$scope.tableParams.reload();
    		});            

	    }
	]);
});