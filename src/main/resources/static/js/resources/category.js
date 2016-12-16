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
                    }                    
                }
            );
        }
    ];
});


