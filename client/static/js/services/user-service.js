'use strict';

app.factory('UserService', ['$http', '$q', function ($http, $q) {
  return {
    getSession: (tokenObj) => {
      return $http.get('http://localhost:9095/session', tokenObj)
        .then(res => {
            //TODO handle session id to Local Storage.
            console.log('Authorized!');

            location.hash = '';
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
      return $http.get('http://localhost:9095/friends')
        .then(res => res.data,
          err => {
            console.error('Get friends list failed');
            return $q.reject(err);
          });
    },

    remove: (id) => {
      return $http.delete('http://localhost:9095/friends/' + id)
        .then(() => {
          err => {
            console.error('Friend deletion failed');

            return $q.reject(err);
          }
        });
    },

    addFriend: (id) => {
      return $http.put('http://localhost:9095/friends/' + id)
        .then( res => res.data,
          err => {
            console.error('Friend add failed');

            return $q.reject(err);
        });
    }

  };
}]);