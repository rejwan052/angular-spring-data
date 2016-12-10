define([
	'angular',
	'controllers',
	'translate',
	'formatterService',
	'messageService'
], function(angular, controllers) {
    controllers.controller("adminSecurityUsersCtrl", ["$scope", "$filter", "$translate", "$state", 
                           "$security", "ngTableParams", "$formatter", "$confirm", "$message", "$securitySession",
	    function ($scope, $filter, $translate, $state, $security, ngTableParams, 
	    		$formatter, $confirm, $message, $securitySession) {

            var tableData;
            
            $scope.securityWriter = $securitySession.permissions.securityWriter();

            $scope.data = {
                list: [],
                selected: "",
                loading: true
            };

            $scope.filter = {
                searchQuery: "",
                fields: ["login", "email", "name"]
            };

            $scope.getUsers = function(){
                $security.users(function(data){
                    renderGrid(data);
                });
            };

            var renderGrid = function(data){
                tableData = data;
                if ($scope.tableParams) {
                    $scope.tableParams.reload();
                } else {
                    $scope.tableParams = new ngTableParams({
                        page: 1,
                        count: 10,
                        sorting: {
                            name: "asc"
                        }
                    }, {
                        total: tableData.length,
                        getData: function($defer, params) {
                            var sliceStart = (params.page() - 1) * params.count();
                            var sliceEnd = params.page() * params.count();
                            var filteredData = params.filter() ? $filter('filterByFields')(tableData, $scope.filter.fields, $scope.filter.searchQuery) : tableData;
                            var orderedData = params.sorting() ? $filter('orderBy')(filteredData, params.orderBy()) : filteredData;
                            params.total(tableData.length);
                            $defer.resolve(orderedData.slice(sliceStart, sliceEnd));
                        }
                    });
                }
            };

            $scope.deleteUser = function(item){
                $translate([
                    'confirms.deleteUser',
                    'messages.success.itemDeleted'
                ], {
                    name: item.login,
                }).then(function (str) {
                    $confirm(str['confirms.deleteUser'], function(){
                        $security.deleteUser({
                            id: item.id
                        }, function(){
                            $message("success", str["messages.success.itemDeleted"]);
                            $scope.getUsers();
                        }, function (error) {
                            $message("error", $formatter.error(error.data));
                        });
                    });
                });
            };

            $scope.$on('refreshTab', function() {
                $scope.getUsers();
            });

            $translate(
                [
                    'table.headers.user',
                    'table.headers.name',
                    'table.headers.email'
                ]
            ).then(function (data) {
                $scope.tableHeaders = {
                    user: data["table.headers.user"],
                    name: data["table.headers.name"],
                    email: data["table.headers.email"]
                };
                $scope.getUsers();
            });

	    }
	])
});