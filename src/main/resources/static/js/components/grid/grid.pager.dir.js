define([
	'angular'
], function (angular) {
	return ["$gridConfig", function($gridConfig) {
	    return {
	        restrict: "A",
	        replace: true,
	        scope: {
	        	table: "=gridPager",
	        },
	        template: "<div class='grid-pager' ng-include='template'></div>",
	        link: function(scope, el, attrs) {

	        	var pager = $gridConfig.get("pager");

	        	var table = scope.$watch("table.table", function(value){
	        		if (value) {
	        			scope.params = scope.table.table;
	        			table();
	        		};
	        	});

	        	var params = scope.$watch("params.settings().$scope", function(value){
	        		if (value) {
	        			scope.params.settings().$scope.$on('ngTableAfterReloadData', function() {
		                    scope.pages = scope.params.generatePagesArray(scope.params.page(), scope.params.total(), scope.params.count());
		                }, true);
		                params();
	        		};
	        	});

	        	if (pager.hasOwnProperty(attrs.gridPagerTpl)) {
	        		scope.template = pager[attrs.gridPagerTpl];
	        	} else {
	        		scope.template = pager.base;
	        	};
	        	
            }
	    };
	}];
});
