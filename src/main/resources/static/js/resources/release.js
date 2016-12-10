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
                    categories: {
                        method: "GET",
                        url: baseUrl + "rest/release/category",
                        isArray: true
                    },
                    saveCategory: {
                        method: "POST",
                        url: baseUrl + "rest/release/category"
                    },
                    deleteCategory: {
                        method: "DELETE",
                        url: baseUrl + "rest/release/category/:id"
                    },
                    categorySubscriptions: {
                        method: "GET",
                        url: baseUrl + "rest/release/category/:id/subscription",
                        isArray: true
                    },
                    deleteCategorySubscription: {
                        method: "DELETE",
                        url: baseUrl + "rest/release/category/:id/subscription/:id"
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
                    },
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
                    },   
                    releases: {
                        method: "GET",
                        url: baseUrl + "rest/release",
                        isArray: true
                    },
                    saveRelease: {
                        method: "POST",
                        url: baseUrl + "rest/release"
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


