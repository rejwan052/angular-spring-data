require.config({
    waitSeconds: 200,
	paths: {

		//Libs
		angular: 'libs-bower/angular/angular',
		jquery: 'libs-bower/jquery/jquery',
		jqueryui: 'libs-bower/jquery-ui/jquery-ui',
		jquerycookie: 'libs-bower/jquery.cookie/jquery.cookie',
		uirouter: 'libs-bower/angular-ui-router/release/angular-ui-router',
		uiroutertabs: 'libs/ui-router-tabs',
		resource: 'libs-bower/angular-resource/angular-resource',
		translate: 'libs-bower/angular-translate/angular-translate',
		sanitize: 'libs-bower/angular-sanitize/angular-sanitize',
		translatefiles: 'libs-bower/angular-translate-loader-static-files/angular-translate-loader-static-files',
        permissions: 'libs-bower/angular-permission/dist/angular-permission',
		moment: 'libs-bower/moment/moment',
		route: 'libs-bower/angular-route/angular-route',
		bootstrap: 'libs-bower/bootstrap/dist/js/bootstrap',
        bootstrapTpls: 'libs-bower/angular-bootstrap/ui-bootstrap-tpls',
        ngDialog: 'libs-bower/ngDialog/js/ngDialog',
		lodash: 'libs/lodash.min',
		showdown: 'libs/showdown.min',
		
    	//Configs    
        dialogConfig: 'configs/dialog.config',
        dialogTitleConfig: 'configs/dialog.title.config',
        wizardConfig: 'configs/wizard.config',
  	
		//Plugins
		tablePlugin: 'plugins/ng-table/ng-table',    	

		//Modules
		app: 'modules/app',
		controllers: 'modules/controllers',
		directives: 'modules/directives',
		filters: 'modules/filters',
    	services: 'modules/services',
    	configurations: 'modules/configs',
        templates: 'modules/templates',
        thirdparty: 'modules/thirdparty',
        componentsModule: 'components/components',
        utilsModule: 'utils/utils',
        resourcesModule: 'resources/resources.module',
        skgSecurity: 'security/security.module',        
        
        //----------------------Components----------------------//
        dialogComponent: 'components/dialog/dialog',

        //----------------------Controllers----------------------//
    	indexCtrl: 'app/main/index',
    	releaseCtrl: 'app/release/release',	
    	categoryListCtrl: 'app/release/categories/category-list',	
    	categoryReleasesCtrl: 'app/release/categories/category-releases',
    	categorySubscriptionsCtrl: 'app/release/categories/category-subscriptions',
    	publisherListCtrl: 'app/release/publishers/publisher-list',
    	publisherReleasesCtrl: 'app/release/publishers/publisher-releases',
    	releasesCtrl: 'app/release/releases/releases',
    	subscriberListCtrl: 'app/release/subscribers/subscriber-list',
    	subscriberSubscriptionsCtrl: 'app/release/subscribers/subscriber-subscriptions',
    	subscriptionsCtrl: 'app/release/subscriptions/subscriptions',
    	loginCtrl: 'app/login/login',
    	permissionsCtrl: 'app/permissions/permissions',
    	
        //Admin Controllers
    	adminCtrl: 'app/admin/admin',
    	adminChangesCtrl: 'app/admin/admin-changes',
    	adminReadCtrl: 'app/admin/admin-read',
    	adminWriteCtrl: 'app/admin/admin-write',    	
        adminSecurityCtrl: 'app/admin/security/admin-security',
        adminSecurityUsersCtrl: 'app/admin/security/users/security-users',
        adminSecurityRolesCtrl: 'app/admin/security/roles/security-roles',
        adminSecurityTokensCtrl: 'app/admin/security/tokens/security-tokens',
        
        //Dialog Controllers
        saveUserDialogCtrl: 'dialogs/security/user/save-user.ctrl',
        resetPasswordDialogCtrl: 'dialogs/security/user/reset-password.ctrl',
        manageUserRolesDialogCtrl: 'dialogs/security/user/manage-user-roles.ctrl',
        saveRoleDialogCtrl: 'dialogs/security/role/save-role.ctrl',
        multipleDialogCtrl: 'dialogs/multiple.dialog.ctrl',
        
        addReleaseDialogCtrl: 'dialogs/release/release/add-release/add-release.ctrl',
        addReleasePublisherDialogCtrl: 'dialogs/release/release/add-release/publisher/add-release-publisher.ctrl',
        addReleaseCategoryDialogCtrl: 'dialogs/release/release/add-release/category/add-release-category.ctrl',
        addReleaseUploadDialogCtrl: 'dialogs/release/release/add-release/upload/add-release-upload.ctrl',
        
        addSubscriptionDialogCtrl: 'dialogs/release/subscription/add-subscription/add-subscription.ctrl',
        addSubscriptionSubscriberDialogCtrl: 'dialogs/release/subscription/add-subscription/subscriber/add-subscription-subscriber.ctrl',
        addSubscriptionCategoryDialogCtrl: 'dialogs/release/subscription/add-subscription/category/add-subscription-category.ctrl',
        
        saveCategoryDialogCtrl: 'dialogs/release/category/save-category.ctrl',
        saveSubscriberDialogCtrl: 'dialogs/release/subscriber/save-subscriber.ctrl',
        savePublisherDialogCtrl: 'dialogs/release/publisher/save-publisher.ctrl',
    	
    	//Services    	
        formatterService: 'services/formatter.service',
        messageService: 'services/message.service',
        filtersService: 'services/filters.service',
        wizardService: 'services/wizard.service',
    	
        //Directives
        breadcrumbsDirective: 'directives/breadcrumbs/uiBreadcrumbs',
        btnTitleDirective: 'directives/btn-title/btn-title',
        tableSearchDirective: 'directives/table-search/table-search'
	},

	shim: {
		angular: {
			exports: 'angular'
		},

        /*Third Party Modules*/
		resource: ['angular'],
        sanitize: ['angular'],
		translate: ['angular'],
        ngDialog: ['angular'],
		translatefiles: ['angular', 'translate'],
		uirouter: ['angular', 'resource'],  
        permissions: ['uirouter'],
		jquery: ['angular'],
		jqueryui: ['jquery'],
		bootstrap: ['jquery'],
		jquerycookie: ['jquery'],
        bootstrapTpls: ['angular'],

        //Skyglass Modules
        skgSecurity: ['angular'],
        templates: ['angular'],
		app: ['angular']


	}
});

require([
    'angular',
    'app'
], function(angular, app) {
    angular.bootstrap(document, ['skyglass']);     
});

