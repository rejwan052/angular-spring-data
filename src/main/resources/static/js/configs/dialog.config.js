define(['angular'], function(angular) {
    return ["$dialogProvider", function($dialogProvider){
    	

        $dialogProvider.config("settings", {
            contentUrl: '/',
            spinner: 'img/spinner.gif'
        });

        $dialogProvider.config("navigation", {
            add: "js/dialogs/nav.add.tpl.html",
            close: "js/dialogs/nav.close.tpl.html",
            save: "js/dialogs/nav.save.tpl.html",
            saveAndNew: "js/dialogs/nav.save.and.new.tpl.html",
            saveNoClose: "js/dialogs/nav.save.no.close.tpl.html"
        });

        $dialogProvider.config("base", {
            dialog: "js/dialogs/base.dialog.tpl.html",
            confirmation: 'js/dialogs/base.confirmation.tpl.html'
        });

        $dialogProvider.config("dialogs", {
        	
            securityUser: {
                template: 'js/dialogs/security/user/security-user.html',
                navigation: "saveAndNew",
                class: "flex-dialog m",
                controller: "securityUserDialogCtrl",
                model: {
                	id: "",
                    name: "",
                    password: "",
                    login: "",
                    email: ""
                }
            },

            resetUserPassword: {
                template: 'js/dialogs/security/user/reset-password.html',
                navigation: "save",
                class: "flex-dialog m",
                controller: "resetUserPasswordDialogCtrl",
                model: {
                    password: "",
                    reEnter: ""
                }
            },        	

            securityRole: {
                template: 'js/dialogs/security/role/security-role.html',
                navigation: "saveAndNew",
                class: "flex-dialog",
                controller: "securityRoleDialogCtrl",
                model: {
                	id: "",
                    name: ""
                }
            },

            userRoles: {
                template: 'js/dialogs/base.multiple.tpl.html',
                navigation: "add",
                header: 'js/dialogs/security/user/user-roles.header.html',
                class: "flex-dialog",
                controller: "userRolesDialogCtrl",
                model: {},
                cols: ["name"],
                selected: "labels.selectedUserRoles"
            },
            
            saveCategory: {
                template: 'js/dialogs/release/category/edit-category.html',
                navigation: "saveAndNew",
                class: "flex-dialog m",
                controller: "editCategoryDialogCtrl",
                model: {
                	id: "",
                    name: ""
                }
            },            
            
            saveSubscriber: {
                template: 'js/dialogs/release/subscriber/save-subscriber.html',
                navigation: "saveAndNew",
                class: "flex-dialog m",
                controller: "saveSubscriberDialogCtrl",
                model: {
                	id: "",
                    name: ""
                }
            },    
            
            addPublishers: {
                template: 'js/dialogs/base.multiple.select.tpl.html',
                navigation: "save",
                class: "flex-dialog m",
                controller: "addPublishersDialogCtrl",
                model: {
                    ids: []
                }
            },             
            
            editPublisher: {
                template: 'js/dialogs/release/publisher/edit-publisher.html',
                navigation: "save",
                class: "flex-dialog m",
                controller: "savePublisherDialogCtrl",
                model: {
                	id: "",
                    name: ""
                }
            }              


        });

    }];
});
