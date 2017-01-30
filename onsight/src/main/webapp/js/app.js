(function() {
    
    var app = angular.module('onsight', ['ngMessages']);     

    app.controller('onsightCtrl', ['$scope', '$http', '$timeout','$window', function($scope, $http,$timeout,$window){
        var onsightCtrl = this;

        this.currTab = "home";
        this.user = null;
        this.alertMsg="";
        this.adminPermission=false;
        this.userPermission=false;

        $scope.signupResMsg = "" ;
        $scope.signinResMsg = "" ;

        this.httpHeaders = 
        {
            headers : {
                'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8;'
            }
        }

        this.getCurrTab = function(){
            return onsightCtrl.currTab;
        }

        this.setCurrTab = function(currTab){
            onsightCtrl.currTab = currTab;
        }

        this.hasRole = function(role){
            for (var i = 0; i < onsightCtrl.user.roles.length; i++) {
                if(onsightCtrl.user.roles[i] === role)
                    return true;
            };
            return false;
        }

        this.setPermissions = function(){
            if(onsightCtrl.hasRole('user')){
                onsightCtrl.userPermission = true;
            }
            if(onsightCtrl.hasRole('admin')){
                onsightCtrl.adminPermission = true;
            }
        }

        this.getUserInfo = function(){
            $http.get('user_info').success(function(response) {
                if(response.result === true){
                    onsightCtrl.user = response.userInfo;
                    onsightCtrl.setPermissions();
                }else{
                    onsightCtrl.alertMsg="an error occured during getting user info: " + response.message ;
                    $timeout(function(){
                      onsightCtrl.alertMsg = "";
                    }, 5000)
                }
            }).error(function(error){                
                    onsightCtrl.alertMsg="an error occured during getting user info: " + "connection error" ;
                    $timeout(function(){
                      onsightCtrl.alertMsg = "";
                    }, 5000)
            });

        };

        this.signup = function(){;
            if($scope.password !== $scope.repeatPassword){
                $scope.signupResMsg = "password doesn't match" ;
                $scope.password = "" ;
                $scope.repeatPassword = "" ;
            }else{
                var data = $.param({
                    username: $scope.username,
                    name: $scope.name,
                    family: $scope.family,
                    password: $scope.password
                });
                $http.post('signup',data,onsightCtrl.httpHeaders
                    ).success(function(response){
                        if(response.result === true){
                            $window.location.href = 'home.html';
                        }else{
                            $scope.signupResMsg = response.message;
                        }
                    }).error(function(error){                
                        $scope.signupResMsg = error ;
                });            
            }
        }

        this.login = function(){
            var data = $.param({username: $scope.username,password: $scope.password});
            $http.post('login',data,onsightCtrl.httpHeaders
                    ).success(function(response){

                        if(response.result === true){
                            $window.location.href = 'home.html';
                        }else{
                            $scope.signinResMsg = response.message;
                        }
                    }).error(function(error){                
                        $scope.signinResMsg = error ;
                    });
        }

        onsightCtrl.getUserInfo();

    }]);


    app.controller('CreateController', function($scope) {
        $scope.subject = "";
        $scope.descr = "";
        $scope.choices = [];

        $scope.addChoice = function() {
            var newItemNo = $scope.choices.length + 1;
            $scope.choices.push({'id': newItemNo});
        };

        $scope.createPoll = function() {
            var newPoll = {
                id: 0,  // to be set by the server
                title: $scope.subject,
                description: $scope.descr,
                thumb: "default.png",
                choices: [],
                votes: []
            };
            for (var i = 0; i < $scope.choices.length; i++) {
                newPoll.choices.push($scope.choices[i].name);
                newPoll.votes.push(0);
            };

            $scope.$emit('pollCreated', newPoll);
            angular.element("#createPollDialog").modal("hide");
        };

        $scope.moveUp = function(choiceIdx) {
            if (choiceIdx === 0)
                return;
            var temp = $scope.choices[choiceIdx-1];
            $scope.choices[choiceIdx-1] = $scope.choices[choiceIdx];
            $scope.choices[choiceIdx] = temp;
        };

        $scope.moveDn = function(choiceIdx) {
            if (choiceIdx >= $scope.choices.length - 1)
                return;
            var temp = $scope.choices[choiceIdx+1];
            $scope.choices[choiceIdx+1] = $scope.choices[choiceIdx];
            $scope.choices[choiceIdx] = temp;
        };
        $scope.deleteChoice = function(choiceIdx) {
            if (choiceIdx < 0 || choiceIdx >= $scope.choices.length)
                return;
            $scope.choices.remove(choiceIdx);
        };
    });
})();
