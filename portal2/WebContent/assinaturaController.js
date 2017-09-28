angular.module('portal', ['ngAnimate', 'ngSanitize', 'ui.bootstrap', 'ngFileUpload']);

angular.module('portal').controller('assinatura', function($scope, $uibModal, $log, $document, $http, Upload) {

	var $ctrl = this;
	$ctrl.items = ['item1', 'item2', 'item3'];
	$ctrl.animationsEnabled = true;
	
	$scope.linhas = [{index: 0, file: 'file0', filename:'', filetype:'', politicatipo:'', politicasubtipo:'', file1:''}];
	$scope.nome = "teste nome";
	$scope.file1;
	
	
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
		$scope.linhas[index].filename = ele.files[0].name;
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
		var fileName = $scope.linhas[index].file;
		var file = document.getElementById(fileName).files;
		var qtd = file.length;
		var reader = new FileReader();
		reader.readAsDataURL(file[0]);
		reader.onload = function () {
			$scope.linhas[index].base64 = result;
			//$scope.linhas[index].filename = reader.result;
//			alert(reader.result);
//			$scope.mudaNome(reader.result, index);
			$scope.$apply();
		};
		$scope.$apply();

	};
	
	$scope.mudaNome = function(result, index) {
//		alert(result);
//		alert(index);
		$scope.linhas[index].base64 = result;
		$scope.$apply();
		alert($scope.linhas[index].base64);
//		$scope.linhas[index].file1 = result;
	};
	
	$scope.uploadFiles = function(file, errFiles) {
        $scope.f = file;
        $scope.errFile = errFiles && errFiles[0];
        if (file) {
            file.upload = Upload.upload({
                url: 'rest/fileupload',
                data: {file: file}
            });

            file.upload.then(function (response) {
                $timeout(function () {
                    file.result = response.data;
                });
            }, function (response) {
                if (response.status > 0)
                    $scope.errorMsg = response.status + ': ' + response.data;
            }, function (evt) {
                file.progress = Math.min(100, parseInt(100.0 * 
                                         evt.loaded / evt.total));
            });
        }   
    }
	
	$scope.assinar = function(){
		
		var file;
		$scope.errFile = "";
		var lista = $scope.linhas;
		var size = lista.length;
		//upload dos arquivos
		for(var i=0; i<size; i++){
			file = document.getElementById("file"+i).files[0];
			//upload de 1 arquivo
			if (file) {
				file.upload = Upload.upload({
					url: 'rest/fileupload',
					data: {file: file}
				});
				
				file.upload.then(function (response) {
					$timeout(function () {
						file.result = response.data;
					});
				}, function (response) {
					if (response.status > 0)
						$scope.errorMsg = response.status + ': ' + response.data;
				}, function (evt) {
					file.progress = Math.min(100, parseInt(100.0 * 
							evt.loaded / evt.total));
				});
			} 
		}
		
		//enviar json de cnfiguração dos arquivos
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
	
$scope.assinar2 = function(){
		
	file = document.getElementById("file0").files[0];
	//upload de 1 arquivo
	if (file) {
		file.upload = Upload.upload({
			url: 'rest/fileupload',
			data: {file: file}
		});
		
		file.upload.then(function (response) {
			$timeout(function () {
				file.result = response.data;
			});
		}, function (response) {
			if (response.status > 0)
				$scope.errorMsg = response.status + ': ' + response.data;
		}, function (evt) {
			file.progress = Math.min(100, parseInt(100.0 * 
					evt.loaded / evt.total));
		});
	} 
		
};
	
		
		
//		// Upar a lista de arquivos
//		var lista = $scope.linhas;
//		var size = lista.length;
//		for(var i=0; i<size; i++){
//			var arquivo = lista[i].filecontent;
//			$http({
//				  method: 'POST',
//				  url: 'rest/fileupload',
//				  params : {
//				        conteudo: $scope.linhas
//				        
//				  }
//				}).then(function successCallback(response) {
//				    // this callback will be called asynchronously
//				    // when the response is available
//					if(response.data == "true"){
//						alert("true");
//					}
//					else{
//						alert("false");
//					}
//				  }, function errorCallback(response) {
//				    // called asynchronously if an error occurs
//				    // or server returns response with an error status.
//					  $scope.message = "Erro no servidor.";
//			});
//		}
//		
//		
//		$http({
//			  method: 'POST',
//			  url: 'rest/assinatura',
//			  params : {
//			        conteudo: $scope.linhas
//			        
//			  }
//			}).then(function successCallback(response) {
//			    // this callback will be called asynchronously
//			    // when the response is available
//				if(response.data == "true"){
//					alert("true");
//				}
//				else{
//					alert("false");
//				}
//			  }, function errorCallback(response) {
//			    // called asynchronously if an error occurs
//			    // or server returns response with an error status.
//				  $scope.message = "Erro no servidor.";
//		});
//		
//	};

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
				$uibModalInstance.close();
			};

			$scope.cancel = function() {
				$uibModalInstance.dismiss('cancel');
			};
		});