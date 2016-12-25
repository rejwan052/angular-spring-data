define([
	'angular',
	'controllers'
	], function(angular, controllers) {
    controllers.controller("editPublisherDialogCtrl", ["$scope", "$stateFilter", "$translate", "$skgGrid", 
                           "$publisher", "ngTableParams", "$formatter", "$confirm", "$message", 
                           "$securitySession", "$q", "$security",
	    function ($scope, $stateFilter, $translate, $skgGrid, $publisher, ngTableParams, 
	    		$formatter, $confirm, $message, $securitySession, $q, $security) {   
    	
    		$scope.formModel = angular.copy($scope.ngDialogData.model);
	    		
	    	$scope.loading = true;
    		
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
                    $translate("messages.success.publisherSaved", {name: model.name}).then(function(str){
                        $scope.closeThisDialog({
                            action: "message",
                            type: "success",
                            text: str,
                            pageRefresh: true
                        });
                    });
                });
	    	};

            getFormData($scope.ngDialogData, $scope.formModel); 	    		
         

	    }
	]);
});