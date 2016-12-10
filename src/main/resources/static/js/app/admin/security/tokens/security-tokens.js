define([
	'angular',
	'controllers',
	'translate',
	'formatterService',
	'messageService'
], function(angular, controllers) {
    controllers.controller("adminSecurityTokensCtrl", ["$scope", "$filter", "$translate", "$state", 
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
                fields: ["userLogin", "ipAddress", "date", "userAgent", "value"]
            };

            $scope.getTokens = function(){
                $security.tokens(function(data){
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

            $scope.deleteToken = function(item){
                $translate([
                    'confirms.deleteToken',
                    'messages.success.tokenDeleted'
                ], {
                    name: item.userLogin,
                }).then(function (str) {
                    $confirm(str['confirms.deleteToken'], function(){
                        $security.deleteToken({
                            id: encodeURIComponent(item.series)
                        }, function(){
                            $message("success", str["messages.success.tokenDeleted"]);
                            $scope.getTokens();
                        }, function (error) {
                            $message("error", $formatter.error(error.data));
                        });
                    });
                });
            };

            $scope.$on('refreshTab', function() {
                $scope.getTokens();
            });

            $translate(
                [
                    'table.headers.userLogin',
                    'table.headers.ipAddress',
                    'table.headers.date',
                    'table.headers.userAgent',
                    'table.headers.value'
                ]
            ).then(function (data) {
                $scope.tableHeaders = {
                    userLogin: data["table.headers.userLogin"],
                    ipAddress: data["table.headers.ipAddress"],
                    date: data["table.headers.date"],
                	userAgent: data["table.headers.userAgent"],
                    value: data["table.headers.value"]
                };
                $scope.getTokens();
            });

	    }
	])
});