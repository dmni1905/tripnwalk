'use strict';

app.controller('MapCtrl', function($scope, $element, $attrs, uiGmapIsReady, MapService) {
  var map;
  var routes = {};
  var curRoute;

  $scope.map = {
    center: { latitude: 59.938600, longitude: 30.31410 },
    zoom: 11,
    options: {
      disableDefaultUI: true
    }
  };

  function setPolyline() {
    curRoute = new google.maps.Polyline({
      strokeColor: '#000000',
      strokeOpacity: 1.0,
      strokeWeight: 3
    });

    curRoute.setMap(map);
  }
  // Handles click events on a map, and adds a new point to the Polyline.
  function setMarker(evt) {
    !curRoute && setPolyline();

    var position = evt.latLng;
    var path = curRoute.getPath();

    // Because path is an MVCArray, we can simply append a new coordinate
    // and it will automatically appear.
    path.push(position);
  }

  function routeToArray(route) {
    return _.map(route.getPath().j, (latLng, index) => _.extend(latLng.toJSON(), { position: index }));
  }

  function renderRoute(latLngArray) {
    var polyLine = _.map(latLngArray, latLng => new google.maps.LatLng(latLng.lat, latLng.lng));
    var Path = new google.maps.Polyline({
      path: polyLine,
      strokeColor: '#000000',
      strokeOpacity: 1.0,
      strokeWeight: 3
    });

    Path.setMap(map);

  }

  $scope.createRoute = function() {
    var points = JSON.stringify(routeToArray(curRoute));

    MapService.create(points)
      .then(route => {
        routes[route.id] = route;

        setPolyline();
      });
  };

  $scope.updateRoute = function(route) {
    var points = JSON.stringify(routeToArray(route));

    MapService.update(points)
      .then(route => {
        routes[route.id] = route;

        renderRoute(route);

        setPolyline();
      });
  };

  $scope.clearRoute = function() {
    curRoute.getPath().clear();

    setPolyline();
  };

  $scope.removeRoute = function(route) {
    MapService.remove(route.id)
      .then(() => {
        route.getPath().clear();

        delete routes[route.id];
      });
  };

  //Initialize map.
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
          routes = res;

          _.each(routes, route => renderRoute(route));
        });
    });
});