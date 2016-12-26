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
                    notSubscribers: {
                        method: "GET",
                        url: baseUrl + "rest/security/release/user/notSubscribers"
                    },                 	
                    subscribers: {
                        method: "GET",
                        url: baseUrl + "rest/release/subscriber",
                        isArray: true
                    },
                    saveSubscriber: {
                        method: "POST",
                        url: baseUrl + "rest/release/subscriber"
                    },
                    deleteSubscriber: {
                        method: "DELETE",
                        url: baseUrl + "rest/release/subscriber/:id"
                    },
                    subscriberSubscriptions: {
                        method: "GET",
                        url: baseUrl + "rest/release/subscriber/subscriptions/:id",
                        isArray: true
                    }                   
                }
            );
        }
    ];
});


