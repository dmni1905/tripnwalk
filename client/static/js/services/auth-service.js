'use strict';

app.factory('AuthService', ['$http', '$q', function($http, $q) {
  return {
    authorizeVkUser: () => {
      var url = "http://oauth.vk.com/authorize?" +
        "client_id=" + client_id +
        "&redirect_uri=" + redirect_uri +
        "&display=" + display +
        "&response_type=" + response_type;
      return $http.get(url)
        .then(res => {
          return $http.post
        },
        err => {
          //TODO Handle unsuccessful auth.
        });
    }
  };
}]);