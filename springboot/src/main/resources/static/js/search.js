var search = angular.module('search', []);

searchResults = function($scope, $http) {
    $scope.events = [];

    $scope.search = function() {
        $http.get('/rest/search/?q=' + $scope.searchKey).success(function(data) {
            try {
                if (data.count > 0) {
                    $scope.events = data.results;
                } else {
                    $scope.events = [];
                }
            } catch(err) {
                console.log(err);
            }
        })
    }
};
search.controller("searchResults", searchResults);
