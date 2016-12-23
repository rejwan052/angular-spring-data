define([
    'angular',
    'translate',
    'sanitize',
    'translatefiles',
    'tablePlugin',
    'ngLocalStorage',
    'bootstrapTpls'
], function(angular) {
    return angular.module('skyglass.thirdparty', [
        'pascalprecht.translate',  
        'ngTable',
        'ngSanitize',
        'ui.bootstrap'
        ])
        .config(["$translateProvider", function($translateProvider) {

            $translateProvider.useStaticFilesLoader({
                prefix: 'js/locale/',
                suffix: '.json'
            });

            $translateProvider.preferredLanguage('en');
            
            $translateProvider.useSanitizeValueStrategy('sanitize');

        }]);
});