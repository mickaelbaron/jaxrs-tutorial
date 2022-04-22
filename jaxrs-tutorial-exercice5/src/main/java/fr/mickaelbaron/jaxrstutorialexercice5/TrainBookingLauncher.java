package fr.mickaelbaron.jaxrstutorialexercice5;

import java.net.URI;
import java.util.logging.Level;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;

import jakarta.ws.rs.core.UriBuilder;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
public class TrainBookingLauncher {

	public static final URI BASE_URI = getBaseURI();

	private static URI getBaseURI() {
		return UriBuilder.fromUri("http://localhost/api/").port(9993).build();
	}

	public static void main(String[] args) {
		ResourceConfig rc = new ResourceConfig();
		rc.registerClasses(TrainResource.class);
		rc.property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_SERVER, Level.INFO.getName());
		rc.property("org.glassfish.jersey.logging.LoggingInterceptor", Level.WARNING.getName());
		
		try {
			HttpServer server = GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
			server.start();

			System.out.println(String.format(
					"Jersey app started with WADL available at " + "%s\nHit enter to stop it...",
					BASE_URI, BASE_URI));

			System.in.read();
			server.shutdownNow();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
