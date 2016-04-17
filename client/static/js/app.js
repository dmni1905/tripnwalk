'use strict';

var app = angular.module('App', ['uiGmapgoogle-maps', 'ui.bootstrap', 'ngCookies']).config(
  ['uiGmapGoogleMapApiProvider', function(GoogleMapApiProviders) {
    GoogleMapApiProviders.configure({
      libraries: 'weather,geometry,visualization'
    });
  }]
);