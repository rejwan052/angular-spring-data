define([
	'angular',
	'dialogComponent',
	'gridComponent'
], function(angular) {
    return angular.module('skyglass.components', [
    	"skyglass.component.dialog",
    	"skyglass.component.grid"
    ])
});
