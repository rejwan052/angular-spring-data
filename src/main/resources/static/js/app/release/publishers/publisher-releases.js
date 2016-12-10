define([
	'angular',
	'controllers',
	'translate',
	'formatterService',
	'messageService'
], function(angular, controllers) {
    controllers.controller("publisherReleasesCtrl", ["$scope", "$filter", "$translate", "$state", 
                           "$release", "ngTableParams", "$formatter", "$confirm", "$message", "$securitySession",
	    function ($scope, $filter, $translate, $state, $release, ngTableParams, 
	    		$formatter, $confirm, $message, $securitySession) {

            var tableData;
            
            $scope.adminWriter = $securitySession.permissions.adminWriter();

            $scope.data = {
                list: [],
                selected: "",
                loading: true
            };

            $scope.filter = {
                searchQuery: "",
                fields: ['name', 'publisher', 'publishDate']
            };

            $scope.getPublisherReleases = function(){
                $release.publisherReleases(function(data){
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

            $scope.deletePublisherRelease = function(item){
                $translate([
                    'confirms.deleteRelease',
                    'messages.success.itemDeleted'
                ], {
                    name: item.login,
                }).then(function (str) {
                    $confirm(str['confirms.deleteRelease'], function(){
                        $release.deleteRelease({
                            id: item.id
                        }, function(){
                            $message("success", str["messages.success.itemDeleted"]);
                            $scope.getPublisherReleases();
                        }, function (error) {
                            $message("error", $formatter.error(error.data));
                        });
                    });
                });
            };

            $scope.$on('refreshTab', function() {
                $scope.getPublisherReleases();
            });

            $translate(
                [
                    'table.headers.name',
                    'table.headers.publisher',
                    'table.headers.publishDate'
                ]
            ).then(function (data) {
                $scope.tableHeaders = {
                    name: data["table.headers.name"],
                    publisher: data["table.headers.publisher"],
                    publishDate: data["table.headers.publishDate"]
                };
                $scope.getPublisherReleases();
            });

	    }
	])
});