angular.module('JWTDemoApp')
// Creating the Angular Controller
    .controller('StepController', function ($http, $scope, AuthService) {
        var edit = true;
        $scope.buttonText = 'Create';
        $scope.nameurl = 'Submit';
        $scope.user = AuthService.user;

        //For everything else
        var init = function () {
            var currentLocation = window.location.toString().split('/')[5] + ' '
                + window.location.toString().split('/')[7];
            $http.get('/product/' + currentLocation).success(function (res) {
                $scope.step = res;

                $scope.message = '';
                $scope.appUser = null;
                $scope.buttonText = 'Create';

            }).error(function (error) {
                $scope.message = error.message;
            });
        };
        $scope.initEdit = function (instruction) {
            edit = true;
            $scope.instruction = instruction;
            $scope.message = '';
            $scope.buttonText = 'Update';
        };
        $scope.initAddUser = function () {
            edit = false;
            $scope.instruction = null;

            $scope.message = '';
            $scope.buttonText = 'Create';
        };
        $scope.deleteUser = function (block) {
            $http.delete('product/' + $scope.step.id + '/' + block.id + '/' + AuthService.user.id).success(function (res) {
                $scope.deleteMessage = "Success!";
                init();
            }).error(function (error) {
                $scope.deleteMessage = error.message;
            });
        };
        var editUser = function () {
            var currentLocation = window.location.toString().split('/')[5] + ' '
                + window.location.toString().split('/')[7];
            $http.put('product/' + $scope.step.id + '/' + AuthService.user.id, $scope.block).success(function (res) {
                $scope.block = null;
                $scope.confirmPassword = null;
                $scope.userForm.$setPristine();
                $scope.message = "Editting Success";
                init();
            }).error(function (error) {
                $scope.message = error.message;
            });
        };
        var addUser = function () {
            $http.post('product/' + $scope.step.id, $scope.stepComment).success(function (res) {
                $scope.stepComment = null;
                $scope.confirmPassword = null;
                $scope.userForm.$setPristine();
                $scope.message = "Comment Created";
                init();
            }).error(function (error) {
                $scope.message = error.message;
            });
        };
        $scope.submit = function () {
            if (edit) {
                editUser();
            } else {
                addUser();
            }
        };
        $scope.submitComment = function () {
            addUser();
        }
        init();

    });
