define(['angular', 'services'], function(angular, services) {
    return ['$timeout',
        function($timeout) {

            return function(func, wait, immediate) {
                
                var timeout, args, context, result;

                function debounce() {
                    context = this;
                    args = arguments;
                    var later = function() {
                        timeout = null;
                        if (!immediate) {
                            result = func.apply(context, args);
                        }
                    };
                    var callNow = immediate && !timeout;
                    if (timeout) {
                        $timeout.cancel(timeout);
                    }
                    timeout = $timeout(later, wait);
                    if (callNow) {
                        result = func.apply(context, args);
                    }
                    return result;
                };

                debounce.cancel = function() {
                    $timeout.cancel(timeout);
                    timeout = null;
                };

                return debounce;

            };
        }
    ];
});
