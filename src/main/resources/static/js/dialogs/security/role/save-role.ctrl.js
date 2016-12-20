define([
	'angular',
	'controllers'
	
], function(angular, controllers) {
    controllers.controller("saveRoleDialogCtrl", ["$scope", "$translate", "$formatter", "$security", "$message",
	    function ($scope, $translate, $formatter, $security, $message) {

	    	$scope.formModel = angular.copy($scope.ngDialogData.model);
	    	$scope.loading = true;

	    	var getData = function(params, model){
	    		if(params.item){
	    			angular.forEach(model, function(val, key){
	    				model[key] = params.item[key];
	    			});
	    		} 
                $scope.loading = false;
	    	};

	    	var save = function(model){
	    		return $security.saveRole({}, model, null, function(error){
	    			$scope.errors = [$formatter.error(error.data.message)];
	    		}).$promise;
	    	};

	    	$scope.save = function(model){
	    		save(model).then(function(){
	    			$translate("messages.success.roleSaved", {name: model.name}).then(function(str){
    					$scope.closeThisDialog({
                            action: "message",
                            type: "success",
                            text: str
                        });
                    });
	    		});
	    	};

	    	$scope.saveAndNew = function(model){
	    		save(model).then(function(data){
	    			$translate("messages.success.roleSaved", {name: model.name}).then(function (str) {
                        $message("success", str);
                        $scope.dialogForm.$setPristine();
                        $scope.formModel = angular.copy($scope.ngDialogData.model);
                        $scope.refresh();
                        getData($scope.ngDialogData, $scope.formModel);
                    });
	    		})
	    	};


    		getData($scope.ngDialogData, $scope.formModel);

	    }
	]);
});