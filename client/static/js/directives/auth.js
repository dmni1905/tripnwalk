'use strict';

app.controller('AuthCtrl', function(UserService, $scope) {
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
      '&display=popup' +
      '&redirect_uri=http://localhost/docs/tmp/client/index.html' +
      '&scope=bdate,photo_200_orig' +
      '&response_type=token' +
      '&v=5.50';
  };
});