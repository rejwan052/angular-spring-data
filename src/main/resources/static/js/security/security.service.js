define(['angular', 'jquerycookie'], function(angular) {
    return ['$securityConfigProvider', '$securitySessionProvider', 
            function($securityConfigProvider, $securitySessionProvider) {
		
		var SECURITY_CONFIG = $securityConfigProvider.Config;
		
		var SECURITY_SESSION = $securitySessionProvider.Session;
		
		var CSRF_HEADERS = {};
		
		var rememberMeAuthenticate = function(path) {	
    		CSRF_HEADERS[SECURITY_CONFIG.csrfHeaderName] = $.cookie(SECURITY_CONFIG.csrfCookieName);
			var authData = $.ajax({
				type: 'POST',
			    url: SECURITY_CONFIG.rememberMeAuthenticateUrl,
			    headers: CSRF_HEADERS,  
			    async: false
			});
	        
	        $.when(authData).then(function(authData){
				if (authData.name) {
					doAfterRememberMeSuccess(path, authData);
				} else {
					doAfterRememberMeError();								
				}	        		
	        }, function(error){
	        	doAfterRememberMeError();	
	        });		        

		};
		
		var doAfterRememberMeError = function() {
			console.log("Login failed");
			SECURITY_SESSION.invalidate();
		};
		
		var doAfterRememberMeSuccess = function(path, authData) {
			console.log("Login succeeded"); 
			SECURITY_SESSION.initData(authData);
			SECURITY_SESSION.updatePath(path);
		};			
		
		var get = ['$state', '$location', '$rootScope', '$http',
		           function($state, $location, $rootScope, $http) {
			
			var redirectToPath = function(path) {
				$location.path(path);
			};
			
			var enter = function() {
				SECURITY_SESSION.updatePath($location.path());
				if (SECURITY_SESSION.permissions.authenticated()) {
					redirectToPath(SECURITY_SESSION.path);							
				} else {
					redirectToPath(SECURITY_SESSION.fullLoginPath);
				}	
			};	
			
			var doAfterAuthSuccess = function(authData, callback) {
				console.log("Login succeeded"); 
				SECURITY_SESSION.initData(authData);
				enter();
				callback && callback(true);
			};
			
			var doAfterAuthError = function(callback) {
				console.log("Login failed");
				SECURITY_SESSION.invalidate();
				callback && callback(false);
			};			
			
			var authenticate = function(credentials, callback) {
	    		CSRF_HEADERS[SECURITY_CONFIG.csrfHeaderName] = $.cookie(SECURITY_CONFIG.csrfCookieName);
				var authData = $.ajax({
					type: 'POST',
				    url: SECURITY_CONFIG.authenticateUrl,
				    headers: CSRF_HEADERS, 
				    data: {
						username: credentials.username, 
						password: credentials.password, 
						rememberMe: credentials.rememberMe
					},
				    async: false
				});
		        
		        $.when(authData).then(function(authData){
					if (authData.name) {
						doAfterAuthSuccess(authData, callback);
					} else {
						doAfterAuthError(callback);								
					}	        		
		        }, function(error){
					doAfterAuthError(callback);	
		        });		        

			};			
			
			var postLogout = function() {
		        return $http
	        	.post(SECURITY_CONFIG.logoutUrl, {})
	        	.success(function(response) {
					console.log("Logout succeeded");
	        	})
	        	.error(function(errResp) {
					console.log("Logout failed");
	        	});
			}; 
			
			var authorizeMenu = function(menu){
	    		var permissions = SECURITY_SESSION.permissions;
				var result = new Array();
				angular.forEach(menu.tabs, function(item){
	            	var revert = item.permissionMode && item.permissionMode == 'not';
	        		if (item.permission instanceof Array) {
	                    if (!item.permissionMode 
	                    		|| item.permissionMode == "or" || item.permissionMode == 'not') {
	                        for (var i = 0; i < item.permission.length; i++) {
	                            var el = item.permission[i];
	                            if (!revert && permissions[el]()) {
	                                result.push(item);
	                                break;
	                            } else if (revert && !(permissions[el]())) {
	                                result.push(item);
	                                break;                                	
	                            }
	                        }
	                    } else if (item.permissionMode == "and") {
	                        var shouldAddAction = true;
	                        for (var k = 0; k < item.permission.length; k++) {
	                            var el1 = item.permission[k];
	                            if (!(permissions[el1]())) {
	                                shouldAddAction = false;
	                                break;
	                            }
	                        }

	                        if (shouldAddAction) {
	                            result.push(item);
	                        }
	                    }

	                }
	                if (!(item.permission instanceof Array) 
	                		&& !revert 
	                		&& permissions[item.permission]()) {
	        			result.push(item);
	                }
	                if (!(item.permission instanceof Array) 
	                		&& revert 
	                		&& !(permissions[item.permission]())) {
	        			result.push(item);
	                }
	                if (item.permission == "always") {
	        			result.push(item);
	                }        				
	            });
				return result;
			}; 			
			
			return {
				
				logout : function(callback) {
			        postLogout().then(function() {
			        	SECURITY_SESSION.invalidate();
						enter();
						callback && callback();
			        });
				},					
				
				start: function() {
					// Guard route changes and switch to login page if unauthenticated
					$rootScope.$on('$routeChangeStart', function() {
						enter();
					});	
					enter();
				},					
				
				login: function(credentials, callback) {
					authenticate(credentials, callback);
				},
				
				authorizeMenu: function(menu) {
					return authorizeMenu(menu);
				}
			}
		}];

		this.$get = get;
		
		this.rememberMeAuthenticate = rememberMeAuthenticate;
    }]
});
