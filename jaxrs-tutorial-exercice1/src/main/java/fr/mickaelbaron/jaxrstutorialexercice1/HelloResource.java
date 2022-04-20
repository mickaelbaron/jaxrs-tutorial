package fr.mickaelbaron.jaxrstutorialexercice1;

import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
@Path("hello")
@Produces(MediaType.TEXT_PLAIN)
public class HelloResource {

	public HelloResource() {
	}

	@GET
	public String getHello() {
		return "Bonjour ENSMA";
	}

	@GET
	@Path("{id}")
	public String getHello(@PathParam("id") String id,
			@DefaultValue("votre serviteur") @HeaderParam("name") String name) {
		return "Bonjour " + id + " de la part de " + name;
	}

	@GET
	@Path("withheaders/{id}")
	public Response getHelloWithHeaders(@PathParam("id") String id,
			@DefaultValue("votre serviteur") @HeaderParam("name") String name) {
		return Response.ok().header("name", name).entity("Bonjour " + id + " de la part de (voir l'en-tête).").build();
	}
}
