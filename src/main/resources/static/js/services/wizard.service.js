define(['angular', 'services'], function (angular, services) {
	services.provider("$wizard", ["$stateProvider",  function($stateProvider){

		var wizardParams = {};

        var dialogActions = {
            message: function(data, $message, $state){
                $message(data.type, data.text);
            },
            buildout: function(data, $message, $state){
                $state.go(data.state, data.params);
            },
            goMessage: function(data, $message, $state){
                $state.go(data.state, data.params);
                $message(data.type, data.text);
            }
        };

        function openWizardModal($wizardConfig, ngDialog, configName, $message, $state){
            var dialog = $wizardConfig[configName];
            var data = {
                title: (typeof(dialog.title) == "function" ? dialog.title(wizardParams) : dialog.title),
                model: dialog.model,
                wizardData: {}
            };

            angular.forEach(wizardParams, function (val, key) {
                data.wizardData[key] = wizardParams[key]
            });

            var modal = ngDialog.open({
                className: "ngdialog-theme-default " + dialog.class + " skg-dialog",
                template: dialog.template,
                controller: dialog.controller,
                showClose: dialog.hasOwnProperty("showClose") ? dialog.showClose : true,
                data: data
            });

            modal.closePromise.then(function(data) {
                if (data.value && dialogActions.hasOwnProperty(data.value.action)) {
                    dialogActions[data.value.action](data.value, $message, $state);
                }
            });

            return modal;
        }

		var wizards = {
	            addRelease: function(name){
	                return {
	                    name: name + ".addRelease",
	                    url: "/add-release",
	                    views: {
	                        "category@": {
	                            templateUrl: 'js/dialogs/release/release/add-release/category/add-release-category.html',
	                            controller: 'addReleaseCategoryDialogCtrl'
	                        },		                    	
	                        "publisher@": {
	                            templateUrl: 'js/dialogs/release/release/add-release/publisher/add-release-publisher.html',
	                            controller: 'addReleasePublisherDialogCtrl'
	                        },                   	
	                        "upload@": {
	                            templateUrl: 'js/dialogs/release/release/add-release/upload/add-release-upload.html',
	                            controller: 'addReleaseUploadDialogCtrl'
	                        }
	                    },
	                    onEnter: ['$stateParams', '$wizardConfig', 'ngDialog', '$state', '$message',
	                        function($stateParams, $wizardConfig, ngDialog, $state, $message){
	                        openWizardModal($wizardConfig, ngDialog, $message, $state).closePromise.then(function (data) {
	                            $state.go("^");
	                        });
	                    }]
	                }
	            },
	            addSubscription: function(name){
	                return {
	                    name: name + ".addSubscription",
	                    url: "/add-subscription",
	                    views: {
	                        "category@": {
	                            templateUrl: 'js/dialogs/release/subscription/add-subscription/category/add-subscription-category.html',
	                            controller: 'addSubscriptionCategoryDialogCtrl'
	                        },	                    	
	                        "subscriber@": {
	                            templateUrl: 'js/dialogs/release/subscription/add-subscription/subscriber/add-subscription-subscriber.html',
	                            controller: 'addSubsriptionSubscriberDialogCtrl'
	                        },	                    },
	                    onEnter: ['$wizardConfig', 'ngDialog', '$state', '$message',
	                        function($wizardConfig, ngDialog, $state, $message){
	                        openWizardModal($wizardConfig, ngDialog, $message, $state).closePromise.then(function (data) {
	                            $state.go("^");
	                        });
	                    }]
	                }
	            }	            
	            
    };

		var get = ["$state", function($state){
			return {
				open: function(params, wizardName, e){
					
					wizardParams = {};
					
					angular.forEach(params, function(val, key){
						wizardParams[key] = {
							id: val.id,
							name: val.name
						}						
					});
	        		
	        		$state.go($state.current.name + wizardName);
	        		
	        		if (e) {
	        			e.preventDefault();
	        		};
				}
			}
		}];

		this.$get = get;

		this.set = function(name){
			angular.forEach(parents[name], function(parent) {
				$stateProvider.state(wizards[name](parent));
			});
		};

	}])
});
