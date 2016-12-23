define([
    'angular',
    'controllers',
    'tableSearchDirective',
    'services',
    'breadcrumbsDirective',
    'showdown',
    'skgGridService',
    'stateFilterService'
], function(angular, controllers) {
    controllers.controller('indexCtrl', ['$rootScope', '$scope', '$securityService', '$securityMenuConfig', 
                                         '$message', 'ngDialog', '$dialog', '$wizard',
        function ($rootScope, $scope, $securityService, $securityMenuConfig, $message, ngDialog, $dialog,
        		$wizard) {    	
    		$rootScope.authorizeMainMenu = function() {
    			$rootScope.mainMenu = $securityService.authorizeMenu($securityMenuConfig['main.menu']);
    		};
    		
    		$rootScope.authorizeMainMenu();
    		
            var dialogActions = {
                    message: function(data){
                        $message(data.type, data.text);
                        if (!data.hasOwnProperty("noTabRefresh")) {
                            $scope.$broadcast('refreshTab', data.refreshParams);
                        };
                        if (data.pageRefresh) {
                            $scope.$broadcast('refreshPage', data.refreshParams);
                        }
                    },
                    buildout: function(data){
                        $state.go(data.state, data.params);
                    },
                    refresh: function (data) {
                        $scope.$broadcast('refreshTab');
                        if (data.pageRefresh) {
                            $scope.$broadcast('refreshPage');
                        }
                    },
                    goMessage: function(data){
                        $state.go(data.state, data.params);
                        $message(data.type, data.text);
                    }
                };

                $scope.openDialog = function(e, dialog, params){
                    $dialog.open(e, dialog, params).closePromise.then(function(data) {
                        if (data.value && dialogActions.hasOwnProperty(data.value.action)) {
                            dialogActions[data.value.action](data.value);
                        }
                    });
                };
                
                $scope.openWizard = function(e, wizard, params){
                    $wizard.open(e, wizard, params).closePromise.then(function(data) {
                        if (data.value && dialogActions.hasOwnProperty(data.value.action)) {
                            dialogActions[data.value.action](data.value);
                        }
                    });
                };                

                $scope.refreshPage = function(){
                    $scope.$broadcast('refreshPage');
                    $scope.$broadcast('refreshTab');
                };

                $scope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams){
                    var fromParent = fromState.name.split(".")[0],
                        toParent = toState.name.split(".")[0],
                        fromStr = JSON.stringify(fromParams),
                        toStr = JSON.stringify(toParams);
                    if (fromParent == toParent && fromStr != toStr) {
                        $scope.$broadcast('refreshPage');
                    }
                });  
                
                $scope.$on("indexRefresh", function(){

                });

                $scope.$on('ngDialog.opened', function (e, $dialog) {

                });

                $scope.$on('ngDialog.closing', function (e, $dialog) {

                });
        }
    ]);
});