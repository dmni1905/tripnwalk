'use strict';

app.directive('addRouteForm', function() {
  return {
    restrict: 'E',
    template: '<button route-form>Add route</button>'
  };
});

app.directive('routeForm', function($compile, $templateRequest) {
  return {
    link: function(scope, element, attrs){
      //TODO look at controllers to handle route-form.
      var myScope = scope;
      var myEl = element;
      element.bind('click', function(evt){
        myScope;
        myEl;
        $templateRequest('templates/route-form.html').then(html => {
          angular.element($('.route-form-place')).append($compile(html)(scope));
        });
      });
    }
  };
});