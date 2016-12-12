define(['angular', 'configurations'], function (angular, configurations) {
	configurations.constant("wizardConfig", []).constant('$wizardConfig', {

	    addRelease: {
	        title: 'Add Release',
	        class: 'wizard-750',
	        template: 'js/dialogs/release/release/add-release/add-release.html',
	        controller: "addReleaseDialogCtrl",
	        model: {
                categoryId: "",
                publisherId: "",
                file: ""
	        }
	    },
	    
	    addSubscription: {
	        title: 'Add Subscription',
	        class: 'wizard-750',
	        template: 'js/dialogs/release/subscription/add-subscription/add-subscription.html',
	        controller: "addSubscriptionDialogCtrl",
	        model: {
	        	subscriberId: '',
	        	categoryId: ''
	        }
	    }	    

	})
});
