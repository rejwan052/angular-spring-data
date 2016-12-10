define([
	'angular',
	'directives',
	'tabActionConfig'
], function (angular, directives) {
	directives.directive("tabAction",[
	"$navigation", "$tabActionConfig", "$bootstrap", "$translate", "$formatter", "$wizard", "$dialog", "$dialog",
	 function($navigation, $tabActionConfig, $bootstrap, $translate, $formatter, $wizard, $dialog, $dialog) {

		var processData = {
			"drop-down": function(config, permissions){
				var ta = {};
				angular.forEach(config, function(val, key){
					if (key != "actions") {
						ta[key] = val;
                    }
                });
				ta.actions = [];
				angular.forEach(config.actions, function(item){
					if (permissions.hasOwnProperty(item.permission) && permissions[item.permission]) {
	        			ta.actions.push(item);
                    }
                    if (item.permission == "always") {
	        			ta.actions.push(item);
                    }
                });
				return ta;
			},
			"btn": function(config, permissions){
				var ta = {};
				if (permissions.hasOwnProperty(config.permission) && permissions[config.permission]) {
					ta = config;
                }
                return ta;
			},
			"grid": function(config, permissions){
				var ta = {};
				if (permissions.hasOwnProperty(config.permission) && permissions[config.permission]) {
					ta = config;
                }
                return ta;
			},
			"edit": function(config, permissions){
				var ta = {};
				if (permissions.hasOwnProperty(config.permission) && permissions[config.permission]) {
					ta = config;
                }
                return ta;
			}
		};

	    return {
	        restrict: "A",
	        scope: {
	        	entity: "=tabActionEntity",
	        	params: "=tabActionParams",
	        	wizardParams: "=tabActionWizardParams",
	        	invoke: "&tabActionInvoke",
	        	invokeParams: "=tabActionInvokeParams"
	        },
	        templateUrl: $bootstrap.contentUrl + "js/directives/tab-action/tab-action.html",
	        link: function(scope, el, attrs) {

	        	var type = attrs.tabAction,
	        		tpl = $tabActionConfig[type].tpl,
	        		permissions = $navigation.globalPermissions,
	        		items = [];

	        	if (attrs.hasOwnProperty("tabActionEntity")) {
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
	        				scope.tabActionData = processData[tpl]($tabActionConfig[type], permissions);
                        }
                    })
	        	} else {
	        		scope.tabActionData = processData[tpl]($tabActionConfig[type], permissions);
                }
                scope.actionClick = function(e, item){
	        		if (item.hasOwnProperty("dialog")) {
	        			var params = item.params ? item.params : scope.params;
	        			if ($dialog.dialogs().hasOwnProperty(item.dialog)) {
	        				scope.$parent.openDialog(e, item.dialog, params);
	        			} else {
	        				scope.$parent.openModal(e, item.dialog, params);
	        			}
	        		} else if (item.hasOwnProperty("wizard")){
	        			$wizard.open(scope.wizardParams, item.wizard, e);
	        		} else if (item.hasOwnProperty("invoke")) {
                        e.preventDefault();
	        			scope.invoke(scope.invokeParams)
	        		};
	        	};

	        }
	    };
	}]);
});
