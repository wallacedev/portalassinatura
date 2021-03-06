package br.gov.prodabel.portal.service;




import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
    public Response downloadPdfFile(@QueryParam("filename") String fileName)
    {
		String DOWNLOAD_PATH = context.getRealPath("/files/");
		System.out.println("Download path: "+DOWNLOAD_PATH);
		try {
			file = new FileInputStream(DOWNLOAD_PATH + "/" + fileName);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		responseBuilder = Response.ok(file);
		responseBuilder.header("Content-Disposition", "attachment;filename=cemig_out.pdf");
		return responseBuilder.build();
       
    }
}