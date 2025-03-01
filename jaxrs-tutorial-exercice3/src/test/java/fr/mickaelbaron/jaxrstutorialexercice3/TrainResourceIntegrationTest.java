package fr.mickaelbaron.jaxrstutorialexercice3;

import java.util.List;
import java.util.logging.Level;

import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
public class TrainResourceIntegrationTest extends JerseyTest {

	@Override
	protected Application configure() {
		ResourceConfig resourceConfig = new ResourceConfig(TrainResource.class);
		resourceConfig.property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_SERVER, Level.WARNING.getName());
		return resourceConfig;
	}

	@Test
	public void getTrainsTest() {
		// Given

		// When
		Response response = target("/trains").request(MediaType.APPLICATION_JSON_TYPE).get();

		// Then
		Assertions.assertEquals(Status.OK.getStatusCode(), response.getStatus(), "Http Response should be 200: ");
		List<Train> readEntities = response.readEntity(new GenericType<List<Train>>() {
		});
		Assertions.assertNotNull(readEntities);
		Assertions.assertEquals(3, readEntities.size());
		Assertions.assertTrue(readEntities.stream().anyMatch(current -> "TR123".equals(current.getId())));
	}

	@Test
	public void getTrainTest() {
		// Given
		String trainId = "TR123";

		// When
		Response response = target("/trains").path("trainid-" + trainId).request(MediaType.APPLICATION_JSON_TYPE).get();

		// Then
		Assertions.assertEquals(Status.OK.getStatusCode(), response.getStatus(), "Http Response should be 200: ");
		Assertions.assertEquals(MediaType.APPLICATION_JSON_TYPE, response.getMediaType());
		Train readEntity = response.readEntity(Train.class);
		Assertions.assertNotNull(readEntity);
		Assertions.assertEquals("Poitiers", readEntity.getDeparture());
	}

	@Test
	public void searchTrainsByCriteriaTest() {
		// Given
		String departure = "Poitiers";
		String arrival = "Paris";
		String departureTime = "1710";

		// When
		Response response = target("/trains").path("search").queryParam("departure", departure)
				.queryParam("arrival", arrival).queryParam("departure_time", departureTime)
				.request(MediaType.APPLICATION_JSON_TYPE).get();

		// Then
		Assertions.assertEquals(Status.OK.getStatusCode(), response.getStatus(), "Http Response should be 200: ");
		Assertions.assertEquals(departure, response.getHeaderString("departure"));
		Assertions.assertEquals(arrival, response.getHeaderString("arrival"));
		Assertions.assertEquals(departureTime, response.getHeaderString("departure_time"));
		List<Train> readEntities = response.readEntity(new GenericType<List<Train>>() {
		});
		Assertions.assertNotNull(readEntities);
		Assertions.assertEquals(2, readEntities.size());
	}
}
