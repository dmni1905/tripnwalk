'use strict';

app.directive('addRouteForm', function() {
  return {
    restrict: 'E',
    template: '<label type="button" class="add-route-btn btn btn-primary" route-form>ADD ROUTE</label>'
  };
});

app.directive('routeForm', function($compile, $templateRequest) {
  return {
    link: function($scope, element, attrs){
      //TODO look at controllers to handle route-form.
      element.bind('click', function(evt){
        function removeForm() {
          //TODO proper clean-up.
          $('.route-form-place').empty();
          $scope.clearRoute();

          $scope.toggleRouteMode(false);
        }

        $('#route-edit-window').length > 0 && removeForm();
        $scope.toggleRouteMode(true);

        if (_.contains(evt.target.getAttribute('class'), 'id-')) {
          $scope.curRoute =  _.find($scope.routes, route => {
            return route.id == evt.target.getAttribute('class').match(/id-(.+)/)[1];
          });

          $scope.curRoute.points = _.sortBy($scope.curRoute.points, point => point.position);
        }

        $templateRequest('templates/route-form.html').then(html => {
          angular.element($('.route-form-place')).append($compile(html)($scope));

          $('.js-route-form').draggable();

          $('#clear-route').on('click', () => removeForm());
        });
      });
    }
  };
});