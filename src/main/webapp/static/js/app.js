'use strict';

var app = angular.module('App', ['uiGmapgoogle-maps', 'ui.bootstrap', 'ngCookies']).config(
  ['uiGmapGoogleMapApiProvider', '$httpProvider', function(GoogleMapApiProviders, $httpProvider) {
    GoogleMapApiProviders.configure({
      libraries: 'weather,geometry,visualization'
    });

    $httpProvider.interceptors.push('httpRequestInterceptor');
  }]
);

app.factory('httpRequestInterceptor', ['$cookies', function($cookies) {
    return {
      request: function($config) {
        var sid = $cookies.get('sid');

        if(sid) {
          $config.headers.sid = 'sid=' + sid;
        }

        return $config;
      }
    };
  }]);