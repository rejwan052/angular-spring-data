define([
    'angular',
    'permissions',
    'services'
], function(angular) {
    return angular.module('skyglass.security', ['permission']).run([
        "Permission",
        "$securitySession",
        function(Permission, $securitySession) {

            Permission.defineRole('authenticated', function(stateParams) {
            	return $securitySession.permissions.authenticated();
            });

            Permission.defineRole('admin', function(stateParams) {
                return $securitySession.permissions.admin();
            });

            Permission.defineRole('adminReader', function(stateParams) {
                return $securitySession.permissions.adminReader();
            });

            Permission.defineRole('adminWriter', function(stateParams) {
                return $securitySession.permissions.adminWriter();
            });
            
            Permission.defineRole('adminAuditor', function(stateParams) {
                return $securitySession.permissions.adminAuditor();
            });    
            
            Permission.defineRole('adminSecurity', function(stateParams) {
                return $securitySession.permissions.adminSecurity();
            });            

        }
    ]);
});
