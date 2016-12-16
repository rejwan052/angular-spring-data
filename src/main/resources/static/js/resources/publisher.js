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
                    publishers: {
                        method: "GET",
                        url: baseUrl + "rest/release/publisher",
                        isArray: true
                    },
                    savePublisher: {
                        method: "POST",
                        url: baseUrl + "rest/release/publisher"
                    },
                    deletePublisher: {
                        method: "DELETE",
                        url: baseUrl + "rest/release/publisher/:id"
                    },
                    publisherReleases: {
                        method: "GET",
                        url: baseUrl + "rest/release/publisher/releases/:id",
                        isArray: true
                    }                    
                }
            );
        }
    ];
});


