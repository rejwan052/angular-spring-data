define([
	'angular'
], function (angular) {
	return ["$compile", "$translate", "$gridConfig", "$rootScope", function($compile, $translate, $gridConfig, $rootScope) {
	    return {
	        restrict: "A",
	        replace: true,
	        link: function(scope, el, attrs) {

	        	var mtpl = attrs.noDataMessageTpl || "base",
	        		tpl = "<tr ng-show='visible'><td ng-include='include' colspan='{{colspan}}'></td></tr>",
	        		dirScope = $rootScope.$new(),
	        		html = $compile(tpl)(dirScope);

	        	dirScope.include = $gridConfig.get("message")[mtpl];

	        	$translate(attrs.gridMessage).then(function(str){
	        		dirScope.translatedMessage = str;
	        	});

	        	if (attrs.hasOwnProperty("gridMessageCols")) {
	        		attrs.$observe("gridMessageCols", function (value) {
	        			dirScope.colspan = value;
	        		});
	        	} else {
	        		scope.$watch("$columns", function (value) {
	        			if (value) {
	        				dirScope.colspan = value.length;
	        			};
	        		})
	        	};

	        	scope.$watch("$data", function (value) {
	        		if (value) {
	        			dirScope.visible = !value.length
	        		} else {
	        			dirScope.visible = true;
	        		}
	        	});

	        	$(el).append(html);
	        	
            }
	    };
	}];
});
