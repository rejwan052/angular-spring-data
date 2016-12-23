define(['angular'], function(angular, services) {
    return ["ngTableParams", "$q", "$filter", "$debounce", "$translate", "localStorageService",
        function(ngTableParams, $q, $filter, $debounce, $translate, localStorageService) {

            var getUrlParams = function(obj, filter){
                var res = {};
                for(var key in obj){
                    if (key == "page") {
                        res.pageNumber = obj[key];
                    } else if (key == "count") {
                        res.rowsPerPage = obj[key];
                    } else if (key.indexOf("sorting") != -1){
                        res.orderField = key.match(/[^[\]]+(?=])/g)[0];
                        res.sortType = obj[key];
                    } 
                };
                for(var key in filter) {
                    res[key] = filter[key];
                };
                return res;
            };

            var saveFilter = function(id, filter, type) {
                var data = localStorageService.get("gridFilter");
                if (!data) {
                    data = {};
                };
                if (!data.hasOwnProperty(id)) {
                    data[id] = {};
                };
                data[id][type] = filter;
                localStorageService.set("gridFilter", data);
            };

            var getFilter = function(id, filter, extra) {
                var data = localStorageService.get("gridFilter");
                if (data && data.hasOwnProperty(id) && data[id].hasOwnProperty("filter")) {
                    return angular.extend({}, extra, data[id].filter);
                } else {
                    saveFilter(id, filter, "filter");
                    return angular.extend({}, extra, filter);
                };
            };

            var getParams = function(id, params) {
                var data = localStorageService.get("gridFilter");
                if (data && data.hasOwnProperty(id) && data[id].hasOwnProperty("params")) {
                    return data[id].params;
                } else {
                    saveFilter(id, params, "params");
                    return params;
                }
            };

            var Data = function(data, params, filter){

                var data = data,
                    total = 0,
                    self = this;

                this.data = function(){
                    return $q.when({
                        totalRecords: total,
                        records: data
                    });
                };

                this.filter = function(){
                    if (params.filter()) {
                        data = $filter('filter')(data, params.filter());
                        total = data.length;
                    };
                    return this;
                    
                };

                this.filterByFields = function(fields){
                    if (filter && filter.hasOwnProperty("searchQuery")) {
                        data = $filter('filterByFields')(data, fields, filter.searchQuery);
                        total = data.length;
                    };
                    return this;
                };

                this.order = function(){
                    if (params.sorting()) {
                        data = $filter('orderBy')(data, params.orderBy());
                        total = data.length;
                    };
                    return this;
                }

                this.exclude = function(exclude){
                    if (exclude) {
                        data = $filter('exclude')(data, exclude);
                        total = data.length;
                    };
                    return this;
                };

                this.slice = function(page){
                    var page = page ? page : params.page(),
                        start = (page - 1) * params.count(),
                        end = page * params.count();
                    var tmp = data.slice(start, end);
                    if (data.length && tmp.length) {
                        data = tmp;
                        return self;
                    } else if (data.length && !tmp.length) {
                        params.$params.page = page - 1;
                        return this.slice(page - 1)
                    } else if (!data.length && !tmp.length) {
                        return self;
                    }
                };

            };

            return function(obj){

                var self = this,
                    filter = obj.filter,
                    exfilter = obj.exfilter,
                    id = obj.id;

                var filterdata = function(){
                    if (id) {
                        saveFilter(id, self.filter, "filter");
                    };
                    if (self.table.page() == 1) {
                        self.table.reload();
                    } else {
                        self.table.page(1);
                    };
                };

                this.data = obj.data;

                this.filter = (function(){
                    if (id) {
                        return getFilter(id, filter, exfilter || {});
                    } else {
                        return obj.filter;
                    }
                })();

                this.search = $debounce(filterdata, 500);

                this.filterdata = filterdata;

                this.reload = function(data){
                    if (data) {
                        self.data = data;
                    };
                    self.table.reload();
                };

                this.saveParams = function(){
                    saveFilter(id, self.table.parameters(), "params");
                };

                this.table = new ngTableParams((function(){
                    if (id) {
                        return getParams(id, obj.params);
                    } else {
                        return obj.params;
                    }
                })(), {
                    total: self.data ? self.data.length : 0,
                    getData: function(defer, params) {
                        var data = new Data(self.data, params, self.filter),
                            urlParams = getUrlParams(params.url(), self.filter || {});
                        obj.getData(data, urlParams).then(function(data){
                            params.total(data.totalRecords);
                            defer.resolve(data.records);
                        });
                    }
                });

            };
        }
    ];
});