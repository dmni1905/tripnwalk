'use strict';

app.directive('addFriendForm', function() {
  return {
    restrict: 'E',
    template: '<label type="button" class="add-friend-btn btn btn-primary" friend-form>ADD FRIEND</label>'
  };
});

app.directive('friendForm', function($compile, $templateRequest) {
  return {
    link: function($scope, element, attrs){
      element.bind('click', function(evt){
        $scope.removeFormFriend = () => {
          //TODO proper clean-up.
          $('.friend-form-place').empty();
          $scope.findUsers = [];
          //
          //$scope.toggleRouteMode(false);
        };

        $('#friend-add-window').length > 0 && $scope.removeFormFriend();

        $templateRequest('templates/friend-form.html').then(html => {
          angular.element($('.friend-form-place')).append($compile(html)($scope));

          $('.js-friend-form').draggable();

          $('#clear-friend').on('click', () => $scope.removeFormFriend());
        });
      });
    }
  };
});