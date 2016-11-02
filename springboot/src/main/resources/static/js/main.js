var routerApp = angular.module('routerApp', ['ui.router']);

routerApp.config(function($stateProvider, $urlRouterProvider) {
    console.log("in router config")
    $urlRouterProvider.otherwise('/home');
    $stateProvider
    .state('home', home_loader("teapot"))
    .state('about', about);
});

home_loader = function(filename) {
    return ({
        url: '/home',
        templateUrl: '/assets/three.html',
        controller: home_controller
    });
}

home_controller = function($scope, $http) {
    console.log("controller default")
    //load(filename);
    $scope.search = function() {
        $http.get('/assets/sample.json').success(function(data) {
            try {
                if (data.count > 0) {
                    $scope.events = data.results;
                    setTimeout(function() {loadImage(data.results)}, 1000);
                } else {
                    $scope.events = [];
                }
            } catch(err) {
                console.log(err);
            }
        })
    }
    $scope.search();
}

loadImage = function(results) {
    for(var k in results) {
        console.log(angular.element(document).find("#image_" + results[k].id)[0])
        load("teapot", angular.element(document).find("#image_" + results[k].id)[0]);
    }
}

about = ({
    url: '/about',
    views: {
        '': { templateUrl: 'about.html' },
        'columnOne@about': {
            templateUrl: 'three.html',
            controller: function($scope) {
                load("teapot");
            }
        },
        'columnTwo@about': {
            templateUrl: 'three.html',
            controller: function($scope) {
                load("teapot");
            }
        }
    }
});

load = function(filename, element) {
    console.log("load function triggered")
    var obj = loadFile ('assets/' + filename + '.obj', element);
    obj.addEventListener ('ready', function () {
        // set initial size
        obj.dispatchEvent ({
            type: 'resize', width: 500, height: 400
        });
    });
    obj.addEventListener ('click', function (event) {
            // test click coords by adding small green cube there
            var cube = new THREE.Mesh (
            new THREE.BoxGeometry (0.5, 0.5, 0.5),
            new THREE.MeshBasicMaterial ({ color: 0xff00 })
        );
        cube.position.copy (event.point);
        obj.dispatchEvent ({
            type: 'add',
            object: cube
        });
    });
};