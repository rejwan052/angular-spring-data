define(['angular'], function(angular) {
    return [function() {

        var config = {}
        
        this.$get = [function(){
            return {
            	config: function(){
            		return config;
            	}
            }
        }];

        this.config = function(obj){
            config = obj;
        };

    }];
});