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
                    releases: {
                        method: "GET",
                        url: baseUrl + "rest/release/release",
                        isArray: true
                    },
                    saveRelease: {
                        method: "POST",
                        url: baseUrl + "rest/release/release"
                    },
                    deleteRelease: {
                        method: "DELETE",
                        url: baseUrl + "rest/release/:id"
                    },
                    publishRelease: {
                        method: "POST",
                        url: baseUrl + "rest/release/publish"
                    },   
                    unpublishRelease: {
                        method: "DELETE",
                        url: baseUrl + "rest/release/unpublish/:id"
                    }                    
                }
            );
        }
    ];
});


