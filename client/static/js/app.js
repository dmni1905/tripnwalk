var app = angular.module('myApp', []);

app.controller('MapCtrl', function($scope, $element, $attrs) {
    $scope.addPath = function() {
        this.createPath();
    };

    $scope.clearPath = function() {
        this.removePath();
    };

    $scope.undoPath = function() {
        this.undoPath();
    };
});

app.directive('myMap', function() {
    // directive link function
    var link = function(scope, element, attrs) {
        var self = this;
        var map;
        var connector;
        var connectors = [];
        
        // map config
        var mapOptions = {
            center: new google.maps.LatLng(59.938600, 30.31410),
            zoom: 11,
            mapTypeId: google.maps.MapTypeId.ROADMAP,
            disableDefaultUI: true
        };
        
        // init the map
        function initMap() {
            if (map === void 0) {
                map = new google.maps.Map(element[0], mapOptions);
            }

            self.setPolyline();

            map.addListener('click', setMarker);
        }    
    
        //TODO Fix map not rerendering on container resize.
        $('map-wraper').change('resize', function() {
            var center = map.getCenter();
            google.maps.event.trigger(map, 'resize');
            map.setCenter(center); 
        })

        // Handles click events on a map, and adds a new point to the Polyline.
        function setMarker(evt) {
            var position = evt.latLng;
            var path = connector.getPath();

            // Because path is an MVCArray, we can simply append a new coordinate
            // and it will automatically appear.
            path.push(position);
        }

        setPolyline = function() {
            connector = new google.maps.Polyline({
                strokeColor: '#000000',
                strokeOpacity: 1.0,
                strokeWeight: 3
            })

            connector.setMap(map);
        }

        scope.createPath = function() {
            connectors.push(connector);

            self.setPolyline();

            return connectors;
        }

        scope.removePath = function() {
            connector.getPath().clear()

            self.setPolyline();
        }

        scope.undoPath = function() {
            var path = connector.getPath();

            path.length > 0 && path.pop();
        }

        // show the map
        initMap();
    };
    
    return {
        restrict: 'A',
        template: '<div id="gmaps"></div>',
        replace: true,
        controller: 'MapCtrl',
        link: link
    };
});