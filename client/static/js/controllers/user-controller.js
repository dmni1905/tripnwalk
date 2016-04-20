'use strict';

app.controller('UserCtrl', function ($scope, UserService, $uibModal) {
  $scope.nickname = 'DesiresDesigner';
  $scope.bday = '16/11/93';
  $scope.country = 'Russia';
  $scope.email = 'desiresdesigner@gmail.com';
  $scope.friends = [];
  $scope.curFriend = {};

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

  function getFriendById(id) {
    return _.find($scope.friends, friend => friend.id == id);
  }

  $scope.openDeletionFriend = id => {
    $scope.curFriend = getFriendById(id);

    $scope.modalInstance = $uibModal.open({
      templateUrl: 'templates/deletion-friend.html',
      scope: $scope
    });
  };

  $scope.removeFriend = function () {
    UserService.remove($scope.curFriend.id)
      .then(() => {
        $scope.friends.splice($scope.friends.indexOf($scope.curFriend), 1);
        $scope.curFriend = {};
        $scope.modalInstance.dismiss();

        delete $scope.modalInstance;

      });
  };

  $scope.addFriend = function (removeFormFriend) {

    UserService.addFriend($scope.curFriend.id)
      .then(res => {
        $scope.friends.push(res);
        removeFormFriend();
      });
  };


  UserService.getFriends()
    .then(res => {
      $scope.friends = res;
    });
});