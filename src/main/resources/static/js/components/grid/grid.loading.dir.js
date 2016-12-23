define([
	'angular'
], function (angular) {
	return ["$compile", "$gridConfig", "$parse", function($compile, $gridConfig, $parse) {
	    return {
	        restrict: "A",
	        replace: true,
	        link: function(scope, el, attrs) {

                scope.spinner = {
                    img: $gridConfig.get("spinner"),
                    vissible: true
                };

                attrs.$observe("gridLoading", function(value){
                	scope.spinner.vissible = !(value === "false");
                });

                var template = 
                    "<div class='grid-spinner' ng-show='spinner.vissible'>" +
                    	"<div class='bg'></div>" +
                        "<img ng-src='{{spinner.img}}' />" +
                    "</div>"

                var html = $compile(template)(scope);
                el.after(html);
	        	
            }
	    };
	}];
});
