package br.gov.prodabel.portal.service;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Model.Conteudo;
import br.gov.pbh.certillion.DownloadArquivo;
import br.gov.pbh.certillion.api.ICPMException;
import br.gov.pbh.certillion.api.sync.AssinaAdobePDF;
import br.gov.pbh.certillion.api.sync.AssinaArquivo;
import br.gov.pbh.certillion.utils.AmbienteCertillion;
import br.gov.pbh.certillion.utils.CertillionStatus;
import br.gov.pbh.certillion.utils.vo.ArquivoInfo;


@Path("/assinatura")
public class AssinaturaService {
	
	public final AmbienteCertillion AMBIENTE = AmbienteCertillion.CERTILLION_PRODUCAO;
	public boolean statusAssinatura = true;
	public String userIdentifier = "wallace.teixeira@pbh.gov.br";
	public String identificadorAplicacao = "Portal da assinatura";
	public List<Conteudo> listaConteudo;
	@Context
	public ServletContext context;
	
	@POST
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response assinatura(@QueryParam("conteudo") List<String> list) {

		listaConteudo = jsonToList(list);
		userIdentifier = listaConteudo.get(0).getIdentificador();
		//assinarTeste(listaConteudo);
		verificaIdentificador();
		
		List<Conteudo> listaPDFPadrao = new ArrayList();
		List<Conteudo> listaPDFPades = new ArrayList();
		List<Conteudo> listaXMLPadrao = new ArrayList();
		List<Conteudo> listaXMLXades = new ArrayList();
		List<Conteudo> listaCMSPadrao = new ArrayList();
		List<Conteudo> listaCMSCades = new ArrayList();
		
		//Separa os arquivos recebidos em listas pelo tipo e politica selecionada
		if (listaConteudo != null) {
			if (listaConteudo.size() > 0 ) {
				Iterator<Conteudo> it = listaConteudo.iterator();
				while(it.hasNext()) {
					Conteudo conteudoTemp = it.next();
					
					//PDF
					if (conteudoTemp.getPoliticatipo().equals("pdf")) {
						if (conteudoTemp.getPoliticasubtipo().equals("padrao")) {
							listaPDFPadrao.add(conteudoTemp);
						}else if(conteudoTemp.getPoliticasubtipo().equals("pades")) {
							listaPDFPades.add(conteudoTemp);
						}
					}
					
					//XML
					else if (conteudoTemp.getPoliticatipo().equals("xml")) {
						if (conteudoTemp.getPoliticasubtipo().equals("padrao")) {
							listaXMLPadrao.add(conteudoTemp);
						}else if(conteudoTemp.getPoliticasubtipo().equals("xades")) {
							listaXMLXades.add(conteudoTemp);
						}
					}
					
					//CMS
					else if (conteudoTemp.getPoliticatipo().equals("cms")) {
						if (conteudoTemp.getPoliticasubtipo().equals("padrao")) {
							listaCMSPadrao.add(conteudoTemp);
						}else if(conteudoTemp.getPoliticasubtipo().equals("cades")) {
							listaCMSCades.add(conteudoTemp);
						}
					}
				}
			}
		}
				
		//Chama os metodos de assinatura de acordo com o conteudo das listas
		
		//PDF Padrao
		if (listaPDFPadrao.size()==1) {
			//single 
			assinaPDFSingle(listaPDFPadrao.get(0));
		}else if (listaPDFPadrao.size()>1) {
			//bath
			assinaPDFList(listaPDFPadrao);
		}
		
		//Pades
		if (listaPDFPades.size()==1) {
			//single 
			
			
		}else if (listaPDFPades.size()>1) {
			//bath
		}
		
		//XML Padrao
		if (listaXMLPadrao.size()==1) {
			//single 
			
		}else if (listaXMLPadrao.size()>1) {
			//bath
		}
		
		//Xades
		if (listaXMLXades.size()==1) {
			//single 
			
		}else if (listaXMLXades.size()>1) {
			//bath
		}
		
		
		//CMS Padrao
		if (listaCMSPadrao.size()==1) {
			//single 
			
		}else if (listaCMSPadrao.size()>1) {
			//bath
		}
		
		//CMS Cades
		if (listaCMSCades.size()==1) {
			//single 
			
		}else if (listaCMSCades.size()>1) {
			//bath
		}
		if (statusAssinatura == true) {
			return Response.status(200).entity(new Gson().toJson(listaConteudo)).build();
		}
		return Response.status(200).entity(new Gson().toJson("false")).build();
	}
	
