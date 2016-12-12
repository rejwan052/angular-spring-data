define([
    'angular',
    'controllers',
    'directives',
    'filters',
    'services',
    'configurations',
    'templates',
    'thirdparty',
    'componentsModule',
    'resource',
    'utilsModule',
    'skgSecurity',    
    'route',
	'uirouter',
	'uiroutertabs',
	
    'messageService',
    'wizardService',
    
    'wizardConfig',
	
    'indexCtrl',
    'loginCtrl',
    'permissionsCtrl',
    
    'releaseCtrl',
	'categoryListCtrl',
	'categorySubscriptionsCtrl',
	'subscriptionsCtrl',
	'publicationsCtrl',
	'adminCtrl',
	'adminWriteCtrl',
	'adminReadCtrl',
	'adminChangesCtrl',
	'adminSecurityCtrl',
    'btnTitleDirective',
    
    'multipleDialogCtrl'
], function(angular) {

    return angular.module('skyglass', [
            'skyglass.controllers',
            'skyglass.directives',
            'skyglass.filters',
            'skyglass.services',
            'skyglass.configs',
            'skyglass.templates',
            'skyglass.thirdparty',	
            'skyglass.utils',  
            'skyglass.security',
            'skyglass.components', 

    		'ui.router',
    		'ui.bootstrap',
    		'ui.router.tabs',		
    		'angularUtils.directives.uiBreadcrumbs'
        ])
        .config(['$stateProvider', '$httpProvider', '$locationProvider', '$urlRouterProvider', 
                   '$securityConfigProvider', '$securityServiceProvider', '$windowProvider',
                   '$wizardProvider',
                   
            function($stateProvider, $httpProvider, $locationProvider, $urlRouterProvider, 
            		$securityConfigProvider, $securityServiceProvider, $windowProvider,
            		$wizardProvider) {
		            
					$securityServiceProvider.rememberMeAuthenticate($windowProvider.$get().location.pathname);

					$locationProvider.html5Mode(true);
					
					$httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
					
					$urlRouterProvider.otherwise(function($injector){
						  $injector.invoke(['$state', function($state) {
						    $state.go($securityConfigProvider.Config.homeState);
						  }]);
					}); 
					
	                $wizardProvider.set("addRelease");
	                $wizardProvider.set("addSubscription");
				
					$stateProvider
					
					   .state('skyglass', {
					        url: '/skyglass',
					        abstract: true
					    })
				        
				        .state($securityConfigProvider.Config.noPermissionsState, {
				            url: $securityConfigProvider.Config.noPermissionsPath,
				            views: {
				                "mainView@": {
				                    templateUrl: "js/app/permissions/permissions.html",
				                    controller : 'permissionsCtrl'
				                }
				            },
		                    data: {
		                    	displayName: 'No Permissions'      	
						    }				            
				        }) 

				    

				        
				        
					    .state($securityConfigProvider.Config.loginState, {
				            url: $securityConfigProvider.Config.loginPath,
				            views: {
				                "mainView@": {
									templateUrl : 'js/app/login/login.html',
									controller : 'loginCtrl',
				                }
				            },
						    data: {
						    	displayName: 'Login'
						    }
					    })
					    
					    .state('skyglass.release', {
						      url: '/release',
						      views: {
						        'mainView@': {
						          templateUrl: 'js/app/release/release.html',
						          controller: 'releaseCtrl'
						        }
						      },
			                  data: {
			                	  displayName: 'Release',
			                	  proxyLink: 'skyglass.release.category'
				                  permissions: {
			                    	  only: ['authenticated'],
			                    	  redirectTo: 'skyglass.permissions'
								  }		                	  
							  }
						})		
						

						
				        .state('skyglass.release.category', {
				            url: '/category',
				            abstract: true,
				            views: {
				                "release@skyglass.release": {
				                    abstract :true
				                }
				            },
						    data: {
						        proxy: 'skyglass.release.category.list'
						    }				        
				        })						    
				    
					    
					    .state('skyglass.release.category.list', {
					      url: '/list',
					      views: {
					        'release@skyglass.release': {
						       templateUrl: 'js/app/release/categories/category-list.html',
							   controller : 'categoryListCtrl',
					        }
					      },
					      data: {
						    displayName: 'Categories',
		                    permissions: {
	                    	  only: ['authenticated'],
	                    	  redirectTo: 'skyglass.permissions'
					        }							     
					      }
					    })
					    
					    .state('skyglass.release.category.subscriptions', {
					      url: '/:id/subscriptions',
					      views: {
					        'release@skyglass.release': {
							  templateUrl: 'js/app/release/categories/category-subscriptions.html',
							  controller : 'categorySubscriptionsCtrl'
					        }
					      },
					      data: {
					        displayName: 'Subscriptions: {{ categoryName | uppercase }}',
		                    permissions: {
	                    	  only: ['authenticated'],
	                    	  redirectTo: 'skyglass.permissions'
						    }						        
					      },
					      resolve: {
					        categoryName: function($stateParams, categoryService) {
					          return categoryService.getCategoryName($stateParams.id)
					        }
					      }
					    })							
						
				        .state('skyglass.release.publisher', {
				            url: '/publisher',
				            abstract: true,
				            views: {
				                "release@skyglass.release": {
				                    abstract :true
				                }
				            },
						    data: {
						        proxy: 'skyglass.release.publisher.list'
						    }				        
				        })						    
				    
					    
					    .state('skyglass.release.publisher.list', {
					      url: '/list',
					      views: {
					        'release@skyglass.release': {
						       templateUrl: 'js/app/release/publishers/publisher-list.html',
							   controller : 'publisherListCtrl',
					        }
					      },
					      data: {
						    displayName: 'Publishers',
		                    permissions: {
	                    	  only: ['authenticated'],
	                    	  redirectTo: 'skyglass.permissions'
					        }							     
					      }
					    })
					    
					    .state('skyglass.release.publisher.releases', {
					      url: '/:id/releases',
					      views: {
					        'release@skyglass.release': {
							  templateUrl: 'js/app/release/publishers/publisher-releases.html',
							  controller : 'publisherReleasesCtrl'
					        }
					      },
					      data: {
					        displayName: 'Releases: {{ publisherName | uppercase }}',
		                    permissions: {
	                    	  only: ['authenticated'],
	                    	  redirectTo: 'skyglass.permissions'
						    }						        
					      },
					      resolve: {
					        publisherName: function($stateParams, $resource) {
					          return $resource.getReleaseName($stateParams.id)
					        }
					      }
					    })		
						
					    .state('skyglass.release.releases', {
						      url: '/releases',
						      views: {
						        'release@skyglass.release': {
						          templateUrl: 'js/app/release/releases/releases.html',
						          controller: 'releasesCtrl'
						        }
						      },
			                  data: {
			                	  displayName: 'Releases',
				                  permissions: {
			                    	  only: ['authenticated'],
			                    	  redirectTo: 'skyglass.permissions'	
								  }					                	  
							  }					    
						})
						
				        .state('skyglass.release.subscriber', {
				            url: '/subscriber',
				            abstract: true,
				            views: {
				                "release@skyglass.release": {
				                    abstract :true
				                }
				            },
						    data: {
						        proxy: 'skyglass.release.subscriber.list'
						    }				        
				        })						    
				    
					    
					    .state('skyglass.release.subscriber.list', {
					      url: '/list',
					      views: {
					        'mainView@': {
						       templateUrl: 'js/app/release/subscribers/subscriber-list.html',
							   controller : 'subscriberListCtrl',
					        }
					      },
					      data: {
						    displayName: 'Subscribers',
		                    permissions: {
	                    	  only: ['authenticated'],
	                    	  redirectTo: 'skyglass.permissions'
					        }							     
					      }
					    })
					    
					    .state('skyglass.release.subscriber.subscriptions', {
					      url: '/:id/subscriptions',
					      views: {
					        'release@skyglass.release': {
							  templateUrl: 'js/app/release/subscribers/subscriber-subscriptions.html',
							  controller : 'subscriberSubscriptionsCtrl'
					        }
					      },
					      data: {
					        displayName: 'Subscriptions: {{ subscriberName | uppercase }}',
		                    permissions: {
	                    	  only: ['authenticated'],
	                    	  redirectTo: 'skyglass.permissions'
						    }						        
					      },
					      resolve: {
					        subscriberName: function($stateParams, $resource) {
					          return $resource.getSubscriberName($stateParams.id)
					        }
					      }
					    })		
		                
		                .state('skyglass.release.subscriptions', {
		                    url: '/subscriptions',
		                    views: {
		                        'release@skyglass.release': {
		                            templateUrl: 'js/app/release/subscriptions/subscriptions.html',
		                            controller: 'subscriptionsCtrl',
		                        }
		                    },
			                data: {
			                	  displayName: 'Subscriptions',
				                  permissions: {
			                    	  only: ['authenticated'],
			                    	  redirectTo: 'skyglass.permissions'	
								  }					                	  
							}	
		                }) 
		                
		                
					    
					    .state('skyglass.admin', {
						      url: '/admin',
						      views: {
						        'mainView@': {
						          templateUrl: 'js/app/admin/admin.html',
						          controller: 'adminCtrl'
						        }
						      },
			                  data: {
			                	  displayName: 'Admin',
			                	  //proxyLink: 'skyglass.admin.write'
			                	  proxyLink: '{{ proxyLink }}',
				                  permissions: {
			                    	  only: ['admin'],
			                    	  redirectTo: 'skyglass.permissions'
								  }		                	  
							  },
			                  resolve: {
			                	  proxyLink: function($securityMenuConfig){
			                		  return $securityMenuConfig.defaultAdminState();
			                	  },
			                	  greeting: function($http, $q) {
			                		  var dfd = $q.defer()
			                          $http.get('/resource/').success(function(data) {
			                        	  dfd.resolve(data);
					            	  })
					            	  return dfd.promise;
			                      }
			                  }
						})		
						
					    .state('skyglass.admin.read', {
						      url: '/read',
						      views: {
						        'admin@skyglass.admin': {
						          templateUrl: 'js/app/admin/admin-read.html',
						          controller: 'adminReadCtrl'
						        }
						      },
			                  data: {
			                	  displayName: 'Read',
				                  permissions: {
			                    	  only: ['adminReader'],
			                    	  redirectTo: 'skyglass.permissions'
								  }				                	  
							  }					    
						})	
						
					    .state('skyglass.admin.write', {
						      url: '/write',
						      views: {
						        'admin@skyglass.admin': {
						          templateUrl: 'js/app/admin/admin-write.html',
						          controller: 'adminWriteCtrl'
						        }
						      },
			                  data: {
			                	  displayName: 'Write',
				                  permissions: {
			                    	  only: ['adminWriter'],
			                    	  redirectTo: 'skyglass.permissions'			                    	  
								  }				                	  
							  }					    
						})	
						
					    .state('skyglass.admin.changes', {
						      url: '/changes',
						      views: {
						        'admin@skyglass.admin': {
						          templateUrl: 'js/app/admin/admin-changes.html',
						          controller: 'adminChangesCtrl'
						        }
						      },
			                  data: {
			                	  displayName: 'Changes',
				                  permissions: {
			                    	  only: ['admin'],
			                    	  redirectTo: 'skyglass.permissions'	
								  }					                	  
							  }					    
						})
						
		                .state('skyglass.admin.security', {
		                    url: '/security',
		                    views: {
		                        'admin@skyglass.admin': {
		                            templateUrl: 'js/app/admin/security/admin-security.html',
		                            controller: 'adminSecurityCtrl',
		                        }
		                    },
		                    data: {
		                        displayName: 'Security',
		                        proxyLink: 'skyglass.admin.security.users',
		                        permissions: {
		                            only: ['adminSecurity'],
			                    	redirectTo: 'skyglass.permissions'
		                        }		                        
		                    }
		                }) 
		                
		                .state('skyglass.admin.security.users', {
		                    url: '/users',
		                    views: {
		                        'security@skyglass.admin.security': {
		                            templateUrl: 'js/app/admin/security/users/security-users.html',
		                            controller: 'adminSecurityUsersCtrl'
		                        }
		                    },
		                    data: {
		                    	displayName: 'Users',
		                        permissions: {
		                            only: ['adminSecurity'],
			                    	redirectTo: 'skyglass.permissions'
		                        }			                    	
		                    }
		                }) 	
		                
		                .state('skyglass.admin.security.roles', {
		                    url: '/roles',
		                    views: {
		                        'security@skyglass.admin.security': {
		                            templateUrl: 'js/app/admin/security/roles/security-roles.html',
		                            controller: 'adminSecurityRolesCtrl'
		                        }
		                    },
		                    data: {
		                    	displayName: 'Roles',
		                        permissions: {
		                            only: ['adminSecurity'],
			                    	redirectTo: 'skyglass.permissions'
		                        }			                    	
		                    }
		                }) 	
		                
		                .state('skyglass.admin.security.tokens', {
		                    url: '/tokens',
		                    views: {
		                        'security@skyglass.admin.security': {
		                            templateUrl: 'js/app/admin/security/tokens/security-tokens.html',
		                            controller: 'adminSecurityTokensCtrl'
		                        }
		                    },
		                    data: {
		                    	displayName: 'Tokens',
		                        permissions: {
		                            only: ['adminSecurity'],
			                    	redirectTo: 'skyglass.permissions'
		                        }			                    	
		                    }
		                }) 		                

				}])
				
				.run(function($rootScope, $log, $state, $securityService, $location) {
					  // Initialize security module with the bookmarked, home or login state
					  $securityService.start();		  
					  
					  $rootScope.$on('$stateChangeError', function (event, toState, toParams, fromState, fromParams, error) {
						  $log.debug(error);
						  console.log(error);
					  });		  

				});
    
});