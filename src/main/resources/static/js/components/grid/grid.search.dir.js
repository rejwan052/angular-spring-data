define([
	'angular'
], function (angular, directives, jquery, bootstrap) {
	return ["$gridConfig", function($gridConfig) {
	    return {
	        restrict: "A",
	        replace: true,
	        scope: {
	        	table: "=gridSearch"
	        },
	        template: "<div class='input-group' ng-include='template' ng-style='searchParams.width'></div>",
	        link: function(scope, el, attrs) {
	        	
        		var search = $gridConfig.get("search");

	        	if (search.hasOwnProperty(attrs.gridSearchTpl)) {
	        		scope.template = search[attrs.gridSearchTpl];
	        	} else {
	        		scope.template = search.base
	        	};

	        	scope.searchParams = {};

	        	if (attrs.hasOwnProperty("gridSearchWidth")) {
        			scope.searchParams.width = {
        				width: attrs.gridSearchWidth
        			}
        		} else {
        			scope.searchParams.width = {
        				width: "300px"
        			}
        		};

        		if (attrs.hasOwnProperty("placeholder")) {
        			scope.searchParams.placeholder = attrs.placeholder;
        		} else {
        			scope.searchParams.placeholder = "Search";
        		}

            }
	    };
	}];
});
