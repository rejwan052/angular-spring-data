define(['angular', 'services'], function(angular, services) {
    services.filter('Include', function() {
        return function(collection, array, by) {
            var output = [];
            angular.forEach(collection, function(item) {
                if (array && array.indexOf(item[by]) != -1) {
                    output.push(item);
                };
            });
            return output;
        };
    })
    .filter('Exclude', function() {
        return function(collection, array, by) {
            var output = [];
            angular.forEach(collection, function(item) {
                if (array && array.indexOf(item[by]) == -1) {
                    output.push(item);
                };
            });
            return output;
        };
    });
});