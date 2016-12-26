define(function() {
    return ["$resource", '$securityConfig',
        function($resource, $securityConfig) {
    	
    		var baseUrl = $securityConfig.config.baseUrl;
    	
            return $resource(
                baseUrl,
                {
                    id: "@id"
                },
                {
                    roles: {
                        method: "GET",
                        url: baseUrl + "rest/security/role",
                        isArray: true
                    },
                    saveRole: {
                        method: "POST",
                        url: baseUrl + "rest/security/role"
                    },
                    deleteRole: {
                        method: "DELETE",
                        url: baseUrl + "rest/security/role/:id"
                    },
                    users: {
                        method: "GET",
                        url: baseUrl + "rest/security/user",
                        isArray: true
                    },
                    saveUser: {
                        method: "POST",
                        url: baseUrl + "rest/security/user"
                    },
                    setUserPassword: {
                        method: "POST",
                        url: baseUrl + "rest/security/user/:id/setPassword"
                    },
                    getUserRoles: {
                        method: "GET",
                        url: baseUrl + "rest/security/user/:id/roles",
                        isArray: true
                    },
                    setUserRoles: {
                        method: "POST",
                        url: baseUrl + "rest/security/user/:id/setRoles"
                    },                    
                    deleteUser: {
                        method: "DELETE",
                        url: baseUrl + "rest/security/user/:id"
                    },
                    tokens: {
                        method: "GET",
                        url: baseUrl + "rest/security/token",
                        isArray: true
                    },
                    deleteToken: {
                        method: "DELETE",
                        url: baseUrl + "rest/security/token/:id"
                    }
                }
            );
        }
    ];
});


