package br.gov.prodabel.portal.service;


import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import Model.Conteudo;

@Path("/assinatura")
public class AssinaturaService {

	@POST
	@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	@Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	public Response assinatura(@QueryParam("conteudo") String conteudo) {

		
		Gson gson = new GsonBuilder().create();
        Conteudo c = gson.fromJson(conteudo, Conteudo.class);
		
		if (conteudo != null) {
			return Response.status(200).entity(new Gson().toJson("true")).build();
		} else {
			return Response.status(200).entity(new Gson().toJson("false")).build();
		}

	}
}


