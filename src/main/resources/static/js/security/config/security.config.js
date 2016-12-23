define(['angular'], function(angular) {
    return [function(){
		
		var appName = 'skyglass';
		
		var CONFIG = {
				appName: appName,
				baseUrl: '/',
				stateProxyProperty: 'proxy',
				homeState: appName + '.release.category.list',			
				homePath: '/release/category/list',	
				loginState: appName + '.login',
				loginPath: '/login',
				rememberMeAuthenticateUrl: '/rememberMeAuthenticate',
				authenticateUrl: '/authenticate',
				logoutUrl: '/logout',
				noPermissionsState: 'skyglass.permissions',
				noPermissionsPath: '/permissions',
				csrfCookieName: 'XSRF-TOKEN',
				csrfHeaderName: 'X-XSRF-TOKEN'
	    };
		
		var get = [function() {		
			
			return {
				
				config: CONFIG			
			
				
			}		
		}];

		this.$get = get;		

        this.Config = CONFIG;

    }]
});
