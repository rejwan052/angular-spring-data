define([
	'angular',
	'ngDialog',
	'components/dialog/dialog.provider',
	'components/dialog/dialog.title.directive',
	'components/dialog/dialog.open.directive',
	'components/dialog/dialog.confirm.service'
], function(angular, ngDialog, dialogProvider, dialogComponentTitleDirective, dialogComponentOpenDirective, dialogConfirmService) {
    angular.module('skyglass.component.dialog', ['ngDialog'])
    	.config(["ngDialogProvider", function(ngDialogProvider) {
	        ngDialogProvider.setDefaults({
	            plain: false,
	            showClose: true,
	            closeByDocument: false,
	            closeByEscape: true,
	            appendTo: false
	        });
    	}])
    	.provider("$dialog", dialogProvider)
    	.directive("dialogTitle", dialogComponentTitleDirective)
    	.directive("dialogOpen", dialogComponentOpenDirective)
    	.service("$confirm", dialogConfirmService);
});
