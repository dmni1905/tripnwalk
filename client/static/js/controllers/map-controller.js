'use strict';

app.controller('MapCtrl', function($scope, $element, $attrs, uiGmapIsReady, MapService, $uibModal) {
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

  function getRouteById(id) {
    return _.find($scope.routes, route => route.id == id);
  }

  $scope.openDeletionModal = id => {
    $scope.curRoute = getRouteById(id);

    $scope.modalInstance = $uibModal.open({
      templateUrl: 'templates/deletion-modal.html',
      scope: $scope
    });
  };

  $scope.saveRoute = function(removeForm) {
    //TODO validation.
    var route = _.omit($scope.curRoute, 'path');
    route.points = _.map(route.points, point => _.omit(point, '$$hashKey'));

    MapService.create(route)
      .then(route => {
        $scope.routes.push(_.extend($scope.curRoute, { id: route.id }));

        removeForm();
      });
  };

  $scope.updateRoute = function() {
    var route = _.omit($scope.curRoute, 'path');

    MapService.update(route)
      .then(route => {
        _.extend(getRouteById(route.id), route);
      });
  };

  $scope.clearRoute = function() {
    if (_.contains($scope.routes, $scope.curRoute)) {
      $scope.curRoute.path && $scope.curRoute.path.getPath().clear();
    }

    $scope.curRoute = {};
  };

  $scope.removeRoute = function(route) {
    MapService.remove(route.id)
      .then(() => {
        $scope.routes.splice($scope.routes.indexOf($scope.curRoute), 1);
        $scope.clearRoute();
        $scope.modalInstance.dismiss();

        delete $scope.modalInstance;
      });
  };

  $scope.removePoint0 = (id, index) => {
    var route = getRouteById(id);

    route.path.getPath().clear();

    route.points.splice(index, 1);

    renderRoute(route);
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