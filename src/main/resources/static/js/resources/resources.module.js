define([
	'angular',
	'resource',
	'resources/security',
	'resources/release'

], function (
	angular,
	resource,
	security,
	release
) {
	return angular.module('skyglass.resources', [])
	.factory("$security", security)
		.factory("$release", release)
	.name;
});