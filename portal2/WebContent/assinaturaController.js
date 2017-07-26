angular.module('portal', ['ngAnimate', 'ngSanitize', 'ui.bootstrap']);

angular.module('portal').controller('assinatura', function($scope, $uibModal, $log, $document) {

	var $ctrl = this;
	$ctrl.items = ['item1', 'item2', 'item3'];
	$ctrl.animationsEnabled = true;
	
	$scope.linhas = [{index: 0, file: 'file0', filecontent:'', filetype:'', politicatipo:'', politicasubtipo:''}];
	$scope.nome = "teste nome";
	
	$scope.add = function () {
		var i = $scope.linhas.length;
		$scope.linhas.push({
			index: i,
			file: "file"+i,
			nome: 'primeiro nome',
			politica: 'primeira politica'
		})
	};
	
	$scope.remove = function(index){				
		$scope.linhas.splice( index, 1 );		
	};
	
	$scope.fileNameChanged = function (ele) {
		var tipo = ele.files[0].type;
		if(tipo != "application/pdf" &&
		   tipo != "application/cms" &&	
		   tipo !=  "application/vnd.sealed-xls"){
			alert ("Formato do arquivo selecionado inválido");
		}else{
			var index = ele.name;  
			$scope.linhas[index].filecontent = ele.files[0].name;
			$scope.linhas[index].filetype = ele.files[0].type;
			$scope.$apply();
		}
	};

	
	$scope.assinar = function(){
		alert("Chamar servico Java Rest para assinar");
		
	};
	
	$ctrl.politica = function (index, size, parentSelector) {
		//$scope.linhas[index].filetype = type;
		var parentElem = parentSelector ? 
	      angular.element($document[0].querySelector('.modal-demo ' + parentSelector)) : undefined;
	    var modalInstance = $uibModal.open({
	      animation: $ctrl.animationsEnabled,
	      ariaLabelledBy: 'modal-title',
	      ariaDescribedBy: 'modal-body',
	      templateUrl: 'myModalContent.html',
	      controller: 'ModalInstanceCtrl',
	      controllerAs: '$ctrl',
	      size: size,
	      appendTo: parentElem,
	      resolve: {
	        items: function () {
	          return $ctrl.items;
	        },
	        linha: function (){
	          return $scope.linhas[index];	
	        }
	      }
	    });
	    
	    modalInstance.result.then(function (selectedItem) {
	        $ctrl.selected = selectedItem;
	        
	      }, function () {
	        $log.info('Modal dismissed at: ' + new Date());
	      });
	 };
		
});

angular.module('portal').controller('ModalInstanceCtrl', function ($scope, $uibModalInstance, items, linha) {
	  
	
	
	var $ctrl = this;
	  $ctrl.items = items;
	  $ctrl.selected = {
	    item: $ctrl.items[0]
	  };
	  
	  $scope.data = linha;
	  
	  $scope.radiochange = function(value){
			alert(value.value);
			
	  };
		
		$scope.filename = linha.filecontent;
		$scope.filetype = linha.filetype;

		$scope.controllcms = "";
		$scope.controllxls = "";
		$scope.controllpdf = "";
		
		
	 	if (linha.filetype == "application/pdf"){
	 		$scope.controllpdf = "disabled";
	 		$scope.radiopdf = true;
	 	}
	 	if (linha.filetype == "application/vnd.sealed-xls"){
	 		$scope.controllxls = "disabled";
	 		$scope.radioxls = true;
	 	}
	 	if (linha.filetype == "application/cms"){
	 		$scope.controllcms = "disabled";
	 		$scope.radiocms = true;
	 	}
		
	  $scope.ok = function () {
	    $uibModalInstance.close($ctrl.selected.item);
	  };

	  $scope.cancel = function () {
	    $uibModalInstance.dismiss('cancel');
	  };
});



/*
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

*/

