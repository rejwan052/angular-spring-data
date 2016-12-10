define([
    'angular',
    'controllers'
    
], function(angular, controllers) {
    controllers.controller("resetUserPasswordDialogCtrl", ["$scope", "$translate", "$security", "$formatter",
        function ($scope, $translate, $security, $formatter) {

            $scope.formModel = angular.copy($scope.ngDialogData.model);
            
            $scope.save = function(){
                $security.setUserPassword({id: $scope.ngDialogData.item.id}, {password: $scope.formModel.password}, function(){
                    $translate("messages.success.passwordResetSuccess", {name: $scope.ngDialogData.item.name}).then(function(str){
                        $scope.closeThisDialog({
                            action: "message",
                            type: "success",
                            text: str,
                            pageRefresh: true
                        });
                    });
                }, function(error){
                    $scope.errors = [$formatter.error(error.data)];
                });
            };

        }
    ]).directive("passwordVerify", [function(){
        return {
            require: 'ngModel',
            link: function (scope, elem, attrs, model) {
                scope.$watch(attrs.passwordVerify, function (value) {
                    if (model.$viewValue) {
                        model.$setValidity('passwordVerify', value === model.$viewValue);
                    }
                });
                model.$parsers.push(function (value) {
                    if (!value) {
                        model.$setValidity('passwordVerify', true);
                        return value;
                    }
                    var isValid = value === scope.$eval(attrs.passwordVerify);
                    model.$setValidity('passwordVerify', isValid);
                    return isValid ? value : undefined;
                });
            }
        };
    }]);
});