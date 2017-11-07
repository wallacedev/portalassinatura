package br.gov.prodabel.portal.service;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.ProtocolException;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import br.gov.pbh.certillion.UploadArquivo;
import br.gov.pbh.certillion.utils.AmbienteCertillion;
import br.gov.pbh.certillion.utils.vo.ArquivoInfo;



@Path("/fileupload")
public class FileUploadService {

	private final AmbienteCertillion AMBIENTE = AmbienteCertillion.CERTILLION_PRODUCAO;
	
	@Context
	public ServletContext context;
	
	@POST
	@Consumes({MediaType.MULTIPART_FORM_DATA})
	public Response fileUpload(@FormDataParam("file") InputStream fileInputStream,
            @FormDataParam("file") FormDataContentDisposition fileMetaData) throws Exception {

		String retorno = "";
		retorno = getFileHash(fileInputStream, fileMetaData);
		
//		testeCertillionUpload(fileInputStream, fileMetaData);
		
//		String UPLOAD_PATH = context.getRealPath("/files/");
//	    try
//	    {
//	        int read = 0;
//	        byte[] bytes = new byte[1024];
//	 
//	        OutputStream out = new FileOutputStream(new File(UPLOAD_PATH + "/"+ fileMetaData.getFileName()));
//	        while ((read = fileInputStream.read(bytes)) != -1) 
//	        {
//	            out.write(bytes, 0, read);
//	        }
//	        out.flush();
//	        out.close();
//	    } catch (IOException e) 
//	    {
//	        throw new WebApplicationException("Error while uploading file. Please try again !!");
//	    }
//		retorno = "Data uploaded successfully !!";
	    return Response.ok(retorno).build();

	}
	
	private String getFileHash(InputStream fileInputStream, FormDataContentDisposition fileMetaData) {
		String UPLOAD_PATH = context.getRealPath("/files/");
		ArquivoInfo arquivo = new ArquivoInfo();
		arquivo.setNomeArquivo(fileMetaData.getFileName());
		arquivo.setStreamArquivo(fileInputStream);
		//arquivo.setIdentificador(1);
		UploadArquivo uploadArquivo = null;
		try {
			//arquivo.setStreamAssinaturaAttached(new FileOutputStream(new File("temp.temp")));
			uploadArquivo = new UploadArquivo();
			uploadArquivo.uploadArquivo(arquivo, AMBIENTE);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "0";
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "0";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "0";
		}
		if(uploadArquivo != null) { 
			return arquivo.getHashArquivo();
		}
		else return "0";
		
	}

	private void testeCertillionUpload(InputStream fileInputStream, FormDataContentDisposition fileMetaData) {
		
//		AssinaArquivo assinaArquivo = null;
//		String userIdentifier = "wallace.teixeira@pbh.gov.br";
//		String identificadorAplicacao = "Portal da assinatura";
//		ArquivoInfo arquivo = new ArquivoInfo();
//		arquivo.setNomeArquivo(fileMetaData.getFileName());
//		arquivo.setStreamArquivo(fileInputStream);
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
//		
		
		
		
		
	}
}


