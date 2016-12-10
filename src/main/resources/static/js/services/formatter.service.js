define(['angular', 'services', 'moment'], function(angular, services, moment) {
    services.service("$formatter", ["$window", "$translate",
        function($window, $translate) {

        	var labels = {
        		label1: "Application",
        		label2: "Component"
        	};

            return {



                parseUrl: function(search){
                    var ret = {};
                    search.substr(1).split("&")
                    .forEach(function (item) {
                        var arr = item.split("=");
                        ret[arr[0]] = arr[1]
                    });
                    return ret;
                },



                error: function (error, regExp, replace) {
                    if (typeof(error) == "string") {
                        var formattedError = error.replace(/(\r\n|\n|\r)/gm, "");
                        if (regExp && replace) {
                            var escapedRegExp = regExp.replace(/[.*+?^${}()|[\]\\]/g, "\\$&");
                            var re = new RegExp(escapedRegExp, 'g');
                            return formattedError.replace(re, replace);
                        } else {
                            return formattedError;
                        }
                    }
                },



                randomString: function(length) {
                    var chars = ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"],
                        result = "";
                    for (var i = 0; i < length; i++) {
                        result += chars[Math.floor(Math.random() * chars.length)];
                    }
                    return result;
                },



                toCamelCase: function(str){
                    return str
                        .replace(/\s(.)/g, function($1) { return $1.toUpperCase(); })
                        .replace(/\s/g, '')
                        .replace(/^(.)/, function($1) { return $1.toLowerCase(); });
                },



                durationFormatter: function(value) {
                    var time = moment.duration(value);
                    var hh = (time.hours() + time.days() * 24).toString(),
                        mm = time.minutes().toString(),
                        ss = time.seconds().toString(),
                        val = "";
                    hh[1] ? val += hh : val = val + "0" + hh;
                    val += ":";
                    mm[1] ? val += mm : val = val + "0" + mm;
                    val += ":";
                    ss[1] ? val += ss : val = val + "0" + ss;
                    return val;
                },



                formatNumberForFileSize: function(size) {
                    if (size < 1) {
                        return (Number(size)).toFixed(1);
                    } else if (size >= 99.5 && size < 100) {
                        return 100;
                    } else if (size < 100) {
                        return (Number(size)).toPrecision(2);
                    }
                    return size;
                },



                fileSizeFormat: function(size) {
                    var sizeBytes = parseFloat(size),
                        sizeKB = (sizeBytes/1024).toPrecision(3),
                        sizeMB = (sizeKB/1024).toPrecision(3),
                        sizeGB = (sizeMB/1024).toPrecision(3),
                        sizeTB = (sizeGB/1024).toFixed(1);

                    if (sizeBytes < 103) {
                        return sizeBytes+" bytes";
                    } else if (sizeMB < 1) {
                        return this.formatNumberForFileSize(sizeKB) + " Kb";
                    } else if (sizeGB < 1) {
                        return this.formatNumberForFileSize(sizeMB) + " Mb";
                    } else if (sizeTB < 1) {
                        return this.formatNumberForFileSize(sizeGB) + " Gb";
                    } else {
                        return sizeTB + " Tb";
                    }
                },



                combineDateAndTime: function(date, time){
                    var startYear = date.getFullYear(),
                        startMonth = date.getMonth(),
                        startDay = date.getDate(),
                        startHours = time.getHours(),
                        startMinutes = time.getMinutes();
                    return new Date(startYear, startMonth, startDay, startHours, startMinutes).getTime();
                },



                resourceUrl: function(obj, filter){

                    var res = {};

                    for(var key in obj){
                        if (key == "page") {
                            res.pageNumber = obj[key];
                        } else if (key == "count") {
                            res.rowsPerPage = obj[key];
                        } else if (key.indexOf("sorting") != -1){
                            res.orderField = key.match(/[^[\]]+(?=])/g)[0];
                            res.sortType = obj[key];
                        } else if (key.indexOf("filter") != -1) {
                            if (!res.hasOwnProperty("filterFields")) {
                                res.filterFields = [];
                            }
                            var field = key.match(/[^[\]]+(?=])/g)[0];
                            res.filterFields.push(field);
                            res["filterValue_" + field] = obj[key];
                            res["filterType_" + field] = filter.filterType ? filter.filterType : "like";
                            res["filterClass_" + field] = "String";
                            res["filterClass_" + field] = filter.filterClass ? filter.filterClass : "String";
                        }
                    }
                    delete filter.filterType;
                    delete filter.filterClass;

                    for(var key in filter) {
                        res[key] = filter[key];
                    }
                    return res;
                },



        		resourceParentName: function(item){
        			if (item.agent) {
                        if (item.agent.security.read) {
                            return item.agent.name;
                        } else {
                            return item.agent.id;
                        }
            		} else if (item.agentPool) {
                        if (item.agentPool.security.read) {
                            return item.agentPool.name
                        } else {
                            return item.agentPool.id
                        }
            		} else if (item.parent) {
                        if (item.parent.security.read) {
                            return item.parent.name
                        } else {
                            return item.parent.id
                        }
                    } else {
            			return "";
            		}
        		},
                resourceTypeFormatter: function (item) {
                    if (item.resourceGroup) {
                        return "Resource Group";
                    } else {
                        return "Resource";
                    }
                },



                changeHistoryFormatter: function(item, friendlyPathsStaticNames){
                    var result = [],
                        self = this;

                    angular.forEach(item.friendlyPathsModified, function(pathObject){
                        var flag = (pathObject.persistent && pathObject.persistent.className === "PropSheet");
                        result.push({
                            text: self.changeHistoryCreatePathString(pathObject.path, friendlyPathsStaticNames) + " " + "v." + pathObject.version,
                            changes: (flag ? true : false),
                            path: ((flag ? pathObject.persistent.path : ""))
                        })
                    });

                    angular.forEach(item.friendlyPathsDeleted, function(pathObject){
                        result.push({
                            text: "Deleted " + self.changeHistoryCreatePathString(pathObject.path, friendlyPathsStaticNames),
                            changes: false
                        });
                    });

                    return result;
                },
                changeHistoryCreatePathString: function (pathElements, pathStaticNames) {
                    var arr = [];
                    angular.forEach(pathElements, function(item){
                        if (pathStaticNames.indexOf(item) > -1) {
                            arr.push(item);
                        } else {
                            arr.push(item);
                        }
                    });
                    return arr.join("/");
                },



                requestStatusFormatter: function(item){
                	if (item.error) {
                        return "COULD_NOT_START";
                    } else if (item.failed) {
                        return "FAILED";
                    } else if (item.rootTrace) {
                        return this.activityStatusFormatter(item.rootTrace);
                    } else if (item.approval) {
                        return this.approvalStatusFormatter(item.approval);
                    } else if (item.entry) {
                        return "SCHEDULED";
                    } else {
                        return "UNKNOWN";
                    }
                },
                getNamedProperty: function(array, name) {
                    var result;
                    angular.forEach(array, function(item) {
                        if (item.name === name) {
                            result = item;
                        }
                    });
                    return result;
                },
                activityStatusFormatter: function(item, value){
                    var status = item.state;
                    var result = item.result;
                    var failureCaught = this.getNamedProperty(item.properties, "_SYS_FAILURE_CAUGHT");
                    if (failureCaught !== undefined && failureCaught.value === "true") {
                        result = "FAULTED";
                    } if (status === "CLOSED" && result === "SUCCEEDED") {
                        if (item.metadata && item.metadata.notNeeded) {
                            return "NOT_NEEDED";
                        }
                        else {
                            return "SUCCESS";
                        }
                    } else if (status === "CLOSED" && result === "CANCELED") {
                        return "CANCELED";
                    } else if (status === "EXECUTING") {
                        if (item.paused) {
                            return "PAUSED";
                        }
                        else {
                            return "RUNNING";
                        }
                    } else if ( (status === "CLOSED" && result === "FAULTED") || status === "FAULTING") {
                        return "FAILURE";
                    } else if (status === "INITIALIZED" && result === "NONE") {
                        return "WAITING_TO_START"
                    }
                },
                approvalStatusFormatter: function(item) {
                    if (item.failed) {
                        return "APPROVAL_FAILED";
                    } else if (item.finished) {
                        return "APPROVED";
                    } else if (item.cancelled) {
                        return "APPROVAL_CANCELLED";
                    } else {
                        return "APPROVAL_IN_PROGRESS";
                    }
                },



                processTableChildren: function(arr, depth){
                    var self = this;
                    angular.forEach(arr, function(item){
                        item.depth = depth;
                        item.parentName = self.resourceParentName(item);
                        item.childrenList = item.children;
                        item.globalParentId = item.id;
                        angular.forEach(item.childrenList, function(child){
                            child.depth = item.depth + 1;
                        });
                    });
                    return arr;
                },



                processImpersonationType: function(data, defValue){
                    if (data.impersonationUser){
                        if(data.impersonationUseSsh){
                            return "SSH";
                        } else if(data.impersonationUseSudo) {
                            return "SUDO";
                        } else {
                            return "SU";
                        }
                    }
                    return defValue;
                },



                componentProcessType: function(process){
                    if (process.takesVersion) {
                        if (process.inventoryActionType == "ADD") {
                            return "deploy";
                        }
                        else if (process.inventoryActionType == "REMOVE") {
                            return "uninstall";
                        }
                        else {
                            return "operational_version";
                        }
                    } else if (process.configActionType == "ADD") {
                        return "config_deploy";
                    } else {
                        return "operational";
                    }
                },

                adjustParams: function(params){
                    if(params.total() > 0){
                        var diff = params.page() * params.count() - params.total();
                        if(diff > 0){
                            params.page(Math.ceil(params.total() / params.count()));
                        }
                    }
                },

                customPagination: function(params, data, filter){
                    var res = {};
                    var sliceStart = (params.page() - 1) * params.count();
                    var sliceEnd = params.page() * params.count();
                    res.filteredData = params.filter() ? filter('filter')(data, params.filter()) : data;
                    res.orderedData = params.sorting() ? filter('orderBy')(res.filteredData, params.orderBy()) : res.filteredData;
                    var filterParams = [res.orderedData];
                    Array.prototype.push.apply(filterParams, Array.prototype.slice.call(arguments, 4, arguments.length));
                    var func = filter(arguments[3]);
                    var customFiltered = func.apply(func, filterParams);
                    params.total(customFiltered.length);
                    res.pagedData = customFiltered.slice(sliceStart, sliceEnd);
                    return res;
                },

                logStatus: function(activity){
                    var status = activity.state,
                        result = activity.result;

                    activity.failureCaught ? result = "FAULTED" : result = activity.result;

                    if (status == "CLOSED" && result == "SUCCEEDED") {
                        if (activity.notNeeded) {
                            if (activity.notNeeded === "noResources") {
                                return "Not_Mapped";
                            } else if (activity.notNeeded === "noVersionsSelected") {
                                return "No_Version_Selected";
                            } else if (activity.notNeeded === "noVersionsNeeded") {
                                return "Already_Installed";
                            } else if (activity.notNeeded === "noConfigurationChanged") {
                                return "Configuration_Not_Changed";
                            } else if (activity.notNeeded === "offline") {
                                return "Resource_Offline";
                            } else if (activity.notNeeded === "roleNotFound") {
                                return "Task_Role_Deleted";
                            } else if (activity.notNeeded === "componentNotInApp") {
                                return "Component_Not_Found";
                            } else if (activity.notNeeded === "precondition") {
                                return "Precondition_Failed";
                            } else if (activity.notNeeded === "noComponents") {
                                return "No_Components";
                            }
                        } else {
                            return "Success";
                        }
                    } else if (status == "CLOSED" && result == "CANCELED") {
                        return "Canceled";
                    } else if (status == "EXECUTING") {
                        if (activity.paused) {
                            return "Paused";
                        } else {
                            return "Running";
                        }
                    } else if ((status == "CLOSED" && result == "FAULTED") || status == "FAULTING") {
                        return "Failed";
                    } else if (status === "INITIALIZED") {
                        return "Not_Started";
                    }
                },

                buildVersionsArray : function(numberOfVersions){
                    var versions = [];
                    for (var i = 1; i <= numberOfVersions; i++) {
                        versions.push({
                            id: i,
                            label: $translate.instant("applicationDetails.labels.label4", {version: i, total: numberOfVersions})
                        })
                    }
                    return versions;
                },

                formatDate : function(date){
                    return moment(date).format('MM/DD/YYYY hh:mm A');
                },

                initiateAndValuesArr: function(records){
                    var andValues = [];
                    angular.forEach(records, function(record){
                        var arr = [];
                        angular.forEach(record, function(item){
                            arr.push("And");
                        });
                        andValues.push(arr);
                    });
                    return andValues;
                },

                convertItemValue: function(item){
                    if (item.value === "true") {
                        return true;
                    } else if(item.value === "false") {
                        return false;
                    } else if(item.value && item.type == "MULTI_SELECT"){
                        if(item.value.indexOf("[") == 0){
                            return JSON.parse(item.value);
                        } else {
                            return item.value.split(",");
                        }
                    }
                    return item.value;
                },

                evalProcessDuration: function(status, rootTrace){
                    if (rootTrace){
                        if(status == "WAITING_TO_START" || status == "UNKNOWN"){
                            return 0;
                        }
                        return rootTrace.duration;
                    }
                    return 0;
                },

                getPropPath: function (id, param) {
                    if (param === "environmentPropSheetDef") {
                        return "components/" + id + "/environmentPropSheetDef";
                    }
                }

            }
        }
    ]);
});