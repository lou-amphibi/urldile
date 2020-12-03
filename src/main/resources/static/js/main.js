var app = angular.module("Urldile", []);

app.controller("UrldileController", function ($scope, $http) {

    $scope.customerLink = {
        url: ""
    };

    $scope.submitLongUrl = function () {
        $http({
            method: "POST",
            url: "http://localhost:8080/api/eatLink",
            data: angular.toJson($scope.customerLink),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(
            function (response) {
                $scope.urldileResponse = response.data;
        },  function (error) {
                $scope.urldileResponse = error.data;
            })
    };
});



