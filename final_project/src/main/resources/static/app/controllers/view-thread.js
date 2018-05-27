angular.module('JWTDemoApp')
// Creating the Angular Controller
    .controller('View-threadController', function($http, $scope, AuthService) {
        var edit = true;
        $scope.buttonText = 'Create step';
        $scope.nameurl =  'Submit';
        $scope.user = AuthService.user;

        //For comments
        $(document).ready(function () {

            $('.star').on('click', function () {
                $(this).toggleClass('star-checked');
            });

            $('.ckbox label').on('click', function () {
                $(this).parents('tr').toggleClass('selected');
            });

            $('.btn-filter').on('click', function () {
                var $target = $(this).data('target');
                if ($target != 'all') {
                    $('.table tr').css('display', 'none');
                    $('.table tr[data-status="' + $target + '"]').fadeIn('slow');
                } else {
                    $('.table tr').css('display', 'none').fadeIn('slow');
                }
            });

        });

        //For everything else
        var init = function() {

            var currentLocation = window.location.toString().split('/')[5];

            $http.get('view-thread/' + currentLocation ).success(function(res) {
                $scope.instruction = res;

                $scope.message='';
                $scope.appUser = null;
                // $scope.buttonText = 'Create';

            }).error(function(error) {
                $scope.message = error.message;
            });
        };
        $scope.initEdit = function(step) {
            edit = true;
            $scope.step = step;
            $scope.message='';
            // $scope.buttonText = 'Update';
        };
        $scope.initAddUser = function() {
            edit = false;
            $scope.instruction = null;

            $scope.message='';
            // $scope.buttonText = 'Create';
        };
        $scope.deleteUser = function(step) {
            var currentLocation = window.location.toString().split('/')[5];
            $http.delete('view-thread/'+ currentLocation + '/' + step.id + '/' + AuthService.user.id).success(function(res) {
                $scope.deleteMessage ="Success!";
                init();
            }).error(function(error) {
                $scope.deleteMessage = error.message;
            });
        };
        var editUser = function(){
            var currentLocation = window.location.toString().split('/')[5];
            $http.put('view-thread/' + currentLocation, $scope.step).success(function(res) {
                $scope.step = null;
                $scope.confirmPassword = null;
                $scope.userForm.$setPristine();
                $scope.message = "Editting Success";
                init();
            }).error(function(error) {
                $scope.message =error.message;
            });
        };
        var addUser = function(){
            var currentLocation = window.location.toString().split('/')[5];
            $http.post('view-thread/' + currentLocation, $scope.instructionComment).success(function(res) {
                $scope.instructionComment = null;
                $scope.confirmPassword = null;
                $scope.commentForm.$setPristine();
                $scope.message = "Comment Created";
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
        $scope.submitComment = function () {
            addUser();
        }
        init();

    });