	private void verificaIdentificador() {
		// TODO Auto-generated method stub
		
	}

	private void assinaPDFSingle(Conteudo conteudo) {
		String UPLOAD_PATH = context.getRealPath("/files/");
		ArquivoInfo arquivoInfo = conteudoToArquivoInfo(conteudo);
		File file = new File(UPLOAD_PATH);
		if(!file.exists()) {
			file.mkdirs();
		}
		try {
			@SuppressWarnings("unused")
			AssinaArquivo assinaArquivo = new AssinaAdobePDF(arquivoInfo, userIdentifier, identificadorAplicacao, AMBIENTE);
			arquivoInfo.setStreamAssinaturaAttached(new FileOutputStream(new File(UPLOAD_PATH + "/"+ arquivoInfo.getIdentificador() + "_" + arquivoInfo.getNomeArquivo())));  
			if (arquivoInfo.getStatusAssinatura() != null) {
				if(arquivoInfo.getStatusAssinatura() == CertillionStatus.SIGNATURE_VALID) {
					if (download(arquivoInfo)) {
						setNomeArquivoDownload(arquivoInfo);
					}
				} else statusAssinatura = false;
			}else {
				statusAssinatura = false;
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			statusAssinatura = false;
			e.printStackTrace();
		} catch (ICPMException e) {
			// TODO Auto-generated catch block
			statusAssinatura = false;
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			statusAssinatura = false;
			e.printStackTrace();
		}
	}
	
	private void assinaPDFList(List<Conteudo> lista) {
		List<ArquivoInfo> listaArquivoInfo = listToListArquivo(lista);
		try {
			AssinaArquivo assinaArquivo = new AssinaAdobePDF(listaArquivoInfo, userIdentifier, identificadorAplicacao, AMBIENTE);
			Iterator<ArquivoInfo> it = listaArquivoInfo.iterator();
			String UPLOAD_PATH = context.getRealPath("/files/");
			System.out.println("Arquivo gravado em: "+UPLOAD_PATH);
			File file = new File(UPLOAD_PATH);
			if(!file.exists()) {
				file.mkdirs();
			}
			while(it.hasNext()) {
				ArquivoInfo arquivoInfo = it.next();
				arquivoInfo.setStreamAssinaturaAttached(new FileOutputStream(new File(UPLOAD_PATH + "/"+ arquivoInfo.getIdentificador() + "_" + arquivoInfo.getNomeArquivo())));
				if (arquivoInfo.getStatusAssinatura() != null) {
					if(arquivoInfo.getStatusAssinatura() == CertillionStatus.SIGNATURE_VALID) {
						if (download(arquivoInfo)) {
							setNomeArquivoDownload(arquivoInfo);
						}
					}else {
						statusAssinatura = false;
					}
				}else {
					statusAssinatura = false;
				}
			}
		} catch (MalformedURLException e) {
			statusAssinatura = false;
			e.printStackTrace();
		} catch (ICPMException e) {
			statusAssinatura = false;
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			statusAssinatura = false;
			e.printStackTrace();
		}
	}
	
	private void setNomeArquivoDownload(ArquivoInfo arquivoInfo) {
		//listaConteudo.get(index)
		Iterator<Conteudo> it = listaConteudo.iterator();
		while(it.hasNext()) {
			Conteudo conteudoTemp = it.next();
			if(conteudoTemp.getFilename().equals(arquivoInfo.getNomeArquivo())) {
				listaConteudo.get(listaConteudo.indexOf(conteudoTemp)).setFiletodownload(arquivoInfo.getIdentificador()+"_"+arquivoInfo.getNomeArquivo());
				break;
			}
		}
	}

	private List<ArquivoInfo> listToListArquivo(List<Conteudo> lista) {
		List<ArquivoInfo> listArquivoInfo = new ArrayList();
		Iterator<Conteudo> it = lista.iterator();
		while(it.hasNext()) {
			listArquivoInfo.add(conteudoToArquivoInfo(it.next()));
		}
		return listArquivoInfo;
	}

	private List<Conteudo> jsonToList(List<String> list){
		List<Conteudo> listaConteudo = new ArrayList();
		Gson gson = new GsonBuilder().create();
		Iterator<String> it = list.iterator();
		while(it.hasNext()) {
			String jsonTemp = it.next();
			Conteudo conteudoTemp = gson.fromJson(jsonTemp, Conteudo.class);
			listaConteudo.add(conteudoTemp);
		}
		return listaConteudo;
	}
	
	private String listToJson(List<Conteudo> lista) {
		Gson gson = new GsonBuilder().create();
		gson.toJson(lista);
		return gson.toString();
	}
	
	private boolean download(ArquivoInfo arquivo) {
		boolean retorno = false;
		try {
			DownloadArquivo download = new DownloadArquivo();
			download.downloadArquivo(arquivo, AMBIENTE);
			if (arquivo.isSigned()) {
				System.out.println("STATUS - " + arquivo.isSigned());
				retorno = true;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return retorno;
	}

	private ArquivoInfo conteudoToArquivoInfo(Conteudo conteudo) {
		String UPLOAD_PATH = context.getRealPath("/files/");
		ArquivoInfo arquivoInfo = new ArquivoInfo();
		arquivoInfo.setHashArquivo(conteudo.getHash());
		arquivoInfo.setNomeArquivo(conteudo.getFilename());
//		try {
//			arquivoInfo.setStreamAssinaturaAttached(new FileOutputStream(UPLOAD_PATH + "/"+"fileAttached_"+conteudo.getFilename()));
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return arquivoInfo;
	}
	
	private void assinarTeste(List<Conteudo> listaConteudo) {

//		AssinaArquivo assinaArquivo = null;
//		String userIdentifier = "wallace.teixeira@pbh.gov.br";
//		String identificadorAplicacao = "Portal da assinatura";
//		ArquivoInfo arquivo = new ArquivoInfo();
//		//arquivo.setNomeArquivo(fileMetaData.getFileName());
//		//arquivo.setStreamArquivo(fileInputStream);
//		//arquivo.setIdentificador(1);
//		UploadArquivo uploadArquivo = null;
//		try {
//			arquivo.setStreamAssinaturaAttached(new FileOutputStream("teste.pdf"));
//			uploadArquivo = new UploadArquivo(arquivo.getStreamArquivo(), AmbienteCertillion.CERTILLION_PRODUCAO);
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ProtocolException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		if(uploadArquivo != null) { 
//			arquivo.setHashArquivo(uploadArquivo.getHashArquivoUpload());
//			System.out.println("Hash - "+arquivo.getHashArquivo());
//			
//			try {
//				assinaArquivo = new AssinaAdobePDF(arquivo, userIdentifier, identificadorAplicacao, AmbienteCertillion.CERTILLION_PRODUCAO);
//				
//			} catch (MalformedURLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (ICPMException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		
//		try {
//			DownloadArquivo download = new DownloadArquivo(arquivo, AmbienteCertillion.CERTILLION_PRODUCAO);
//			
//			if (arquivo.isSigned()) {
//				System.out.println("STATUS - " + arquivo.isSigned());
//			}
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ProtocolException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}
	
	private void assinarTesteAssincrono() {
//		try {
//			ArquivoInfo arquivoInfo = new ArquivoInfo();
//			arquivoInfo.setStreamArquivo(new FileInputStream(pathEntrada + File.separator + arquivoPdf));
//			uploading(arquivoInfo);
//			System.out.println(arquivoPdf + " - " + arquivoInfo.getHashArquivo());
//			arquivoInfo.setNomeArquivo(arquivoPdf);
//			AssinaAdobePDF assinador = new AssinaAdobePDF(arquivoInfo, email, identificadorAplicacao,
//					ambienteCertillion);
//			CertillionStatus status;
//			int contador = 0;
//			do {
//				contador++;
//				System.out.print(contador + " - ");
//				status = VerificaAssinatura.verificaAssinatura(assinador.getIdentificadorLote(), ambienteCertillion);
//				System.out.println(status);
//			} while (status == CertillionStatus.TRANSACTION_IN_PROGRESS);
//			List<ArquivoInfo> filesSigned = ValidaAssinatura.validaAssinatura(assinador.getIdentificadorLote(),
//					ambienteCertillion);
//			for (ArquivoInfo arquivoInfoAss : filesSigned) {
//				arquivoInfoAss.setStreamAssinaturaAttached(
//						new FileOutputStream(pathSaida + File.separator + arquivoInfoAss.getNomeArquivo()));
//				downloading(arquivoInfoAss);
//			}
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		} catch (ICPMException e) {
//			e.printStackTrace();
//		}
	}
	
}


