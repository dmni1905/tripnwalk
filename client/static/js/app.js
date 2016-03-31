'use strict';

var app = angular.module('App', ['uiGmapgoogle-maps']).config(
  ['uiGmapGoogleMapApiProvider', function(GoogleMapApiProviders) {
    GoogleMapApiProviders.configure({
      libraries: 'weather,geometry,visualization'
    });
  }]
);