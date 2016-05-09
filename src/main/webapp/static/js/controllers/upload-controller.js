'use strict';
app.controller('uploadCtrl', function ($scope, UploadService) {
  $scope.uploadFile = function (id) {
    var file = $scope.fileImg;

    console.log('file is ');
    console.dir(file);

    var uploadUrl = "/" + id + "/fileUpload";
    UploadService.uploadFileToUrl(file, uploadUrl)
      .then(res => {
        $scope.setUrl(res);
        console.log($scope.urlAvatar);
    });
  };
});