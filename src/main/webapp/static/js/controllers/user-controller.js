'use strict';

app.controller('UserCtrl', function ($scope, $cookies, UserService, $uibModal) {
  $scope.friends = [];
  $scope.findUsers = [];
  $scope.curFriend = {};

  function compareUser(a, b) {
    if (a.surname < b.surname)
      return -1;
    else if (a.surname > b.surname)
      return 1;
    else if (a.name < b.name)
      return -1;
    else if (a.name > b.name)
      return 1;
    else
      return 0;
  }

  function getTokenFromUrl() {
    var tokenObj = {};

    function getHashValue(key) {
      var matches = location.hash.match(new RegExp(key + '=([^&]*)'));
      return matches ? matches[1] : null;
    }

    _.each(['access_token', 'expires_in', 'user_id', 'email'], param => tokenObj[param] = getHashValue(param));

    return tokenObj;
  }

  if (_.every(['access_token', 'expires_in', 'user_id', 'email'], param => _.contains(location.hash, param))) {
    UserService.getSession(getTokenFromUrl(), $scope)
      .then(res => {
        $scope.user = {
          first_name: res.first_name,
          last_name: res.last_name,
          email: res.email,
          bdate: res.bdate,
          session_id: res.session_id
        };
      });//TODO testing
  }

  $scope.authorize = function () {
    window.location.href = 'http://oauth.vk.com/authorize?' +
      'client_id=5368462' +
      '&display=popup' +
      '&redirect_uri=http://localhost:9095/' +
      '&scope=friends,email' +
      '&response_type=token' +
      '&v=5.50';
  };

  function getFriendById(id) {
    return _.find($scope.friends, friend => friend.id == id);
  }

  $scope.openDeletionFriend = id => {
    $scope.curFriend = getFriendById(id);

    $scope.modalInstance = $uibModal.open({
      templateUrl: '/templates/deletion-friend.html',
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

  $scope.addFriend = function (id, removeFormFriend) {
    if (!_.contains($scope.friends, getFriendById(id))) {
      UserService.addFriend(id)
        .then(res => {
          $scope.friends.push(res);
          $scope.friends.sort(compareUser);
          removeFormFriend();
        });
    }
  };

  $scope.findFriend = function () {
    UserService.findFriend($scope.curFriend.name, $scope.curFriend.surname)
      .then(res => {
        $scope.findUsers = res;
        $scope.findUsers.sort(compareUser);
      });
  };

    UserService.getFriends()
      .then(res => {
        $scope.friends = res;
        $scope.friends.sort(compareUser);
      });


});