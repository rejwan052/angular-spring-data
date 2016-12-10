define([
    'angular',
    'controllers',
    'directives',
    'filtersService',
    'tablePlugin'
    
], function(angular, controllers) {
    controllers.controller("userRolesDialogCtrl", ["$scope", "$translate", "$q", "$security", "$message", "$formatter", "ngTableParams", "$filter", "$controller", "$q",
        function ($scope, $translate, $q, $security, $message, $formatter, ngTableParams, $filter, $controller, $q) {

            $scope.loading = true;

            angular.extend(this, $controller('dialogMultipleController', {$scope: $scope}));

            var vm = this;
            
            $scope.filter = {
            		searchQuery: "",
                    fields: ["name"]
            };

            var getSelected = function(data){
                return data.map(function(item){
                    return item.id;
                })
            };

            var getData = function(params){

                var allRoles = $security.roles(),
                    currentUserRoles = $security.getUserRoles({id: params.item.id}),
                    defer = $q.defer();

                $q.all([allRoles.$promise, currentUserRoles.$promise]).then(function(data){
                    defer.resolve({
                        roles: data[0],
                        selected: data[1]
                    });
                }, function(error){
                    defer.reject(error);
                });

                return defer.promise;

            };
            
            var renderGrid = function(data){
                tableData = data;
                if ($scope.tableParams) {
                    $scope.tableParams.reload();
                } else {
                    $scope.tableParams = new ngTableParams({
                        page: 1,
                        sorting: {
                            name: "asc"
                        },
                        filter: $scope.filter
                    }, {
                        total: tableData.length,
                        getData: function($defer, params) {
                            var filteredData = params.filter() ? $filter('filterByFields')(tableData, $scope.filter.fields, $scope.filter.searchQuery) : tableData;
                            filteredData = $filter('exclude')(filteredData, $scope.selectedItems);
                            var orderedData = params.sorting() ? $filter('orderBy')(filteredData, params.orderBy()) : filteredData;
                            params.total(tableData.length);
                            $defer.resolve(orderedData);
                        }
                    });
                    $scope.tableParams.data = data;
                }
            };            

            $scope.save = function(items) {
                $security.setUserRoles({id: $scope.ngDialogData.item.id}, {roleIds: items}, function () {
                    $translate("messages.success.userRolesSaved").then(function (str) {
                        $scope.closeThisDialog({
                            action: "message",
                            type: "success",
                            text: str
                        });
                    });
                }, function (error) {
                    $message("error", $formatter.error(error.data));
                });
            };

            this.init = function(){
                $q.all([
                    vm.getTranslations($scope.ngDialogData),
                    getData($scope.ngDialogData)
                ]).then(function(data){
                    $scope.userName = $scope.ngDialogData.item.name;
                    $scope.selectedItems = getSelected(data[1].selected);
                    $scope.allItems = data[1].roles;
                    renderGrid(data[1].roles);
                    vm.setWatcher();
                    $scope.loading = false;
                });
            };
            
        }
    ])
});