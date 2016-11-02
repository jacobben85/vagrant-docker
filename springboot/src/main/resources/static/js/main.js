var routerApp = angular.module('routerApp', ['ui.router']);

routerApp.config(function($stateProvider, $urlRouterProvider) {
    console.log("in router config")
    $urlRouterProvider.otherwise('/');
    $stateProvider
    .state('', {
        url: '/',
        templateUrl: '/assets/three.html',
        controller: function($scope) {
            console.log("controller default")
            load("teapot");
        }
    })
    .state('about', {
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
});

load = function(filename) {
    console.log("load function triggered")
    var obj = loadFile ('assets/' + filename + '.obj', document.body);
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