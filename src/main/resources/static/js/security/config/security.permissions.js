define(['angular'], function(angular) {
    return [function(){
    	
	    var USER_ROLES = {
	    		ADMIN: 'ADMIN',
	    		WRITER: 'WRITER',
	    		SECURITY: 'SECURITY',
	    		SECURITY_WRITER: 'SECURITY_WRITER'
	    };	
	    
        
		var PERMISSION_SESSION = {	
					user: {},
		
					invalidate : function() {
						PERMISSION_SESSION.user = {};	
					},	
					
					initData : function(authData) {
						PERMISSION_SESSION.user.roles = authData.roles;
					},
					
					authenticated : function() {
						return (PERMISSION_SESSION.user.roles) ? true : false
					},
					
					hasRole : function(role) {
						return PERMISSION_SESSION.user.roles.indexOf(role) > -1;
					}
		};		    
	    
	    var PERMISSIONS = {
			authenticated: PERMISSION_SESSION.authenticated,	
    		
			admin: function() {
				return hasRole(USER_ROLES.ADMIN);
			},
			
			adminAuditor: function() {
				return hasRole(USER_ROLES.ADMIN);
			},
			
			adminReader: function() {
				return hasRole(USER_ROLES.ADMIN) && !hasRole(USER_ROLES.WRITER);
			},
			
			adminWriter: function() {
				return hasRoles([USER_ROLES.ADMIN, USER_ROLES.WRITER]);
			},
			
			adminSecurity: function() {
				return hasRoles([USER_ROLES.ADMIN, USER_ROLES.SECURITY]);
			},
			
			securityWriter: function() {
				return hasRoles([USER_ROLES.ADMIN, USER_ROLES.SECURITY, USER_ROLES.SECURITY_WRITER]);
			},
			
			initData : PERMISSION_SESSION.initData,
			
			invalidate : PERMISSION_SESSION.invalidate			
			
			
		};		
		
		var hasRole = function(role) {
			if (!PERMISSION_SESSION.authenticated()) {
				return false;
			}
			return PERMISSION_SESSION.hasRole(role);		
		};
		
		var hasRoles = function(roles) {
			if (!PERMISSION_SESSION.authenticated()) {
				return false;
			}
			for (var i = 0; i < roles.length; i++) {
				if (!PERMISSION_SESSION.hasRole(roles[i])) {
					return false;
				}
			}
			return true;
		};	
		
		var hasOneRole = function(roles) {
			if (!PERMISSION_SESSION.authenticated()) {
				return false;
			}
			for (var i = 0; i < roles.length; i++) {
				if (PERMISSION_SESSION.hasRole(roles[i])) {
					return true;
				}
			}
			return false;
		};
		
		var get = [function() {			
			return {
			}		
		}];

		this.$get = get;	
		
		this.Permissions = PERMISSIONS;
	
    }]
});
