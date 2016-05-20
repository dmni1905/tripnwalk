'use strict';

app.controller('UserCtrl', function ($scope, $cookies, UserService, $uibModal) {
  $scope.friends = [];
  $scope.findUsers = [];
  $scope.curFriend = {};
  $scope.user = {};
  $scope.urlAvatar = 'http://www.nbb.go.th/images/blank_person[1].jpg';
  $scope.incorrect_login = "";
  $scope.incorrect_register ="Login exist";
  $scope.hide = {surname: true, name: true, birthDate: true, email: true, login_error: true, register_error: true, upload_form: true};
  $scope.userUpdate = {surname: true, name: true, birthDate: true, email: true};

  $scope.setUrl = function(url){
    $scope.urlAvatar = url;
  };

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

    _.each(['access_token', 'expires_in', 'user_id', 'email',""], param => tokenObj[param] = getHashValue(param));

    return tokenObj;
  }

  if (_.every(['access_token', 'expires_in', 'user_id', 'email'], param => _.contains(location.hash, param))) {
    UserService.getSession(getTokenFromUrl(), $scope)
      .then(res => {
        window.location.href = '/' + res.session_id;
      });//TODO testing
  }

  $scope.authorize = function () {
    window.location.href = 'http://oauth.vk.com/authorize?' +
      'client_id=5368462' +
      '&display=popup' +
      '&redirect_uri=http://tripnwalk.tk/' +
      '&scope=friends,email' +
      '&response_type=token' +
      '&v=5.50';
  };

  function getFriendById(id) {
    return _.find($scope.friends, friend => friend.id == id);
  }

  $scope.login = function () {
    if (($scope.user.login == null)&&($scope.user.password == null)) {
      $scope.hide.login_error = false;
      $scope.incorrect_login = "Login & Password is null"
    }else if ($scope.user.login == null){
      $scope.hide.login_error = false;
      $scope.incorrect_login = "Login is null";
    }else if ($scope.user.password == null) {
      $scope.hide.login_error = false;
      $scope.incorrect_login = "Password is null";
    }else{
      UserService.login($scope.user)
          .then(res => {
            if (res == undefined) {
              $scope.incorrect_login = "Data is incorrect"
              $scope.hide.login_error = false;
            } else {
              window.location.href = '/' + res;
            }
          });
    }
  }

  $scope.register = function () {
    console.log($scope.user);
    if ($scope.user.name != null && $scope.user.login != null
      && $scope.user.email != null && $scope.user.password != null) {
      UserService.register($scope.user)
        .then(res => {
          if (res == undefined) {
            $scope.hide.register_error = false;
          } else {
            window.location.href = '/' + res;
          }
        });
    }else{
      $scope.incorrect_register = "Data is incorrect";
      $scope.hide.register_error = false;
    }
  }


  $scope.enter = function(){
    window.location.href = '/enter_guest';
  }

  $scope.registerPage = function () {
    window.location.href = '/register';
  }

  $scope.mainPage = function () {
    window.location.href = 'http://tripnwalk.tk/';
  }

  $scope.updateMe = function () {
    if (!$scope.user.surname.match(/[A-Za-zА-Яа-яЁё]{2,20}/)){
      $scope.userUpdate.surname = false;
      $scope.hide.surname = false;
    }
    if (!$scope.user.name.match(/[A-Za-zА-Яа-яЁё]{2,20}/)) {
      $scope.userUpdate.name = false;
      $scope.hide.name = false;
    }
    if ((!$scope.user.birthDate.match(/[0-9]{2}\.[0-9]{2}\.[0-9]{4}/)) && (!$scope.user.birthDate.match(/[0-9]{1,2}\.[0-9]{1,2}/))){
      $scope.userUpdate.birthDate = false;
      $scope.hide.birthDate = false;
    }
    if (!$scope.user.email.match(/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/)){
      $scope.userUpdate.email = false;
      $scope.hide.email = false;
    }
    if ($scope.user.surname.match(/[A-Za-zА-Яа-яЁё]{2,20}/)
        &&($scope.user.name.match(/[A-Za-zА-Яа-яЁё]{2,20}/))
        &&(($scope.user.birthDate.match(/[0-9]{2}\.[0-9]{2}\.[0-9]{4}/)) || ($scope.user.birthDate.match(/[0-9]{1,2}\.[0-9]{1,2}/)))
        &&($scope.user.email.match(/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/))){
      UserService.update($scope.user)
        .then(() => {
        });
    }
  }

  $scope.logout = function () {
    UserService.logout()
        .then(() => {
          window.location.href = 'http://tripnwalk.tk/';
        });
  }

  $scope.setFriends = function (friends) {
    $scope.friends = friends;
    $scope.friends.sort(compareUser);
  }

  $scope.openDeletionFriend = id => {
    $scope.curFriend = getFriendById(id);

    $scope.modalInstance = $uibModal.open({
      templateUrl: '/templates/deletion-friend.html',
      scope: $scope
    });
  };

  $scope.removeFriend = function () {
    UserService.removeFriend($scope.user.id, $scope.curFriend.id)
      .then(() => {
        $scope.friends.splice($scope.friends.indexOf($scope.curFriend), 1);
        $scope.curFriend = {};
        $scope.modalInstance.dismiss();

        delete $scope.modalInstance;

      });
  };

  $scope.addFriend = function (id, removeFormFriend) {
    if (!_.contains($scope.friends, getFriendById(id))) {
      UserService.addFriend($scope.user.id, id)
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

  //$scope.uploadImg = function () {
  //  file.$upload('/uploadImg', $scope.file)
  //}

})
;