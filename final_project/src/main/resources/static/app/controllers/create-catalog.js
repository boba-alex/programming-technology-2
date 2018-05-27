angular.module('JWTDemoApp')
// Creating the Angular Controller
    .controller('CreateInstructionController', function($http, $scope, AuthService) {
        $scope.user = AuthService.user;
        $scope.buttonText = 'CREATE';

        var init = function(catalog) {
            $http.get('api/create-catalog').success(function(res) {
                $scope.users = res;

                $scope.userForm.$setPristine();
                $scope.message='';
                $scope.catalog = catalog;
                $scope.buttonText = 'CREATE';
            }).error(function(error) {
                $scope.message = error.message;
            });
        };
        $scope.initEdit = function(catalog) {
            $scope.catalog = catalog;
            $scope.message='';
            $scope.buttonText = 'CREATE';
        };
        var editUser = function(){
            $http.put('api/create-catalog', $scope.catalog).success(function(res) {
                $scope.catalog = null;
                $scope.confirmPassword = null;
                $scope.userForm.$setPristine();
                $scope.message = "Editting Success";
                init();
            }).error(function(error) {
                $scope.message = error.message;
            });
        };
        $scope.submit = function() {
            editUser();
        };
        init();

    });
