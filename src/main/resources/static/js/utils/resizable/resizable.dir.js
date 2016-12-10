define(['angular', 'jqueryui'], function(angular) {
    return ["$util", function($util) {
        return {
            restrict: 'E',
            link: function(scope, element, attrs) {
                $(element).css({
                    height: parseInt(attrs.minHeight)
                });
                $(element).resizable({
                    handles: attrs.handles,
                    minHeight: parseInt(attrs.minHeight)
                });
            }
        };
    }];
});
