define([
	'angular'
], function (angular) {
	return ["$gridConfig", function($gridConfig) {
	    return {
	        restrict: "A",
	        replace: true,
	        scope: {
	        	table: "=gridFilter",
	        	labels: "=gridFilterLabels"
	        },
	        transclude: true,
	        template: "<ul class='grid-filter'><li ng-repeat='item in filters' ng-include='item'></li></ul>",
	        link: function($scope, el, attrs) {

	        	var types = attrs.gridFilterTypes.split(",");

	        	$scope.filters = types.map(function(item){
	        		return $gridConfig.get("filter")[item.trim()];
	        	});

	        	$scope.applyFilter = function(){
	        		$scope.table.filterdata();
	        	};

            }
	    };
	}];
});
