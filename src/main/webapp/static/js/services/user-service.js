'use strict';

app.factory('UserService',function($compile, $templateRequest, $http) {
  return {
    getSession: (tokenObj, $scope) => {
      return $http.post('/session', tokenObj)
        .then(res => {
            //TODO handle session id to Local Storage.
            console.log('Authorized!');

            location.hash = '';
            $('#auth').remove();
            $templateRequest('/templates/main-page.html').then(html => angular.element($('body')).append($compile(html)($scope)));

            return res.data;

          },
          err => {
            //TODO Handle unsuccessful auth.
            err;
            console.log('Authorization failed!');

            //TODO location.hash.replace('#',''); - extract params using this.
            location.hash = '';
          });
    },

    getFriends: () => {
      return $http.get('/friends')
        .then(res => res.data,
          err => {
            console.error('Get friends list failed');
            return $q.reject(err);
          });
    },

    remove: (id) => {
      return $http.delete('/friends/' + id)
        .then(() => {
          err => {
            console.error('Friend deletion failed');

            return $q.reject(err);
          }
        });
    },

    addFriend: (id) => {
      return $http.put('/friends/' + id)
        .then(res => res.data,
          err => {
            console.error('Friend add failed');

            return $q.reject(err);
          });
    },

    findFriend: (name, surname) => {
      return $http.get('/find-user', {
          params: {name: (name === undefined) ? "" : name , surname: (surname === undefined) ? "" : surname}
        })
        .then(res => res.data,
          err => {
            console.error('Friend find failed');

            return $q.reject(err);
          }
        );
    }
  };
});
