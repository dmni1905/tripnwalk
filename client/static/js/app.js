'use strict';

var app = angular.module('App', ['uiGmapgoogle-maps', 'ui.bootstrap']).config(
  ['uiGmapGoogleMapApiProvider', function(GoogleMapApiProviders) {
    GoogleMapApiProviders.configure({
      libraries: 'weather,geometry,visualization'
    });
  }]
);