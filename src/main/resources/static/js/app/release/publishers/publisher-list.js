define([
	'angular',
	'controllers',
	'translate',
	'formatterService',
	'messageService'
], function(angular, controllers) {
    controllers.controller("publisherListCtrl", ["$scope", "$stateFilter", "$translate", "$skgGrid", 
                           "$publisher", "ngTableParams", "$formatter", "$confirm", "$message", 
                           "$securitySession", "$q",
	    function ($scope, $stateFilter, $translate, $skgGrid, $publisher, ngTableParams, 
	    		$formatter, $confirm, $message, $securitySession, $q) {
    	
        	$scope.adminWriter = $securitySession.permissions.adminWriter();

            $scope.filter = $stateFilter.getFilter({
	    		searchQuery: ""
	    	});

	    	var renderGrid = function(){
	    		var publishers = $publisher.publishers($scope.filter);
	    		$q.all([publishers.$promise]).then(function(data){
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
		                	publishersFilter(parameters, params, $defer);
		                }
		            });
	    		});
	    	};

	    	var publishersFilter = function(parameters, params, defer){
				$publisher.publishers(parameters, function(data) {
			    	params.total(data.totalRecords);
	    			defer.resolve(data.results);
				});
	    	};

            $scope.deletePublisher = function(item){
                $publisher.deletePublisher({
                    id: item.id
                }, function(){
      				$scope.tableParams.reload();
                }, function (error) {
                    $message("error", $formatter.error(error.data));
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