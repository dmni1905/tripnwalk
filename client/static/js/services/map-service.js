'use strict';

app.factory('MapService', ['$http', '$q', function($http, $q) {
  return {
    create: (route) => {
      return $http.post('http://localhost:9095/routes', route)
        .then(res => res.data,
          err => {
            console.error('Route creating failed');

            return $q.reject(err);
          });
    },

    remove: (id) => {
      return $http.delete('http://localhost:9095/routes/' + id)
        .then(() => {},
          err => {
            console.error('Route deletion failed');

            return $q.reject(err);
          });
    },

    update: (route) => {
      return $http.patch('http://localhost:9095/routes/' + route.id, route)
        .then(res => res.data,
          err => {
            console.error('Route updating failed');

            return $q.reject(err);
          });
    },

    fetchAll: () => {
      return $http.get('http://localhost:9095/routes')
        .then(res => res.data,
          err => {
            console.error('Route fetching failed');
            return $q.reject(err);
          });
    },

    fetch: (id) => {
      return $http.get('http://localhost:9095/routes/' + id)
        .then(res => res.data,
          err => {
            console.error('Route fetching failed');

            return $q.reject(err);
          });
    }
  };
}]);