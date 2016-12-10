define([
	'angular',
    'controllers'
], function(angular, controllers) {
    controllers.controller("dialogMultipleController", ["$scope", "$translate", "$formatter", "$filter", "ngTableParams", "$q",
	    function ($scope, $translate, $formatter, $filter, ngTableParams, $q) {

            $scope.selectedItems = [];
            
            var tableData;

            this.getTranslations = function(params){
                var defer = $q.defer();
                $translate([
                    'table.headers.name',
                    params.selected
                ]).then(function(data){
                    $scope.tableHeaders = {
                        name: data["table.headers.name"]
                    };
                    $scope.labels = {
                        selected: data[params.selected]
                    }
                    defer.resolve(true)
                });
                return defer.promise;
            };        

            this.setWatcher = function(){
                $scope.$watchCollection("selectedItems", function(newVal, oldValue){
                    $scope.tableParams.reload();
                });
            };

            $scope.addItem = function(id){
                $scope.selectedItems.push(id)
            };

            $scope.removeItem = function(id){
                if ($scope.selectedItems.indexOf(id) != -1) {
                    var index = $scope.selectedItems.indexOf(id);
                    $scope.selectedItems.splice(index, 1);
                }
            };

	    }
	])
});