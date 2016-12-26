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
                    notPublishers: {
                        method: "GET",
                        url: baseUrl + "rest/security/release/user/notPublishers"
                    },               	
                    publishers: {
                        method: "GET",
                        url: baseUrl + "rest/release/publisher"
                    },
                    savePublisher: {
                        method: "POST",
                        url: baseUrl + "rest/release/publisher"
                    },
                    savePublishers: {
                        method: "POST",
                        url: baseUrl + "rest/release/publisher/saveAll"
                    },                    
                    deletePublisher: {
                        method: "DELETE",
                        url: baseUrl + "rest/release/publisher/:id"
                    },
                    publisherReleases: {
                        method: "GET",
                        url: baseUrl + "rest/release/publisher/releases/:id"
                    }                    
                }
            );
        }
    ];
});


