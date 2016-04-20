'use strict';

app.controller('UserCtrl', function($scope, $cookies, UserService) {

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
      .then(res => {
        $scope.user = {
          first_name: res.first_name,
          last_name: res.last_name,
          email: res.email,
          bdate: res.bdate,
          session_id: res.session_id
        }
      });//TODO testing
  }

  $scope.authorize = function () {
    window.location.href = 'http://oauth.vk.com/authorize?' +
      'client_id=5368462' +
      '&display=popup' +
      '&redirect_uri=http://localhost:63342/tripnwalk/client/index.html' +
      '&scope=friends,email' +
      '&response_type=token' +
      '&v=5.50';
  };
});