'use strict';

app.factory('UserService', ['$http', '$q', function($http, $q) {
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
    }
  };
}]);