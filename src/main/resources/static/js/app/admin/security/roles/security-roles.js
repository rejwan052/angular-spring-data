define([
	'angular',
	'controllers',
	'translate'
], function(angular, controllers) {
    controllers.controller("adminSecurityRolesCtrl", ["$scope", "$filter", "$translate", "$security", 
                           "$formatter", "$confirm", "$message", "$q", "ngTableParams", "$securitySession",
	    function ($scope, $filter, $translate, $security, $formatter, 
	    		$confirm, $message, $q, ngTableParams, $securitySession) {
    	
    		var tableData;
    		
            $scope.securityWriter = $securitySession.permissions.securityWriter();
    		
            $scope.data = {
                    list: [],
                    selected: "",
                    loading: true
            };

            $scope.filter = {
                searchQuery: "",
                fields: ["name"]
            };

            $scope.getRoles = function(){
                $security.roles(function(data){
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
            
            $scope.deleteRole = function(item){
                $translate([
                    'confirms.deleteRole',
                    'messages.success.itemDeleted'
                ], {
                    name: item.name
                }).then(function (str) {
                    $confirm(str['confirms.deleteRole'], function(){
                        $security.deleteRole({
                            id: item.id
                        }, function(){
                            $message("success", str["messages.success.itemDeleted"]);
                            $scope.getRoles();
                        });
                    });
                });
            };
            
            $scope.$on('refreshTab', function() {
                $scope.getRoles();
            });

            $translate(
                [
                 'table.headers.name'
                ]
            ).then(function (data) {
                $scope.tableHeaders = {
                    name: data["table.headers.name"]
                };
                $scope.getRoles();
            });            

	    }
	]);
});