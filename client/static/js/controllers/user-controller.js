'use strict';

app.controller('UserCtrl', ['$scope', 'UserService', function($scope, AuthService) {
  var authorize = () => {
    window.location.href = "http://oauth.vk.com/authorize?" +
      "client_id=5368462" +
      "&redirect_uri=http://localhost/docs/tmp/client/index.html" +
      "&display=popup" +
      "&response_type=token";
  };

  $scope.authorize = authorize;
}]);