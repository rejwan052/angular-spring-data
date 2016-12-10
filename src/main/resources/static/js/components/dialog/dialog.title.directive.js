define(['angular'], function(angular) {
    return ["$dialog", function($dialog) {
        return {
            restrict: "A",
            scope: {
                data: "=dialogTitle"
            },
            template: "{{title}}",
            link: function(scope, el, attrs) {

                var obj = $dialog.titles();

                scope.$watch("data", function(val){
                    if (val && obj.hasOwnProperty(val.dialogName)) {
                        scope.title = obj[val.dialogName](val);
                    };
                });

            }
        };
    }];
});
