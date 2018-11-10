package fr.mickaelbaron.jaxrstutorialexercice5;

import java.util.logging.Level;

import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
public class TrainBookingApplication extends ResourceConfig {

	public TrainBookingApplication() {
		this.registerClasses(TrainBookingResource.class, TrainResource.class);
		this.property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_SERVER, Level.WARNING.getName());
	}
}
