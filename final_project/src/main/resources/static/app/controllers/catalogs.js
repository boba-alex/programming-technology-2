angular.module('JWTDemoApp')
// Creating the Angular Controller
    .controller('InstructionsController', function($http, $scope, AuthService) {
        var edit = false;
        $scope.buttonText = 'Create';

        var init = function() {
            $http.get('catalogs').success(function(res) {
                $scope.catalogs = res;
                $scope.message='';
                $scope.appUser = null;
                $scope.buttonText = 'Create';

            }).error(function(error) {
                $scope.message = error.message;
            });
        };
        $scope.initEdit = function(catalog) {
            edit = true;
            $scope.catalog = catalog;
            $scope.message='';
            $scope.buttonText = 'Update';
        };
        $scope.initAddUser = function() {
            edit = false;
            $scope.catalog = null;

            $scope.message='';
            $scope.buttonText = 'Create';
        };
        $scope.deleteUser = function(catalog) {
            $http.delete('catalogs/'+catalog.id).success(function(res) {
                $scope.deleteMessage ="Success!";
                init();
            }).error(function(error) {
                $scope.deleteMessage = error.message;
            });
        };
        var editUser = function(){
            $http.put('catalogs', $scope.catalog).success(function(res) {
                $scope.catalog = null;
                $scope.confirmPassword = null;
                $scope.userForm.$setPristine();
                $scope.message = "Editting Success";
                init();
            }).error(function(error) {
                $scope.message =error.message;
            });
        };
        var addUser = function(){
            $http.post('catalogs', $scope.catalog).success(function(res) {
                $scope.catalog = null;
                $scope.confirmPassword = null;
                $scope.userForm.$setPristine();
                $scope.message = "User Created";
                init();
            }).error(function(error) {
                $scope.message = error.message;
            });
        };
        $scope.submit = function() {
            if(edit){
                editUser();
            }else{
                addUser();
            }
        };
        init();

    });
