define([], function() {
    return ["$dialogProvider", function($dialogProvider){

    	$dialogProvider.config("titles", {

            securityUser: function(params){
                if (!!params.item) {
                    return 'Edit User'
                } else {
                    return 'Create New User'
                }
            },

            resetUserPassword: function(params){
                if (!!params.item) {
                    return "Reset User Password: " + params.item.name
                } else {
                    return "Reset User Password"
                }
            },

            securityRole: function(params){
                if (!!params.item) {
                    return 'Edit Role'
                } else {
                    return 'Create Role'
                }
            },
            
            userRoles: function(params){
                return "Manage User Roles"
            },
            
            saveCategory: function(params){
                if (!!params.item) {
                    return 'Edit Category'
                } else {
                    return 'Create Category'
                }
            },
            
            saveSubscriber: function(params){
                if (!!params.item) {
                    return 'Edit Subscriber'
                } else {
                    return 'Create Subscriber'
                }
            },
            
            savePublisher: function(params){
                if (!!params.item) {
                    return 'Edit Publisher'
                } else {
                    return 'Create Publisher'
                }
            }            

        });

    }]
});
