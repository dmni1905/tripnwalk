'use strict';

app.controller('UserCtrl', function($scope, $cookies, UserService) {
  $scope.nickname = 'DesiresDesigner';
  $scope.bday = '16/11/93';
  $scope.country = 'Russia';
  $scope.email = 'desiresdesigner@gmail.com';

  $scope.user = {};

  function getTokenFromUrl() {
    var tokenObj = {};

    function getHashValue(key) {
      var matches = location.hash.match(new RegExp(key + '=([^&]*)'));
      return matches ? matches[1] : null;
    }

    _.each(['access_token', 'expires_in', 'user_id','email'], param => tokenObj[param] = getHashValue(param));

    return tokenObj;
  }

  if (_.every(['access_token', 'expires_in', 'user_id','email'], param => _.contains(location.hash, param))) {
    UserService.getSession(getTokenFromUrl(), $scope)
      .then(sid => $cookies.put('sid', sid));//TODO testing
  }

  $scope.authorize = function () {
    window.location.href = 'http://oauth.vk.com/authorize?' +
      'client_id=5368462' +
      '&display=popup' +
      '&redirect_uri=http://localhost/docs/tmp/client/index.html' +
      '&scope=friends,email' +
      '&response_type=token' +
      '&v=5.50';
  };
});