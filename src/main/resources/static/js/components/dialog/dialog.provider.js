define(['angular'], function(angular) {
    return [function(){

        var configurations = {};

        var getNavidationTemplate = function(value){
            if (configurations.navigation.hasOwnProperty(value)) {
                return configurations.navigation[value];
            } else {
                return configurations.settings.contentUrl + value
            }
        };

        var getHeader = function(value){
            if (value) {
                return configurations.settings.contentUrl + value;
            } else {
                return false
            }
        };

        var get = ["ngDialog", "$rootScope", function(ngDialog, $rootScope){

            var refresh = function(val, params){
                if (!val || val == "tab") {
                    $rootScope.$broadcast('refreshTab', params);
                } else if (val && val == "page") {
                    $rootScope.$broadcast('refreshPage', params);
                }
            };
            
            return {

                isOpen: function(id){
                    return ngDialog.isOpen(id)
                },

                open: function(e, dialog, params){
                    
                    if (e) {
                        e.preventDefault();
                    };
                    
                    var config = configurations.dialogs[dialog];

                    var scope = $rootScope.$new();

                    scope.refresh = function(){
                        refresh();
                    };

                    var data = {
                        template: configurations.settings.contentUrl + config.template,
                        navigation: getNavidationTemplate(config.navigation),
                        header: getHeader(config.header),
                        dialogName: dialog,
                        model: config.model,
                        spinner: configurations.settings.spinner,
                        basePath: configurations.settings.contentUrl,
                        cols: config.cols,
                        controller: config.controller,
                        selected: config.selected
                    };

                    angular.forEach(params, function(val, key){
                        data[key] = params[key]
                    });

                    var parameters = {
                        className: config.class,
                        template: configurations.base.dialog,
                        controller: config.controller,
                        showClose: config.hasOwnProperty("showClose") ? config.showClose : true,
                        closeByEscape: config.hasOwnProperty("closeByEscape") ? config.closeByEscape : true,
                        data: data,
                        scope: scope,
                        controllerAs: "skgDialogCtrl"
                    };

                    var modal = ngDialog.open(parameters);

                    return modal;

                },

                titles: function(){
                    return configurations.titles;
                },

                dialogs: function(){
                    return configurations.dialogs;
                },

                configs: function(value){
                    if (value) {
                        return configurations[value]
                    } else {
                        return configurations
                    };
                }
            }
        }];

        this.$get = get;

        this.config = function(value, obj){
            configurations[value] = obj;
        };

    }]
});
