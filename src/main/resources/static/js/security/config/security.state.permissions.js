define(['angular'], function(angular) {
    return ['Permission', '$securitySession',
        function(Permission, $securitySession) {
    	
    		var setPermissions = function() {
    			
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
    	        
    	        Permission.defineRole('adminAuditorWriter', function(stateParams) {
    	            return $securitySession.permissions.adminAuditor()
    	            		&& $securitySession.permissions.adminWriter();
    	        });             
    	        
    	        Permission.defineRole('adminSecurity', function(stateParams) {
    	            return $securitySession.permissions.adminSecurity();
    	        });       			
    		}
    		
    		
    		return {
    			
    			setPermissions: setPermissions   			

    		}    	

    }]
});
