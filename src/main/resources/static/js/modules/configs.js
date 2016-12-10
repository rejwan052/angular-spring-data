define([
    	'angular',  
    	'dialogConfig',
    	'dialogTitleConfig',
    	'utilsModule',
    	'dialogComponent'

], function (angular, dialogConfig, dialogTitleConfig) {
	return angular.module('skyglass.configs', [	
	        "skyglass.component.dialog",
			"skyglass.utils"
		])
		.config(dialogConfig)
		.config(dialogTitleConfig);
	});
