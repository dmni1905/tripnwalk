'use strict';

app.controller('UserCtrl', function($scope, UserService) {
  function getTokenFromUrl() {
    var tokenObj = {};

    _.each(['access_token', 'expires_in', 'user_id'], param => {
      tokenObj[param] = new RegExp('[\\?#&]' + param.replace(/[\[]/,'\\\[').replace(/[\]]/,'\\\]') + '=([^&#]*)')
        .exec(location.href)[1];
    });

    return tokenObj;
  }

  _.contains(window.location.href, '#access_token=') && UserService.getSession(getTokenFromUrl());

  $scope.authorize = function () {
      window.location.href = 'http://oauth.vk.com/authorize?' +
            'client_id=5368462' +
            '&redirect_uri=http://localhost/docs/tmp/client/index.html' +
            '&display=popup' +
            '&response_type=token';
  };

  $scope.nickname = 'DesiresDesigner';
  $scope.bday = '16/11/93';
  $scope.country = 'Russia';
  $scope.email = 'desiresdesigner@gmail.com';
});