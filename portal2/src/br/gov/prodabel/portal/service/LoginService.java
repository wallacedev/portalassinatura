package br.gov.prodabel.portal.service;


import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

@Path("/login")
public class LoginService {

	@POST
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	public Response getLogin(@QueryParam("user") String user,
			@QueryParam("password") String password) {
		
		if (user != null && password != null) {
			if(user.equals("admin") && password.equals("senha")) {
				return Response.status(200).entity(new Gson().toJson("true")).build();
			}
			else {
				return Response.status(200).entity(new Gson().toJson("false")).build();
			}
		} else {
			return Response.status(200).entity(new Gson().toJson("false")).build();
		}
	}
}

