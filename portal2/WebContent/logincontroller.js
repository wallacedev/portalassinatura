var app = angular.module('portal', []);


app.controller('login', ['$scope', '$http', '$location', '$window', function($scope, $http, $location, $window) {

	$scope.message = "";
	
	$scope.update = function(){
		// Simple GET request example:
		var usuario = $scope.user;
		var senha = $scope.password;
		$http({
			  method: 'POST',
			  url: 'rest/login',
			  params : {
			        user: usuario,
			        password: senha
			  }
			}).then(function successCallback(response) {
			    // this callback will be called asynchronously
			    // when the response is available
				if(response.data == "true"){
					$window.location.href = "assinatura.html";
				}
				else{
					$scope.message = "Login e/ou senha incorretos.";
				}
			  }, function errorCallback(response) {
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.
				  $scope.message = "Erro no servidor.";
			  });
		
		
		}
}]);