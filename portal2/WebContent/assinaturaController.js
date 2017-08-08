angular.module('portal', ['ngAnimate', 'ngSanitize', 'ui.bootstrap']);

angular.module('portal').controller('assinatura', function($scope, $uibModal, $log, $document, $http) {

	var $ctrl = this;
	$ctrl.items = ['item1', 'item2', 'item3'];
	$ctrl.animationsEnabled = true;
	
	$scope.linhas = [{index: 0, file: 'file0', filecontent:'', filetype:'', politicatipo:'', politicasubtipo:''}];
	$scope.nome = "teste nome";
	
	var indice = 1;
	
	$scope.add = function () {
		var i = indice;
		$scope.linhas.push({
			index: i,
			file: "file"+i,
		})
		indice = indice+1;
	};
	
	$scope.remove = function(index){				
		$scope.linhas.splice( index, 1 );		
	};

	$scope.fileNameChanged = function(ele) {
		var tipo = ele.files[0].type;
	
		var index = ele.name;
		$scope.linhas[index].filecontent = ele.files[0].name;
		$scope.linhas[index].filetype = ele.files[0].type;
		$scope.linhas[index].politicasubtipo = "padrao";
		if (tipo == "application/pdf") {
			$scope.linhas[index].politicatipo = "pdf";
			$scope.linhas[index].estado = "pdf";
		} else if (tipo == "text/xml") {
			$scope.linhas[index].politicatipo = "xml";
			$scope.linhas[index].estado = "xml";
		} else {
			$scope.linhas[index].politicatipo = "cms";
			$scope.linhas[index].estado = "cms";
		}
		$scope.$apply();

	};
	
	$scope.assinar = function(){
		$http({
			  method: 'POST',
			  url: 'rest/assinatura',
			  params : {
			        conteudo: $scope.linhas
			        
			  }
			}).then(function successCallback(response) {
			    // this callback will be called asynchronously
			    // when the response is available
				if(response.data == "true"){
					alert("true");
				}
				else{
					alert("false");
				}
			  }, function errorCallback(response) {
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.
				  $scope.message = "Erro no servidor.";
		});
		
	};

	$ctrl.politica = function(index, size, parentSelector) {
		var parentElem = parentSelector ? angular.element($document[0]
				.querySelector('.modal-demo ' + parentSelector))
				: undefined;

		var modalInstance = $uibModal.open({
			animation : $ctrl.animationsEnabled,
			ariaLabelledBy : 'modal-title',
			ariaDescribedBy : 'modal-body',
			templateUrl : 'myModalContent.html',
			controller : 'ModalInstanceCtrl',
			controllerAs : '$ctrl',
			size : size,
			appendTo : parentElem,
			resolve : {
				linha : function() {
					return $scope.linhas[index];
				}
			}
		});

		modalInstance.result.then(function(selectedItem) {
			$ctrl.selected = selectedItem;

		}, function() {
			$log.info('Modal dismissed at: ' + new Date());
		});
	};
		
});

angular.module('portal').controller('ModalInstanceCtrl',
		function($scope, $uibModalInstance, linha) {

			$scope.linha = linha;
			
			$scope.radiochange = function() {
				$scope.linha.politicasubtipo = "padrao";
			};

			$scope.controllcms = "";
			$scope.controllxml = "";
			$scope.controllpdf = "";
			
			if (linha.estado == "pdf") {
				$scope.ctrldivxml = "disabled";
				$scope.ctrlradioxml = true;
			}
			if (linha.estado == "xml") {
				$scope.ctrldivpdf = "disabled";
				$scope.ctrlradiopdf = true;
			}
			if (linha.estado == "cms") {
				$scope.ctrldivxml = "disabled";
				$scope.ctrlradioxml = true;
				$scope.ctrldivpdf = "disabled";
				$scope.ctrlradiopdf = true;
			}

			$scope.ok = function() {
				$uibModalInstance.close($ctrl.selected.item);
			};

			$scope.cancel = function() {
				$uibModalInstance.dismiss('cancel');
			};
		});