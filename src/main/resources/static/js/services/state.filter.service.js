define(['angular', 'services'], function(angular, services) {
    services.service("$stateFilter", ["localStorageService", "$state",
        function(localStorageService, $state) {

        	function updateLocalStorage(data){
                localStorageService.set("stateFilter", data);
            };

            function getLocalStorage(){
            	var data = localStorageService.get("stateFilter");
            	if (data) {
            		return data
            	} else {
            		return {};
            	};
            };

            function save(id, f, type){
            	var data = getLocalStorage();
        		if (!data.hasOwnProperty(id)) {
        			data[id] = {};
        		};
        		data[id][type] = f;
        		updateLocalStorage(data);
            };

            if(localStorageService.isSupported){

                return {

                	getParams: function(id){
                		var data = getLocalStorage();
                		if (data.hasOwnProperty(id) && data[id].hasOwnProperty("params")) {
                			return data[id].params;
                		};
                		return false;
                	},

                	saveParams: function(id, params){
                		save(id, params, "params");
                	},

                	getFilter: function(filter, extra, view){
                		var name = $state.current.name,
                    		id = view ? name += "." + view : name,
                    		data = getLocalStorage();
                    	if (data.hasOwnProperty(id) && data[id].hasOwnProperty("filter")) {
                    		if (extra) {
                    			return angular.extend({}, extra, data[id].filter);
                    		} else {
                    			return data[id].filter;
                    		}
                    	} else {
                    		this.saveFilter(filter, view);
                    		if (extra) {
                    			return angular.extend({}, extra, filter);
                    		} else {
                    			return filter;
                    		}
                    	};
                	},

                	saveFilter: function(filter, view){
                		var name = $state.current.name,
                    		id = view ? name += "." + view : name;
                    	save(id, filter, "filter");
                	},

                	saveFilterProp: function(prop, val, view){
                		var name = $state.current.name,
                    		id = view ? name += "." + view : name,
                    		data = getLocalStorage();
                    	if (data.hasOwnProperty(id) && data[id].hasOwnProperty("filter") && data[id].filter.hasOwnProperty(prop)) {
                    		data[id].filter[prop] = val;
                    		updateLocalStorage(data);
                    	};
                	},

                	getStatuses: function(view){
                		var name = $state.current.name,
                    		id = view ? name += "." + view : name,
                    		data = getLocalStorage(),
                    		statuses = {};
                    	if (data.hasOwnProperty(id) && data[id].hasOwnProperty("filter") && data[id].filter.hasOwnProperty("status")) {
                    		angular.forEach(data[id].filter.status, function(item){
                    			statuses[item.toLowerCase()] = true;
                    		});
                    	};
                    	return statuses;

                	}

                }

            } else {

                return {
                    add: function(){
                        $log.warn("Local Storage is not supported in your browser!");
                    },
                    get: function(){
                        $log.warn("Local Storage is not supported in your browser!");
                    },
                    remove: function(){
                        $log.warn("Local Storage is not supported in your browser!");
                    },
                    clear: function(){
                        $log.warn("Local Storage is not supported in your browser!");
                    }
                }

            }

        }
    ]);
});
