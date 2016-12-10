define(['angular', 'showdown'], function (angular, showdown) {
    return angular.module('skyglass.filters', [])
        .filter('include', function() {
            return function(collection, array, property) {
                var output = [],
                    by = property || "id"; 
                angular.forEach(collection, function(item) {
                    if (array && array.indexOf(item[by]) != -1) {
                        output.push(item);
                    };
                });
                return output;
            };
        })
        .filter('exclude', function() {
            return function(collection, array, property) {
                var output = [],
                    by = property || "id"; 
                angular.forEach(collection, function(item) {
                    if (array && array.indexOf(item[by]) == -1) {
                        output.push(item);
                    };
                });
                return output;
            };
        }).filter('filterByFields', function() {
            return function(collection, fields, val) {
                var output = [];
                angular.forEach(collection, function(item) {
                    var arr = [];
                    angular.forEach(fields, function(field){
                        try{
                            if (eval("item." + field).toString().toLowerCase().indexOf(val.toLowerCase()) != -1) {
                                arr.push(true);
                            };
                        } catch (err){}
                    });
                    if (arr.indexOf(true) != -1) {
                        output.push(item);
                    };
                });
                return output;
            };
        })
	    .filter('checkmark', function() {
	  	  return function(input) {
	  	    return input ? '\u2713' : '\u2718';
	  	  };
	  	})
		.filter("markdown", function($sce) {
			var converter = new showdown.Converter();
			return function(value) {
				var html = converter.makeHtml(value || '');
		        return $sce.trustAsHtml(html);
			};
		});	    
});
