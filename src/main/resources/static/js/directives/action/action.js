define([
	'angular',
	'directives',
	'actionConfig'
], function (angular, directives) {
	directives.directive("action",[
		"$navigation", "$actionConfig", "$bootstrap", "$translate", "$formatter", "$parse", "$rootScope", "$state", "$wizard", "$dialog",
		function($navigation, $actionConfig, $bootstrap, $translate, $formatter, $parse, $rootScope, $state, $wizard, $dialog) {

		var processData = function(config, permissions){
			var actions = {
				btn: config.btn,
				tpl: config.tpl,
				actions: []
			};
			angular.forEach(config.actions, function(item){
        		if (item.permission instanceof Array) {
                    if (!item.permissionMode || item.permissionMode == "or") {
                        for (var i = 0; i < item.permission.length; i++) {
                            var el = item.permission[i];
                            if (permissions.hasOwnProperty(el) && permissions[el]) {
                                actions.actions.push(item);
                                break;
                            }
                        }
                    } else if (item.permissionMode == "and") {
                        var shouldAddAction = true;
                        for (var k = 0; k < item.permission.length; k++) {
                            var el1 = item.permission[k];
                            if (!(permissions.hasOwnProperty(el1) && permissions[el1])) {
                                shouldAddAction = false;
                            }
                        }

                        if (shouldAddAction) {
                            actions.actions.push(item);
                        }
                    }

                }
                if (!(item.permission instanceof Array) && permissions.hasOwnProperty(item.permission) && permissions[item.permission]) {
        			actions.actions.push(item);
                }
                if (item.permission == "always") {
        			actions.actions.push(item);
                }
            });
			return actions;
		};

	    return {
	        restrict: "A",
	        scope: {
	        	params: "=actionParams",
	        	entity: "=actionEntity",
	        	wizardParams: "=actionWizardParams"
	        },
	        templateUrl: $bootstrap.contentUrl + "js/directives/action/action.html",
	        link: function(scope, el, attrs) {

	        	var type = attrs.action,
	        		permissions = $navigation.globalPermissions;

	        	if (attrs.hasOwnProperty("actionEntity")) {
	        		scope.$watch("entity", function(entity){
	        			if (entity) {
	        				var permissions = {};
	        				angular.forEach(entity.security, function(val, key){
	        					var pKey = $formatter.toCamelCase(key);
	        					permissions[pKey] = val;
	        				});
	        				angular.forEach(entity.extendedSecurity, function(val, key){
	        					var pKey = $formatter.toCamelCase(key);
	        					permissions[pKey] = val;
	        				});
	        				angular.forEach($navigation.globalPermissions, function(val, key){
	        					permissions[key] = val;
	        				});
	        				scope.pageActions = processData($actionConfig[type], permissions);
                        }
                    })
	        	} else {
	        		scope.pageActions = processData($actionConfig[type], permissions);
                }
                scope.actionClick = function(e, item){
	        		if (item.hasOwnProperty("dialog")) {
                        var params = item.params ? item.params : scope.params;
                        if ($dialog.dialogs().hasOwnProperty(item.dialog)) {
                        	scope.$parent.openDialog(e, item.dialog, params);
                        } else {
                        	scope.$parent.openModal(e, item.dialog, params);
                        }
	        		} else if (item.hasOwnProperty("export")){
	        			scope.$parent.exportEntity(e, item.export, scope.entity);
	        		} else if (item.hasOwnProperty("print")){
	        			scope.$parent.printPage(e);
	        		} else if (item.hasOwnProperty("wizard")) {
	        			$wizard.open(scope.wizardParams, item.wizard, e);
                    }
                };


	        }
	    };
	}]);
});
