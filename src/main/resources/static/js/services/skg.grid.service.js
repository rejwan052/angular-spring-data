define(['angular', 'services', 'stateFilterService'], function(angular, services) {
    services.service("$skgGrid", ["ngTableParams", "$state", "$stateFilter",
        function(ngTableParams, $state, $stateFilter) {

            return function(params, settings, view){

                var name = $state.current.name,
                    id = view ? name += "." + view : name,
                    paramsFilter = $stateFilter.getParams(id);

                if (!paramsFilter) {
                    $stateFilter.saveParams(id, params);
                } else {
                    var params = paramsFilter;
                };
                
                var grid = new ngTableParams(params, settings);

                grid.id = id;

                grid.saveParams = function() {
                    $stateFilter.saveParams(this.id, this.parameters());
                };

                return grid;

            };

        }
    ]);
});
