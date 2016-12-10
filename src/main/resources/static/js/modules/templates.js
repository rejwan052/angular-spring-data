angular.module('skyglass.templates', []).run(['$templateCache', '$http', '$q',
                                         function($templateCache, $http, $q) {
    $http({
		method: "GET",
        url: "js/templates/message/message.html"
    }).then(function(data){
    	$templateCache.put("js/templates/message/message.html", data.data)
    });

}]);
