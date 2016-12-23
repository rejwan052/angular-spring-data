define([
	'angular',
	'utilsModule',
	'tablePlugin',
    'ngLocalStorage',
	'components/grid/grid.factory',
	'components/grid/grid.search.dir',
    'components/grid/grid.provider',
    'components/grid/grid.pager.dir',
    'components/grid/grid.message.dir',
    'components/grid/grid.loading.dir',
    'components/grid/grid.filter.dir'
], function(
    angular,
    utils,
    ngTable,
    ngLocalStorage,
    gridFactory,
    gridSearchDirective,
    gridConfigProvider,
    gridPagerDirective,
    gridMessageDirective,
    gridLoadingDirective,
    gridFilterDirective
) {
    angular.module('skyglass.component.grid', [
    	'ngTable',
        'LocalStorageModule',
    	'skyglass.utils'
    ])
    .factory("$grid", gridFactory)
    .provider("$gridConfig", gridConfigProvider)
    .directive("gridSearch", gridSearchDirective)
    .directive("gridPager", gridPagerDirective)
    .directive("gridMessage", gridMessageDirective)
    .directive("gridLoading", gridLoadingDirective)
    .directive("gridFilter", gridFilterDirective)
    .run(['$templateCache', function ($templateCache) {
        $templateCache.put('no-grid-pager', '');
    }]);
});
