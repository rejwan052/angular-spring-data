define([
	'angular',
	'utils/util.provider',
	'utils/interceptor.factory',
	'utils/spinner/spinner.dir',
	'utils/debounce.service',
	'utils/resizable/resizable.dir'
], function(angular, UtilProvider, InterceptorFactory, SpinnerDirective, DebounceService, ResizableDirective) {
    angular.module('skyglass.utils', [])
	.provider("$util", UtilProvider)
	.factory("interceptor", InterceptorFactory)
	.directive("spinner", SpinnerDirective)
	.directive("resizable", ResizableDirective)
	.service("$debounce", DebounceService)
	
	.config(['$httpProvider', function($httpProvider) {
    	$httpProvider.interceptors.push('interceptor');
	}]);
});