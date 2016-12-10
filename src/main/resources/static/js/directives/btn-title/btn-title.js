define([
	'angular', 
	'directives',
	'translate'
], function (angular, directives) {
	directives.directive("btnTitle",["$translate", function($translate) {
	    return {
	        restrict: "A",
	        link: function($scope, el, attrs) {
	        	if(attrs.btnTitle){
	        		$translate(attrs.btnTitle).then(function (data) {
	        			$(el).attr('title', data);
		  			});
	        	};
	        }
	    };
	}]);
});
