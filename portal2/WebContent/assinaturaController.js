var app = angular.module('portal', ['ui.bootstrap']);

app.controller('assinatura', ['$scope','$uibModal', function($scope, $uibModal) {

	$scope.linhas = [{nome: 'primeiro nome', politica: 'primeira politica'}];
		

	
	$scope.add = function () {
		$scope.linhas.push({
			nome: 'primeiro nome',
			politica: 'primeira politica'
		})
	};
	
	$scope.remove = function(name){				
		var index = -1;		
		var comArr = eval( $scope.linhas );
		for( var i = 0; i < comArr.length; i++ ) {
			if( comArr[i].name === name ) {
				index = i;
				break;
			}
		}
		if( index === -1 ) {
			alert( "Algo de errado ocorreu" );
		}
		$scope.linhas.splice( index, 1 );		
	};

	$scope.politica = function(){
		$uibModal.open({
			animation: 'true',
			ariaLabelledBy: 'modal-title-bottom',
			ariaDescribedBy: 'modal-body-bottom',
			templateUrl: 'myModalContent.html'
	    });
	}
	
	$scope.assinar = function(){
		alert("Chamar servico Java Rest para assinar");
		
	}
	

		
		
}]);

angular.module('portal').controller('ModalInstanceCtrl', function ($uibModalInstance) {
	  $scope.ok = function () {
	    $uibModalInstance.close();
	  };

	  $scope.cancel = function () {
	    $uibModalInstance.dismiss('cancel');
	  };
	});

$(function() {

	  // We can attach the `fileselect` event to all file inputs on the page
	  $(document).on('change', ':file', function() {
	    var input = $(this),
	        numFiles = input.get(0).files ? input.get(0).files.length : 1,
	        label = input.val().replace(/\\/g, '/').replace(/.*\//, '');
	    input.trigger('fileselect', [numFiles, label]);
	  });

	  // We can watch for our custom `fileselect` event like this
	  $(document).ready( function() {
	      $(':file').on('fileselect', function(event, numFiles, label) {

	          var input = $(this).parents('.input-group').find(':text'),
	              log = numFiles > 1 ? numFiles + ' files selected' : label;

	          if( input.length ) {
	              input.val(log);
	          } else {
	              if( log ) alert(log);
	          }

	      });
	  });
	  
	});