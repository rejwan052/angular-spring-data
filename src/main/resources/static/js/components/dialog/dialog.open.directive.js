define(['angular'], function(angular) {
    return ["$dialog", "$parse", function($dialog, $parse) {
        return {
            restrict: "A",
            link: function(scope, el, attrs) {
                $(el).on("click", function(e){
                    $dialog.open(e, attrs.dialogOpen, $parse(attrs.dialogParams)(scope));
                });
            }
        };
    }];
});
