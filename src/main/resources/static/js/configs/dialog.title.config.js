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
            }

        });

    }]
});
