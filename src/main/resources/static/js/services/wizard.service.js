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
	            publishRelease: function(name, contentUrl){
	                return {
	                    name: name + ".publishRelease",
	                    url: "/publish-release",
	                    views: {
	                        "publisher@": {
	                            templateUrl: contentUrl + 'js/dialogs/publish-release/publisher/publish-release-publisher.html',
	                            controller: 'publishReleasePublisherDialogCtrl'
	                        },
	                        "category@": {
	                            templateUrl: contentUrl + 'js/dialogs/publish-release/category/publish-release-category.html',
	                            controller: 'publishReleaseCategoryDialogCtrl'
	                        },	                    	
	                        "upload@": {
	                            templateUrl: contentUrl + 'js/dialogs/publish-release/upload/publish-release-upload.html',
	                            controller: 'publishReleaseUploadDialogCtrl'
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
	            addSubscription: function(name, contentUrl){
	                return {
	                    name: name + ".createSubscription",
	                    url: "/create-subscription",
	                    views: {
	                        "subscriber@": {
	                            templateUrl: contentUrl + 'js/dialogs/add-subscription/subscriber/add-subscription-subscriber.html',
	                            controller: 'addSubsriptionSubscriberDialogCtrl'
	                        },
	                        "category@": {
	                            templateUrl: contentUrl + 'js/dialogs/add-subscription/category/add-subscription-category.html',
	                            controller: 'addSubscriptionCategoryDialogCtrl'
	                        }
	                    },
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

		this.set = function(name, contentUrl){
			angular.forEach(parents[name], function(parent) {
				$stateProvider.state(wizards[name](parent, contentUrl));
			});
		};

	}])
});
