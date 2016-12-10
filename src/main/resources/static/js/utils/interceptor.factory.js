define(['angular', 'services'], function(angular, services) {
    return [
    	'$q', '$injector', '$location',
    	function($q, $injector, $location){

		    return {
	            request: function (config) {
	                return config;
	            },
		        response: function(response){
		            return response || $q.when(response);
		        },
		        responseError: function(rejection) {
		            if (rejection.status === 403) {
		            	$injector.get('$state').go("skyglass.permissions");
	                    $location.replace();
		            } 
		            return $q.reject(rejection);
		        }
		    }
		}
	]
});