define([
	'angular',
	'controllers'
], function(angular, controllers) {
    controllers.controller("addSubscriptionSubscriberDialogCtrl", ["$scope", "$formatter", "$subscriber", "ngTableParams", "$translate",
	    function ($scope, $formatter, $subscriber, ngTableParams, $translate) {

	    	$scope.obj = {};

	    	$scope.filter = {
	    		searchQuery: "",
	    		searchFields: "name",
                id: $scope.wizard.category.id
	    	};

	    	$scope.tableParams = new ngTableParams({
                page: 1,
                count: 10
            }, {
                total: 0,
                getData: function($defer, params) {
                    var parameters = $formatter.resourceUrl(params.url(), $scope.filter);
                	$subscriber.subscribers(parameters, function(data){
                        params.total(data.totalRecords);
                        $defer.resolve(data.records);
                    });
                }
            });

            $scope.subscriberToCategory = function(obj){
            	$scope.wizard.subscriber = obj;
            	$scope.wizard.step = "category";
            };

            if ($scope.wizard.subscriber.id) {
                $scope.obj = $scope.wizard.subscriber;
                $scope.wizard.subscriber = {};
            }

            $translate([
                "table.headers.name"
            ]).then(function (str) {
                $scope.tableHeaders = {
                    name: str["table.headers.name"]
                };
            });

	    }
	]);
});