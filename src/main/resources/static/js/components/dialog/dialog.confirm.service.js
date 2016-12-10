define(['angular'], function(angular, services) {
    return ["$dialog", "ngDialog",
        function($dialog, ngDialog) {

            var open = function(text, successCallback, failCallback){
                ngDialog.openConfirm({
                    template: $dialog.configs("base").confirmation,
                    className: 'flex-confirmation',
                    controller: ["$scope",
                        function ($scope) {
                            $scope.title = "Confirmation!";
                            $scope.text = $scope.ngDialogData.text
                        }
                    ],
                    data: {
                        text: text
                    }
                }).then(successCallback, failCallback);
            };
            

            return function(text, successCallback, failCallback){
                if (text && successCallback) {
                    open(text, successCallback, failCallback);
                };
            };

        }
    ];
});
