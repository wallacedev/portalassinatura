angular.module('portal', ['ngAnimate', 'ngSanitize', 'ui.bootstrap', 'ngFileUpload']);

angular.module('portal').controller('assinatura', function($scope, $uibModal, $log, $document, $http, Upload) {

	var $ctrl = this;
	$ctrl.items = ['item1', 'item2', 'item3'];
	$ctrl.animationsEnabled = true;
	
	$scope.linhas = [{index: 0, file: 'file0', filename:'', filetype:'', politicatipo:'', politicasubtipo:''}];
	$scope.nome = "teste nome";
	$scope.file1;
	
	
	var indice = 1;
	
	$scope.add = function () {
		if($scope.linhas.length < 10){
			var i = indice;
			$scope.linhas.push({
				index: i, file: "file"+i, filename:'', filetype:'', politicatipo:'', politicasubtipo:'' 
			})
			indice = indice+1;
		}else {
			alert("Não é possível adicionar mais arquivos.");
		}
	};
	
	$scope.remove = function(index){
		if ($scope.linhas.length > 1){
			$scope.linhas.splice( index, 1 );		
		}
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
		//Faz o upload para obter o hash
		$scope.getHash(file[0], index);
		$scope.$apply();
	};
	
	$scope.getHash = function(file, index){
		$scope.uploadFiles(file, null, index);
	};
	
	$scope.uploadFiles = function(file, errFiles, index) {
        $scope.f = file;
        $scope.errFile = errFiles && errFiles[0];
        if (file) {
            file.upload = Upload.upload({
                url: 'rest/fileupload',
                data: {file: file}
            });

            file.upload.then(function (response) {
            	$scope.linhas[index].hash = response.data;
            	$scope.$apply();
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
		$scope.linhas[0].identificador = $scope.identificador;
		alert("Digite sua assinatura no dispositivo do certillion");
		
		//enviar json de configuração dos arquivos
		$http({
			  method: 'POST',
			  url: 'rest/assinatura',
			  params : {
			        conteudo: $scope.linhas
			        
			  }
			}).then(function successCallback(response) {
			    // this callback will be called asynchronously
			    // when the response is available
				if(response.data != "false"){
					alert("Assinatura realizada com sucesso!");
					$scope.linhas = "";
					$scope.linhas = response.data;
				}
				else{
					alert("Erro na assinatura!");
				}
				$scope.$apply();
			  }, function errorCallback(response) {
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.
				  $scope.message = "Erro no servidor.";
		});
		
	};
	
	$scope.downloadFile = function(fileName){
		
	    var a = document.createElement("a");
	    document.body.appendChild(a);
	    console.log(fileName);
		$http({
			  method: 'POST',
			  url: 'rest/download',
			  responseType: "arraybuffer",
			  params : {
			        filename: fileName
			        
			  }
			  
			}).then(function successCallback(response) {
			    // this callback will be called asynchronously
			    // when the response is available
				
				console.log("downloadPDF callback");
		        var file = new Blob([response.data], {type: 'application/pdf'});
		        var fileURL = URL.createObjectURL(file);
		        a.href = fileURL;
		        a.download = fileName;
		        a.click();
				
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
				$uibModalInstance.close();
			};

			$scope.cancel = function() {
				$uibModalInstance.dismiss('cancel');
			};
		});