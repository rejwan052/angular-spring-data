define(['angular', 'configurations'], function (angular, configurations) {
	configurations.constant("wizardConfig", []).constant('$wizardConfig', {

	    publishRelease: {
	        title: 'Publish Release',
	        class: 'wizard-750',
	        template: 'js/dialogs/publish-release/publish-release.html',
	        controller: "publishReleaseDialogCtrl",
	        model: {
                categoryId: "",
                publisherId: "",
                file: ""
	        }
	    },
	    
	    addSubscription: {
	        title: 'Add Subscription',
	        class: 'wizard-750',
	        template: 'js/dialogs/add-subscription/add-subscription.html',
	        controller: "addSubscriptionDialogCtrl",
	        model: {
	        	subscriberId: '',
	        	categoryId: ''
	        }
	    }	    

	})
});
