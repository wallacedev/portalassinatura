package br.gov.prodabel.portal;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/message")
public class TesteRest
{
    @GET
    public String getMsg()
    {
         return "Hello World !! - Jersey 2";
    }
}