package br.gov.prodabel.portal.service;




import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.servlet.ServletContext;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
 
@Path("/download")
@Produces("application/pdf")
public class DownloadService {
    
	@Context
	public ServletContext context;
	
	private ResponseBuilder responseBuilder;
	private FileInputStream file;
	
	@POST
    public Response downloadPdfFile()
    {
		String DOWNLOAD_PATH = context.getRealPath("/files/");
		
		try {
			file = new FileInputStream(DOWNLOAD_PATH + "/cemig_out.pdf");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		responseBuilder = Response.ok(file);
		responseBuilder.header("Content-Disposition", "attachment;filename=cemig_out.pdf");
		return responseBuilder.build();
       
    }
}