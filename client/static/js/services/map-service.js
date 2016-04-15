'use strict';

app.factory('MapService', ['$http', '$q', function($http, $q) {
  return {
    create: (route) => {
      return $http.post('http://tripnwalk.tk/rest-api/routes', route)
        .then(res => res.data,
          err => {
            console.error('Route creating failed');

            return $q.reject(err);
          });
    },

    remove: (id) => {
      return $http.delete('http://tripnwalk.tk/rest-api/routes/' + id)
        .then(() => {},
          err => {
            console.error('Route deletion failed');

            return $q.reject(err);
          });
    },

    update: (route) => {
      return $http.patch('http://tripnwalk.tk/rest-api/routes/' + route.id, route)
        .then(res => res.data,
          err => {
            console.error('Route updating failed');

            return $q.reject(err);
          });
    },

    fetchAll: () => {
      return $http.get('http://tripnwalk.tk/rest-api/routes')
        .then(res => res.data,
          err => {
            console.error('Route fetching failed');
            return $q.reject(err);
          });
    },

    fetch: (id) => {
      return $http.get('http://tripnwalk.tk/rest-api/routes/' + id)
        .then(res => res.data,
          err => {
            console.error('Route fetching failed');

            return $q.reject(err);
          });
    }
  };
}]);