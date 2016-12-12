define(['angular'], function(angular) {
    return ['$securityService', '$securityConfig', '$securitySession', '$state', '$rootScope',
        function($securityService, $securityConfig, $securitySession, $state, $rootScope) {
		
		var defaultAdminState = function() {
			return 'skyglass.admin.' + ($securitySession.permissions.adminWriter() ? 'write' : 'read');
		};
		
		var defaultAdminStateName = function() {
			return $securitySession.permissions.adminWriter() ? 'Write' : 'Read';
		};
		
		var defaultAdminSecurityState = function() {
			return 'skyglass.admin.security.users';
		};

		return {
			
			defaultAdminState: defaultAdminState,
			
			defaultAdminStateName: defaultAdminStateName,
			
			defaultAdminSecurityState: defaultAdminSecurityState,			
			
			"main.menu": {
			        tabs: [
				        {
	                 	   state: $securityConfig.config.homeState, 
	                 	   name: 'Home',
	                 	   permission: 'authenticated'
	                    }, 
	                    
	                    {
	                 	   state: 'skyglass.tabs', 
	                 	   name: 'Tabs',
	                 	   permission: 'authenticated'
	                    }, 
	                    
	                    {
	                 	   state: 'skyglass.content', 
	                 	   name: 'Content',
	                 	   permission: 'authenticated'
	                    },
	                 	   
	                    {
	             		   state: 'skyglass.users.list', 
	             		   name: 'Users',
	             		   permission: 'authenticated'
	             	   	},
	                 		   
	                    {
	             		   state: 'skyglass.message', 
	             		   name: 'Message',
	             		   permission: 'authenticated'                    		   
	             	   	},
	                 			   
	                    {
	             		   actionClick: function() {
	             			   $state.go(defaultAdminState());
	             		   }, 
	             		   name: 'Admin',
	             		   permission: 'admin'                    		   
	             	   	},
	             	   
	                    {
	             		   state: $securityConfig.config.loginState, 
	             		   name: 'Login',
	             		   permission: 'authenticated',
	             		   permissionMode: 'not'
	             	   	}, 
	             	   
	                    {
	             		   actionClick: function() {
	             			   $securityService.logout(function() {
	             			        $rootScope.authorizeMainMenu();
	             			   });
	             		   }, 
	             		   name: 'Logout',
	             		   permission: 'authenticated'
	                    }
			        ]
			},
			
			"release.menu": {
			       tabs: [
		               {
		             	   state: 'skyglass.release.categories', 
		             	   name: 'Categories',
		             	   permission: 'authenticated'
		               },
		               
		               {
		             	   state: 'skyglass.release.publishers', 
		             	   name: 'Publishers',
		             	   permission: 'authenticated'
		               },
		               
		               {
		             	   state: 'skyglass.release.subscribers', 
		             	   name: 'Subscribers',
		             	   permission: 'authenticated'
		               },
		               
		               {
		             	   state: 'skyglass.release.releases', 
		             	   name: 'Releases',
		             	   permission: 'authenticated'
		               },
		               
		               {
		             	   state: 'skyglass.release.subscriptions', 
		             	   name: 'Subscriptions',
		             	   permission: 'authenticated'
		               }			               
			       ]
			},				
			
			"admin.menu": {
			       tabs: [
					   {
		              	   stateFunction: function() {
		              		   return defaultAdminState();
		              	   },
		             	   stateNameFunction: function() {
		             		   return defaultAdminStateName();
		             	   },
		             	   permission: 'admin'
					   }, 
	                
		               {
		             	   state: 'skyglass.admin.changes', 
		             	   name: 'Changes',
		             	   permission: 'adminAuditor'
		               },
		               
		               {
		             	   state: defaultAdminSecurityState(), 
		             	   name: 'Security',
		             	   permission: 'adminSecurity'
		               }
			       ]
			},	
			
			"security.menu": {
			       tabs: [
					   {
		             	   value: 'skyglass.admin.security.users', 
		             	   label: 'Users'
					   },
	                
		               {
		             	   value: 'skyglass.admin.security.roles', 
		             	   label: 'Roles'
		               },
		               
		               {
		             	   value: 'skyglass.admin.security.tokens', 
		             	   label: 'Tokens'
		               }	
			       ]
			},				
			
			"tabs.menu": {
			       tabs: [
				   {
	              	   state: 'skyglass.tabs.first', 
	             	   name: 'Menu Tab',
	             	   permission: 'authenticated'
				   }, 
                
	               {
	             	   state: 'skyglass.tabs.second.content', 
	             	   name: 'Content Tab',
	             	   permission: ['adminAuditor', 'adminWriter'],
	             	   permissionMode: 'and'
	               } 
			     ]
			}
		}
    }]
});
