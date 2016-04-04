'use strict';

app.controller('UserCtrl', ['$scope', '$window', function($scope, w) {
  $scope.authorize = function () {
      window.location.href = "http://oauth.vk.com/authorize?" +
            "client_id=5368462" +
            "&redirect_uri=http://localhost/docs/tmp/client/index.html" +
            "&display=popup" +
            "&response_type=token";
  };

  $scope.nickname = "DesiresDesigner";
  $scope.bday = "16/11/93";
  $scope.country = "Russia";
  $scope.email = "desiresdesigner@gmail.com";

}]);