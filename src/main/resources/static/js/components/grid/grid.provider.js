define(['angular'], function (angular, services) {
	return [function(){

		var configurations = {};

		var get = [function(){
			return {
				get: function(prop){
					if (prop && configurations.hasOwnProperty(prop)) {
						return configurations[prop]
					} else {
						return configurations;
					}
				}
			}
		}];

		this.$get = get;

		this.config = function(value, obj){
            configurations[value] = obj;
        };

	}]
});
