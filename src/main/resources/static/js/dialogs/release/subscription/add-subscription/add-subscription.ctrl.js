define([
    'angular',
    'controllers',
    'addSubscriptionCategoryDialogCtrl',
    'addSubscriptionSubscriberDialogCtrl'
], function(angular, controllers) {
    controllers.controller("addSubscriptionDialogCtrl", ["$window", "$scope", "$translate", "$formatter", "$subscription",
        function ($window, $scope, $translate, $formatter, $subscription) {

            $scope.formModel = angular.copy($scope.ngDialogData.model);
            $scope.title = $scope.ngDialogData.title;

            var modelProps = {
                category: "categoryId",
                subscriber: "subscriberId"
            };

            var setData = function(data, wizard){
                
                var steps = [];
                
                angular.forEach(data, function(val, key){
                    if (wizard.hasOwnProperty(key)) {
                        wizard[key] = val;
                        steps.push(key);
                    }
                });

                if (steps.indexOf("category") != -1 && steps.indexOf("subscriber") == -1) {
                    wizard.step = "subscriber"
                } else {
                    wizard.step = "category"
                }
            };

            $scope.wizard = {
                category: {},
                subscriber: {}
            };

            $scope.wizard.step = "category";

            $scope.submit = function(wizard, obj){

                var data = {};

                angular.forEach(obj, function(val, key){
                    wizard[key] = val
                });

                angular.forEach(wizard, function(val, key){
                    if (modelProps.hasOwnProperty(key) && !angular.equals({}, val)) {
                        data[modelProps[key]] = val.id
                    }
                });

                $subscription.saveSubscription({categoryId: data.categoryId, subscriberId: data.subscriberId}, data, function(response){
                    $scope.closeThisDialog({
                        categoryId: response.categoryId,
                        subscriberId: response.subscriberId
                    });
                }, function(error){
                    $scope.errors = [$formatter.error(error.data)];
                });
            };

            $translate([
                "table.headers.h1"
            ]).then(function(str){
                $scope.tableHeaders = {
                    h1: str["table.headers.h1"]
                };
            });

            setData($scope.ngDialogData.wizardData, $scope.wizard)

        }
    ]);
});