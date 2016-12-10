define(['angular', 'services', 'jquery', 'bootstrap'], function(angular, services, jquery, bootstrap) {
    services.service("$message", ["$window", "$compile", "$templateCache", "$rootScope",
        function($window, $compile, $templateCache, $rootScope) {

            var openMessage = function(type, text, noTimeout){

                var scope = $rootScope.$new(true);

                scope.type = type;
                scope.text = text;

                var html = $compile($templateCache.get("js/templates/message/message.html"))(scope);

                var el = $(html).alert();

                $("body").append(el);

                if (!noTimeout) {
                    var timeout = setTimeout(function(){
                        el.alert('close');
                    }, 3000);
                };
                
                el.on("close.bs.alert", function(){
                    if (!noTimeout) {
                        clearTimeout(timeout);
                    };
                });

            };

            return function(type, text, noTimeout){
                openMessage(type, text, noTimeout);
            };

        }
    ]);
});
