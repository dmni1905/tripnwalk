'use strict';

app.factory('UserService', ['$http', '$q', function($http, $q) {
  return {
    authorize: () => {
      return $http.get('http://localhost:9095/auth')
        .then(res => {
          res;
        },
        err => {
          //TODO Handle unsuccessful auth.
          err;
        });
    }
  };
}]);