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
        $scope.removeForm = () => {
          $('.route-form-place').empty();
          $scope.clearRoute();

          $scope.toggleRouteMode(false);
          $scope.toggleRouteSelected(false, $scope.curRoute);
        };

        $('#route-edit-window').length > 0 && $scope.removeForm();
        $scope.toggleRouteMode(true);

        var existingRoute = evt.target.closest('.route-menu-controls').getAttribute('class');

        if (_.contains(existingRoute, 'id-')) {
          $scope.curRoute =  _.find($scope.routes, route => {
            return route.id == existingRoute.match(/id-(.+)/)[1];
          });

          $scope.toggleRouteSelected(true, $scope.curRoute);

          $scope.curRoute.points = _.sortBy($scope.curRoute.points, point => point.position);
        }

        $scope.removePoint = $event => {
          if ($scope.curRoute.points.length <= 2) return;

          var index = $('.panel-group .panel').index($event.currentTarget.closest('.panel'));

          $scope.removePoint0($scope.curRoute.id, index);
        };

        $templateRequest('/templates/route-form.html').then(html => {
          angular.element($('.route-form-place')).append($compile(html)($scope));

          $('.js-route-form').draggable();

          $('#clear-route').on('click', () => $scope.removeForm());
        });
      });
    }
  };
});

  app.directive('routeGuestForm', function($compile, $templateRequest) {
    return {
      link: function($scope, element, attrs){
        //TODO look at controllers to handle route-form.
        element.bind('click', function(evt){
          $scope.removeForm = () => {
            //TODO proper clean-up.
            $('.route-form-place').empty();
            $scope.clearRoute();

            $scope.toggleRouteMode(false);
          };

          $('#route-edit-window').length > 0 && $scope.removeForm();
          $scope.toggleRouteMode(true);

          var existingRoute = evt.target.closest('.route-menu-controls').getAttribute('class');

          if (_.contains(existingRoute, 'id-')) {
            $scope.curRoute =  _.find($scope.routes, route => {
              return route.id == existingRoute.match(/id-(.+)/)[1];
            });

            $scope.curRoute.points = _.sortBy($scope.curRoute.points, point => point.position);
          }

          $scope.removePoint = $event => {
            if ($scope.curRoute.points.length <= 2) return;

            var index = $('.panel-group .panel').index($event.currentTarget.closest('.panel'));

            $scope.removePoint0($scope.curRoute.id, index);
          };

          $templateRequest('/templates/route-guest-form.html').then(html => {
            angular.element($('.route-form-place')).append($compile(html)($scope));

            $('.js-route-form').draggable();

            $('#clear-route').on('click', () => $scope.removeForm());
          });
        });
      }
    };
  });