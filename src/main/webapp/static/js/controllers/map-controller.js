'use strict';

app.controller('MapCtrl', function($scope, $element, $attrs, uiGmapIsReady, MapService, $uibModal) {
  var routeMode = false;
  var markers = [];
  var map;

  // Current route to interact with.
  $scope.curRoute = {};
  // Array of existing routes.
  $scope.routes = [];
  // Map settings.
  $scope.map = {
    center: { latitude: 59.938600, longitude: 30.31410 },
    zoom: 13,
    options: {
      minZoom: 3,
      maxZoom: 18,
      disableDefaultUI: true
    }
  };

  // Route mode switch. If true, allows user to create or modify route.
  $scope.toggleRouteMode = state => routeMode = !!state || false;

  // Creates map polyline for current route.
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

  $scope.toggleRouteSelected = function(isSelected, route) {
    var route0 = route || $scope.curRoute;
    var selected = {
      strokeColor: '#7CFC00',
      strokeOpacity: 1.0,
      strokeWeight: 6
    };
    var notSelected = {
      strokeColor: '#000000',
      strokeOpacity: 1.0,
      strokeWeight: 3
    };

    route0.selected = !!isSelected || false;
    route0.path.setOptions(isSelected && selected || notSelected);
  };

  function renderMarker(position) {
    // Check if marker exists.

    if (_.find(markers, marker => marker.position == position)) return;

    var marker = new google.maps.Marker({
      position: position,
      icon: {
        path: google.maps.SymbolPath.CIRCLE,
        scale: 3
      },
      draggable: false,
      map: map
    });

    markers.push(marker);
  }

  // Handles click events on a map, and adds a new point to the Polyline.
  function setMarker(evt) {
    if (!routeMode) return;

    !$scope.curRoute.path && setPolyline();

    if (!$scope.curRoute.selected) {
      $scope.toggleRouteSelected(true);
    }

    var position = evt.latLng;
    var path = $scope.curRoute.path.getPath();

    // Because path is an MVCArray, we can simply append a new coordinate
    // and it will automatically appear.
    path.push(position);

    $scope.curRoute.points = routeToArray($scope.curRoute.path);

    renderMarker(position);

    $scope.$apply(position);
  }

  // Transforms route path of given route to array of coordinate objects,
  function routeToArray(route) {
    return _.map(route.getPath().j, (latLng, index) => {

      return {
        position: index,
        lat: latLng.lat().toFixed(6),
        lng: latLng.lng().toFixed(6)
      };
    });
  }

  // Renders given route and adds path object to it.
  function renderRoute(route) {
    var points = _.sortBy(route.points, point => point.position);
    var polyLine = _.map(points, latLng => {
      var point = new google.maps.LatLng(latLng.lat, latLng.lng);

      renderMarker(point);

      return point;
    });
    var path = new google.maps.Polyline({
      path: polyLine,
      strokeColor: '#000000',
      strokeOpacity: 1.0,
      strokeWeight: 3
    });

    path.setMap(map);

    return _.extend(route, { path: path });
  }

  function cleaRouteMarkers(route) {
    if (!route.path) return;

    _.each(route.path.getPath().j, latLng => {
      var marker = _.find(markers, marker => marker.position == latLng);

      marker && marker.setMap(null);

      markers = _.without(markers, marker);
    });
  }

  /**
   * Returns route by given id or undefined, if route does not exist.
   *
   * @param id
   */
  function getRouteById(id) {
    return _.find($scope.routes, route => route.id == id);
  }

  /**
   * Opens modal for route to be deleted.
   *
   * @param id Id of route to be deleted.
   */
  $scope.openDeletionModal = id => {
    $scope.curRoute = getRouteById(id);

    $scope.modalInstance = $uibModal.open({
      templateUrl: '/templates/deletion-modal.html',
      scope: $scope
    });
  };

  /**
   * Creates new route or updates existing one.
   *
   * @param route Route to be saved.
   * @param cb Operation callback.
   */
  $scope.saveRoute = function(route, cb) {
    var $input = $('#route-edit-window').find('.route-name');

    if ($input.val() === '') {
      $input.parent().addClass('has-error');

      return;
    }

    var route0 = _.omit(route, ['path', 'selected']);

    route.points = _.map(route.points, point => _.omit(point, '$$hashKey'));

    if (_.contains($scope.routes, route)) {
      MapService.update(route0)
        .then(route => {
          _.extend(getRouteById(route.id), route);

          cb();
        });
    }
    else {
      MapService.create(route0)
        .then(route0 => {
          $scope.routes.push(_.extend(route, { id: route0.id }));

          cb();
        });
    }

    $scope.toggleRouteSelected(false);
  };

  //
  $scope.clearRoute = function() {
    if (!_.contains($scope.routes, $scope.curRoute)) {
      var route = $scope.curRoute;

      cleaRouteMarkers(route);

      route.path && route.path.getPath().clear();
    }

    $scope.curRoute = {};
  };

  $scope.removeRoute = function() {
    MapService.remove($scope.curRoute.id)
      .then(() => {
        $scope.routes.splice($scope.routes.indexOf($scope.curRoute), 1);
        $scope.clearRoute();
        $scope.modalInstance.dismiss();

        delete $scope.modalInstance;
      });
  };

  $scope.removePoint0 = (id, index) => {
    var route = getRouteById(id);

    cleaRouteMarkers(route);
    route.path.getPath().clear();
    route.points.splice(index, 1);
    renderRoute(route);
  };

  $scope.getRender = function() {
    _.map($scope.routes, route => renderRoute(route));
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
      $scope.getRender();
    });
});