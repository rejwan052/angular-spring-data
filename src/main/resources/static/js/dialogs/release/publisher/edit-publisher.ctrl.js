define([
	'angular',
	'controllers'
], function(angular, controllers) {
    controllers.controller("savePublisherDialogCtrl", ["$scope", "$formatter", "$publisher", 
                           "$security", "ngTableParams", "$translate", "$message",
	    function ($scope, $formatter, $publisher, $security, ngTableParams, $translate, $message) {
    	
	    	$scope.formModel = angular.copy($scope.ngDialogData.model);
	    	$scope.loading = true;
	    	
	    	$scope.isNew = function() {
	    		return !$scope.ngDialogData.item;
	    	}
	    	
	    	if ($scope.isNew()) {
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
	                    getFormData($scope.ngDialogData, $scope.formModel);  
	                    var parameters = $formatter.resourceUrl(params.url(), $scope.filter);
	                	$security.notPublishers(parameters, function(data){
	                        params.total(data.length);
	                        $defer.resolve(data);
	                    });
	                }
	            });

	            $translate([
	                "table.headers.name"
	            ]).then(function (str) {
	                $scope.tableHeaders = {
	                    name: str["table.headers.name"]
	                };
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