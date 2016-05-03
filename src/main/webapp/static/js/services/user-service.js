'use strict';

app.factory('UserService', function ($compile, $templateRequest, $http) {
  return {
    getSession: (tokenObj, $scope) => {
      return $http.post('/session', tokenObj)
        .then(res => {
            //TODO handle session id to Local Storage.
            console.log('Authorized!');

            location.hash = '';
            //$('#auth').remove();
            //$templateRequest('/templates/main-page.html').then(html => angular.element($('body')).append($compile(html)($scope)));

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

      login:(user)=>{
          return $http.post('/login', user)
              .then(res => {
                      console.log('Authorized!');
                      return res.data;
                  },
                  err => {
                      err;
                      console.log('Authorization failed!');
                  });
      },

      // register:(user)=>{
      //     return $http.put('/', user)
      //         .then(res => {
      //                 console.log('Authorized!');
      //                 return res.data;
      //             },
      //             err => {
      //                 err;
      //                 console.log('Authorization failed!');
      //             });
      // },
      
    registerPage:()=>{
      return $http.get('/registerPage')
          .then(res => {
              return res.data;
          });
    },
      
    getFriends: (id) => {
      return $http.get('/'+id+'/friends')
        .then(res => res.data,
          err => {
            console.error('Get friends list failed');
            return $q.reject(err);
          });
    },

    update:(user)=>{
        return $http.patch('/' + user.id, user)
            .then(res => res.data,
                err => {
                    return $q.reject(err);
                });
    } ,

    removeFriend: (id, id_friends) => {
      return $http.delete('/' + id + '/friends/' + id_friends)
        .then(() => {
          err => {
            console.error('Friend deletion failed');

            return $q.reject(err);
          }
        });
    },

    addFriend: (id, id_friends) => {
      return $http.put('/' + id + '/friends/' + id_friends)
        .then(res => res.data,
          err => {
            console.error('Friend add failed');

            return $q.reject(err);
          });
    },

    findFriend: (name, surname) => {
      return $http.get('/find-user', {
          params: {name: (name === undefined) ? "" : name, surname: (surname === undefined) ? "" : surname}
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
