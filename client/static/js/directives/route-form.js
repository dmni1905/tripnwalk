'use strict';

app.directive('addRouteForm', function() {
  return {
    restrict: 'E',
    template: '<label type="button" class="add-route btn btn-primary" route-form>ADD ROUTE</label>'
  };
});

app.directive('routeForm', function($compile, $templateRequest) {
  return {
    link: function($scope, element, attrs){
      //TODO look at controllers to handle route-form.
      element.bind('click', function(evt){
        $scope.curRoute = undefined;

        if (_.contains(evt.target.getAttribute('class'), 'id-')) {
          $scope.curRoute =  _.find($scope.routes, route => {
            return route.id == evt.target.getAttribute('class').match(/id-(.+)/)[1];
          });

          $scope.curRoute.points = _.sortBy($scope.curRoute.points, point => point.position);
        }

        $templateRequest('templates/route-form.html').then(html => {
          angular.element($('.route-form-place')).append($compile(html)($scope));

          $('#clear-route').on('click', () => {
            $('.route-form-place').empty();

            $scope.clearRoute();
          });
        });
      });
    }
  };
});