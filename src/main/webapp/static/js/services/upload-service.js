'use strict';

app.factory('UploadService', function ($compile, $templateRequest, $http) {
  return {
    uploadFileToUrl: (file, uploadUrl) => {
      var fd = new FormData();
      fd.append('file', file);

      return $http.post(uploadUrl, fd, {
          transformRequest: angular.identity,
          headers: {'Content-Type': undefined}
        })
        .then(res => {
          return res.data.imgSrc;
          },
          err => {

          });
    }
  }
});