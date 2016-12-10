define(['angular'], function(angular) {
    return ['$securityPermissionsProvider', '$securityConfigProvider', 
            function($securityPermissionsProvider, $securityConfigProvider) {
    	
    	var PERMISSIONS = $securityPermissionsProvider.Permissions;
    	
    	var CONFIG = $securityConfigProvider.Config;	
    	
		var APP_NAME = CONFIG.appName; 
		
		var FULL_HOME_PATH = '/' + APP_NAME + CONFIG.homePath; 
		
		var FULL_LOGIN_PATH = '/' + APP_NAME + CONFIG.loginPath; 
		
		var FULL_NO_PERMISSIONS_PATH = '/' + APP_NAME + CONFIG.noPermissionsPath;
	    
		var isBookmarkablePath = function(path) {
			return path != FULL_LOGIN_PATH && path != FULL_NO_PERMISSIONS_PATH; 
		}		
                                   
		var SESSION = {	
				
				path : undefined,
				
				user: {},
	
				invalidate : function() {
					SESSION.user = {};
					PERMISSIONS.invalidate();
				},	
				
				initData : function(authData) {
					SESSION.user.name = authData.name;
					PERMISSIONS.initData(authData);
				},
				
				updatePath : function(locationPath) {
					if (isBookmarkablePath(locationPath)) {
						SESSION.path = locationPath;
					} else if (!SESSION.path) {
						SESSION.path = FULL_HOME_PATH;
					}				
				},
				
				permissions: PERMISSIONS,
				
				fullLoginPath: FULL_LOGIN_PATH
		};			
	
		var get = [function() {		
		
			return {
				
				permissions: PERMISSIONS,
				
				session: SESSION,
				
				user: function() {
					return SESSION.user;
				}
			}		
		}];

		this.$get = get;
		
		this.Session = SESSION;
	
    }]
});