angular.module('JWTDemoApp')
// Creating the Angular Controller
    .controller('ProfileController', function($http, $scope, AuthService) {
        $scope.user = AuthService.user;
        $scope.buttonText = 'CREATE';

        var init = function(userProfile) {
            $http.get('api/profile').success(function(res) {
                $scope.users = res;

                $scope.userForm.$setPristine();
                $scope.message='';
                $scope.userProfile = userProfile;
                $scope.buttonText = 'CREATE';
            }).error(function(error) {
                $scope.message = error.message;
            });
        };
        $scope.initEdit = function(userProfile) {
            $scope.userProfile = userProfile;
            $scope.message='';
            $scope.buttonText = 'CREATE';
        };
        var editUser = function(){
            $http.put('api/profile', $scope.userProfile).success(function(res) {
                $scope.userProfile = null;
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
