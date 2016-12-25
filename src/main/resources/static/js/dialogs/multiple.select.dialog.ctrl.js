define([
	'angular',
	'controllers'
	], function(angular, controllers) {
    controllers.controller("dialogMultipleSelectController", ["$scope", "$stateFilter", "$translate", "$skgGrid", 
                           "ngTableParams", "$formatter", "$message", "$q",
	    function ($scope, $stateFilter, $translate, $skgGrid, ngTableParams, 
	    		$formatter, $message, $q) {   
    	
    		$scope.formModel = angular.copy($scope.ngDialogData.model);
   	
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
	                	selectFilter(parameters, params, $defer);
	                }
	            });
	    	};
	    	
	    	var selectFilter = function(parameters, params, defer){
				$scope.filterResource(parameters, function(data) {
			    	params.total(data.totalRecords);
	    			defer.resolve(data.results);
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
            
	        $scope.isItemInArray = function(model, item) { 		        	
	     		return $.inArray(item.id, model.ids) == -1 ? false : true;
	        };
	         
	        $scope.pushOrRemoveIfExists = function(model, item) { 
	    		if ($scope.isItemInArray(model, item)) {
	    			model.ids = $.grep(model.ids, function(value) {
        				return value != item.id;
	    			});
        		} else {
        			model.ids.push(item.id);
        		}
	        };
	        
            var save = function(model){
            	return $scope.saveResource({}, model, null, function(error){
                	$scope.errors = [$formatter.error(error.data.message)];
            	}).$promise;
            };

	        $scope.save = function(model){
	            save(model).then(function(data){
	                $translate($scope.saveSuccessMessage).then(function(str){
	                    $scope.closeThisDialog({
	                        action: "message",
	                        type: "success",
	                        text: str,
	                        pageRefresh: true
	                    });
	                });
	            });
	    	}; 	        

	    }
	]);
});