'use strict';

app.controller('UserCtrl', ['$scope', 'UserService', function($scope, AuthService) {
  var authorize = () => {
    AuthService.authorize()
      .then((res, err, info) => {
        res;
      });
  };

  $scope.authorize = authorize;
}]);