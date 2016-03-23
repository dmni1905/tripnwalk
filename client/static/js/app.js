var app = angular.module('App', ['uiGmapgoogle-maps']).config(
  ['uiGmapGoogleMapApiProvider', function(GoogleMapApiProviders) {
    GoogleMapApiProviders.configure({
      libraries: 'weather,geometry,visualization'
    });
  }]
);

app.controller('MapCtrl', function($scope, $element, $attrs, uiGmapIsReady) {
  $scope.map = {
    center: { latitude: 59.938600, longitude: 30.31410 },
    zoom: 11,
    options: {
      disableDefaultUI: true
    }
  };

  var map = uiGmapIsReady.promise(1).then(function(instances) {
    return instances[0].map;
  });

  map
    .then(map => {
      var connector;
      var connectors = [];
      var setPolyline = () => {
        connector = new google.maps.Polyline({
          strokeColor: '#000000',
          strokeOpacity: 1.0,
          strokeWeight: 3
        });

        connector.setMap(map);
      };

      map.addListener('click', setMarker);

      // Handles click events on a map, and adds a new point to the Polyline.
      function setMarker(evt) {
        !connector && setPolyline();

        var position = evt.latLng;
        var path = connector.getPath();

        // Because path is an MVCArray, we can simply append a new coordinate
        // and it will automatically appear.
        path.push(position);
      }

      $scope.addPath = function() {
        connectors.push(connector);
        setPolyline();
      };

      $scope.clearPath = function() {
        connector.getPath().clear();
        setPolyline();
      };

      $scope.undoPath = function() {
        var path = connector.getPath();

        path.length > 0 && path.pop();
      };

      // Special hack for map to be resized on slider toggle.
      $('#menu-toggle').on('click', function() {
        setTimeout(() => {
          var center = map.getCenter();
          google.maps.event.trigger(map, 'resize');
          map.setCenter(center);
        }, 320);
      });
    });
});