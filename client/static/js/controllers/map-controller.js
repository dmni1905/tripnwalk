'use strict';

app.controller('MapCtrl', function($scope, $element, $attrs, uiGmapIsReady, MapService) {
  var routeMode = false;
  var map;

  $scope.curRoute = {};
  $scope.routes = [];
  $scope.map = {
    center: { latitude: 59.938600, longitude: 30.31410 },
    zoom: 13,
    options: {
      minZoom: 3,
      maxZoom: 18,
      disableDefaultUI: true
    }
  };

  $scope.toggleRouteMode = state => routeMode = state;

  function setPolyline() {
    $scope.curRoute = {
      path: new google.maps.Polyline({
        strokeColor: '#000000',
        strokeOpacity: 1.0,
        strokeWeight: 3
      })
    };

    $scope.curRoute.path.setMap(map);
  }
// Handles click events on a map, and adds a new point to the Polyline.
  function setMarker(evt) {
    if (!routeMode) return;

    !$scope.curRoute.path && setPolyline();

    var position = evt.latLng;
    var path = $scope.curRoute.path.getPath();

    // Because path is an MVCArray, we can simply append a new coordinate
    // and it will automatically appear.
    path.push(position);

    $scope.curRoute.points = routeToArray($scope.curRoute.path);

    $scope.$apply();
  }

  function routeToArray(route) {
    return _.map(route.getPath().j, (latLng, index) => {
      return {
        position: index,
        lat: latLng.lat().toFixed(6),
        lng: latLng.lng().toFixed(6)
      };
    });
  }

  function renderRoute(route) {
    var points = _.sortBy(route.points, point => point.position);
    var polyLine = _.map(points, latLng => new google.maps.LatLng(latLng.lat, latLng.lng));
    var path = new google.maps.Polyline({
      path: polyLine,
      strokeColor: '#000000',
      strokeOpacity: 1.0,
      strokeWeight: 3
    });

    path.setMap(map);

    return _.extend(route, { path: path });
  }

  $scope.saveRoute = function() {
    //TODO validation.
    var route = _.omit($scope.curRoute, 'path');
    route.points = _.map(route.points, point => _.omit(point, '$$hashKey'));

    MapService.create(route)
      .then(route => {
        $scope.routes[route.id] = $scope.curRoute;
      });
  };

  $scope.updateRoute = function() {
    var route = _.omit($scope.curRoute, 'path');

    MapService.update(route)
      .then(route => {
        $scope.routes[route.id] = route;
      });
  };

  $scope.clearRoute = function() {
    $scope.curRoute.path && $scope.curRoute.path.getPath().clear();

    $scope.curRoute = {};
  };

  $scope.removeRoute = function(route) {
    MapService.remove(route.id)
      .then(() => {
        route.getPath().clear();

        delete $scope.routes[route.id];
      });
  };

  //Renders map.
  uiGmapIsReady.promise(1)
    .then(instances => instances[0].map)
    .then(loadedMap => {
      map = loadedMap;

      map.addListener('click', setMarker);
      // Special hack for map to be resized on slider toggle.
      $('#menu-toggle').on('click', function() {
        setTimeout(() => {
          var center = map.getCenter();
          google.maps.event.trigger(map, 'resize');
          map.setCenter(center);
        }, 320);
      });

      MapService.fetchAll()
        .then(res => {
          $scope.routes = _.map(res, route => renderRoute(route));
        });
    });
});