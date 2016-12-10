define([
	'angular',
	'directives',
	'jquery',
	'translate'
], function (angular, directives, jquery) {
	directives.directive("tableSearch", ["$debounce", function($debounce) {
	    return {
	        restrict: "A",
	        scope: {
	        	searchValue: "=tableSearchValue",
	        	table: "=tableSearch"
	        },
	        templateUrl: 'js/directives/table-search/table-search.html',
	        link: function(scope, el, attrs) {

	        	var attrVal = attrs.tableSearchValue.split("."),
	        		prop = attrVal[attrVal.length - 1];

	        	var getViewName = function(el){
                    return $(el).closest("[ui-view]").attr("ui-view");
                };

	        	scope.performSearch = $debounce(function(){

	        		var view = getViewName(el);

	        		if (scope.table.page() == 1) {
	        			scope.table.reload();
	        		} else {
	        			scope.table.page(1);
	        		}
	        		
        		}, 500);

        		if (attrs.hasOwnProperty("searchWidth")) {
        			scope.width = {
        				width: attrs.searchWidth
        			}
        		} else {
        			scope.width = {
        				width: "300px"
        			}
        		}
	        	
            }
	    };
	}]);
});
