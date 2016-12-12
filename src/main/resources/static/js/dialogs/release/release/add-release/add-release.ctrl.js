define([
    'angular',
    'controllers',
    'addReleaseCategoryDialogCtrl',
    'addReleasePublisherDialogCtrl',
    'addReleaseUploadDialogCtrl'
], function(angular, controllers) {
    controllers.controller("addReleaseDialogCtrl", ["$window", "$scope", "$translate", "$formatter", "$release",
        function ($window, $scope, $translate, $formatter, $release) {

            $scope.formModel = angular.copy($scope.ngDialogData.model);
            $scope.title = $scope.ngDialogData.title;

            var modelProps = {
                category: "categoryId",
                publisher: "publisherId",
                file: "file"
            };

            var setData = function(data, wizard){
                
                var steps = [];
                
                angular.forEach(data, function(val, key){
                    if (wizard.hasOwnProperty(key)) {
                        wizard[key] = val;
                        steps.push(key);
                    }
                });

                if (steps.indexOf("category") != -1 && steps.indexOf("publisher") == -1) {
                    wizard.step = "publisher"
                } else if (steps.indexOf("category") != -1 && steps.indexOf("publisher") != -1){
                    wizard.step = "upload"
                } else {
                    wizard.step = "category"
                }
            };

            $scope.wizard = {
                category: {},
                publisher: {},
                upload: {}
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

                $release.publish({}, data, function(response){
                    $scope.closeThisDialog({
                        aprId: response.requestId,
                        schedule: data.scheduleCheckbox
                    });
                }, function(error){
                    $scope.errors = [$formatter.error(error.data)];
                });
            };

            $translate([
                "table.headers.name"
            ]).then(function(str){
                $scope.tableHeaders = {
                    name: str["table.headers.name"]
                };
            });

            setData($scope.ngDialogData.wizardData, $scope.wizard)

        }
    ]);
});