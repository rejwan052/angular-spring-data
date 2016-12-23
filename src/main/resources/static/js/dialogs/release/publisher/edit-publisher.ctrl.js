define([
	'angular',
	'controllers'
	], function(angular, controllers) {
    controllers.controller("savePublisherDialogCtrl", ["$scope", "$stateFilter", "$translate", "$skgGrid", 
                           "$publisher", "ngTableParams", "$formatter", "$confirm", "$message", 
                           "$securitySession", "$q", "$security",
	    function ($scope, $stateFilter, $translate, $skgGrid, $publisher, ngTableParams, 
	    		$formatter, $confirm, $message, $securitySession, $q, $security) {
    	
	    	$scope.formModel = angular.copy($scope.ngDialogData.model);
	    	$scope.loading = true;
	    	
	    	$scope.isNew = function() {
	    		return !$scope.ngDialogData.item;
	    	}
	    	
	    	if ($scope.isNew()) {
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
		                	usersFilter(parameters, params, $defer);
		                }
		            });
		    	};

		    	var usersFilter = function(parameters, params, defer){
					$security.notPublishers(parameters, function(data) {
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
   		
	    	}            


	    	var getFormData = function(params, model){
                if(params.item){
	    			angular.forEach(model, function(val, key){
	    				model[key] = params.item[key];
	    			});
                }
                $scope.loading = false;
	    	};

            var save = function(model){
                return $publisher.savePublisher({}, model, null, function(error){
                    $scope.errors = [$formatter.error(error.data.message)];
                }).$promise;
            };

            $scope.save = function(model){
                save(model).then(function(data){
                    $translate("messages.success.publisherSaved", {name: model.name, user: model.user}).then(function(str){
                        $scope.closeThisDialog({
                            action: "message",
                            type: "success",
                            text: str,
                            pageRefresh: true
                        });
                    });
                });
	    	};

            $scope.saveAndNew = function(model){
                save(model).then(function(data){
                    $translate("messages.success.publisherSaved", {name: model.name}).then(function (str) {
                        $message("success", str);
                        $scope.dialogForm.$setPristine();
                        $scope.formModel = angular.copy($scope.ngDialogData.model);
                        $scope.refresh();
                        getFormData($scope.ngDialogData, $scope.formModel);
                    });
                });
            };

            getFormData($scope.ngDialogData, $scope.formModel);            

	    }
	]);
});