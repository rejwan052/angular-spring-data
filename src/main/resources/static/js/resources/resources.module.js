define([
	'angular',
	'resource',
	'resources/security',
	'resources/category',
	'resources/publisher',
	'resources/release',
	'resources/subscriber',
	'resources/subscription'

], function (
	angular,
	resource,
	security,
	category,
	publisher,
	release,
	subscriber,
	subscription
) {
	return angular.module('skyglass.resources', [])
	.factory("$security", security)
	.factory("$category", category)
	.factory("$publisher", publisher)
	.factory("$release", release)	
	.factory("$subscriber", subscriber)
	.factory("$subscription", subscription)
	.name;
});