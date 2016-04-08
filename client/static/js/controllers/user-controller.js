'use strict';

app.controller('UserCtrl', function($scope, UserService) {
  function getTokenFromUrl() {
    var tokenObj = {};

    function getHashValue(key) {
      var matches = location.hash.match(new RegExp(key + '=([^&]*)'));
      return matches ? matches[1] : null;
    }

    _.each(['access_token', 'expires_in', 'user_id'], param => tokenObj[param] = getHashValue(param));

    return tokenObj;
  }

  if (_.every(['access_token', 'expires_in', 'user_id'], param => _.contains(location.hash, param))) {
    UserService.getSession(getTokenFromUrl());
  }

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