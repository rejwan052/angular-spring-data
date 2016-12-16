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
                    subscriptions: {
                        method: "GET",
                        url: baseUrl + "rest/release/subscription",
                        isArray: true
                    },
                    saveSubscription: {
                        method: "POST",
                        url: baseUrl + "rest/release/subscription"
                    },
                    deleteSubscription: {
                        method: "DELETE",
                        url: baseUrl + "rest/release/subscription/:id"
                    }                  
                }
            );
        }
    ];
});


