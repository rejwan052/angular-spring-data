define(['angular'], function(angular) {
    return ["$compile", "$util", function($compile, $util) {
        return {
            restrict: 'A',
            scope: true,
            link: function(scope, element, attrs) {
                
                var opacity = attrs.spinner || 0.7;

                scope.spinner = {
                    img: $util.config().spinner,
                    static: (attrs.hasOwnProperty("spinnerStatic") && attrs.spinnerStatic === "true"),
                    style: {
                        "background-color": "rgba(255, 255, 255, " + opacity + ")" 
                    }
                };
                var template = 
                    "<div ng-style='spinner.style' class='util-spinner' ng-class='{static: spinner.static}'>" +
                        "<img ng-src='{{spinner.img}}' />"
                    "</div>"
                var html = $compile(template)(scope);
                element.append(html);
            }
        };
    }];
});
