/**
 * skyglass-security: Angular JS + Angular ui-router + angular-permission + uiBreadcrumbs directive
 * (+ server-side example: Spring Boot + Spring Security + Spring REST + Spring Data JPA)
 *
 * @author Mykhailo Skliar <michaelsw@ua.fm>
 * @url https://github.com/skyglass/angular-spring-security/
 * @license New BSD License <http://creativecommons.org/licenses/BSD/>
 */

/**
 * @ngdoc module
 * @name skyglass-security
 * @description skyglass-security: Angular JS Security
 * @example
 <doc:example>
 <doc:source>
	see https://github.com/skyglass/angular-spring-security/
 </doc:source>
 </doc:example>
 */
define([
	'angular',
	'permissions',
	'security/config/security.config',
	'security/config/security.permissions',
	'security/security.session',
	'security/security.service',
	'security/config/security.menu.config',
	'security/config/security.state.permissions',	
	'security/directives/security-menu/security-menu',
], function(angular, permissions, securityConfigProvider, securityPermissionsProvider,
		securitySessionProvider, securityServiceProvider, securityMenuConfig, 
		securityStateConfig, securityMenuDirective) {
    angular.module('skyglass.security', ['permission'])
    	.provider("$securityConfig", securityConfigProvider)
    	.provider("$securityPermissions", securityPermissionsProvider)	    	
    	.provider("$securitySession", securitySessionProvider)    	
    	.provider("$securityService", securityServiceProvider)
    	.service("$securityMenuConfig", securityMenuConfig)
    	.service("$securityStateConfig", securityStateConfig)
    	.directive("securityMenu", securityMenuDirective)
    	
		.run(function($securityStateConfig) {
			$securityStateConfig.setPermissions();
		});    	
    	
});
