'use strict';

app.controller('MapCtrl', function($scope, $element, $attrs, uiGmapIsReady, MapService) {
  var map;
  var routes = [];
  $scope.curRoute = {};

  //TODO
  var simpleRoute = {
    id: 1,
    name: 'Test Route',
    duration: '0',
    points: [
      {
        id: '1',
        position: '0',
        lat: 59.938600,
        lng: 30.31410
      },
      {
        id: '2',
        position: '1',
        lat: 59.938,
        lng: 30.38
      },
      {
        id: '3',
        position: '2',
        lat: 59.94,
        lng: 30.45
      }
    ],
    data: [
      {
        id: '1',
        type: 'discription',
        content: 'some text.',
        lat: 59.93863,
        lng: 30.31413
      }
    ]
  };

  var newRoute = {
    id: 2,
    name: 'New Route',
    duration: '0',
    points: [
      {
        id: '1',
        position: '0',
        lat: 58.938600,
        lng: 29.31410
      },
      {
        id: '2',
        position: '1',
        lat: 58.938,
        lng: 29.38
      },
      {
        id: '3',
        position: '2',
        lat: 58.94,
        lng: 29.45
      }
    ]
  };

  //TODO
  //routes.push(simpleRoute);
  //routes.push(newRoute);

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
    !$scope.curRoute.path && setPolyline();

    var position = evt.latLng;
    var path = $scope.curRoute.path.getPath();

    // Because path is an MVCArray, we can simply append a new coordinate
    // and it will automatically appear.
    path.push(position);
  }

  function routeToArray(route) {
    return _.map(route.getPath().j, (latLng, index) => _.extend(latLng.toJSON(), { position: index }));
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

  //TODO
  $scope.createRoute = function() {
    _.each(routes, route => renderRoute(route));
  };

  $scope.saveRoute = function() {
    var points = JSON.stringify(routeToArray($scope.curRoute.path));

    MapService.create(points)
      .then(route => {
        routes[route.id] = $scope.curRoute;
        //TODO handle created route fields.
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
    $scope.curRoute.path && $scope.curRoute.path.getPath().clear();

    $scope.curRoute = {};
  };

  $scope.removeRoute = function(route) {
    MapService.remove(route.id)
      .then(() => {
        route.getPath().clear();

        delete routes[route.id];
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

      //TODO
      $scope.routes = routes;

      MapService.fetchAll()
        .then(res => {
          routes = _.map(res, route => renderRoute(route));

          $scope.routes = routes;
        });
    });
});