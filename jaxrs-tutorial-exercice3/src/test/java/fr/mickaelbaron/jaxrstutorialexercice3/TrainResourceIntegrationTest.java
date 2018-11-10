package fr.mickaelbaron.jaxrstutorialexercice3;

import java.util.List;
import java.util.logging.Level;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Test;

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
		Assert.assertEquals("Http Response should be 200: ", Status.OK.getStatusCode(), response.getStatus());
		List<Train> readEntities = response.readEntity(new GenericType<List<Train>>() {
		});
		Assert.assertNotNull(readEntities);
		Assert.assertEquals(3, readEntities.size());
		Assert.assertTrue(readEntities.stream().anyMatch(current -> "TR123".equals(current.getId())));
	}

	@Test
	public void getTrainTest() {
		// Given
		String trainId = "TR123";

		// When
		Response response = target("/trains").path("numTrain-" + trainId).request(MediaType.APPLICATION_JSON_TYPE)
				.get();

		// Then
		Assert.assertEquals("Http Response should be 200: ", Status.OK.getStatusCode(), response.getStatus());
		Assert.assertEquals(MediaType.APPLICATION_JSON_TYPE, response.getMediaType());
		Train readEntity = response.readEntity(Train.class);
		Assert.assertNotNull(readEntity);
		Assert.assertEquals("Poitiers", readEntity.getDeparture());
	}

	@Test
	public void searchTrainsByCriteriaTest() {
		// Given
		String departure = "Poitiers";
		String arrival = "Paris";
		String departureTime = "1710";

		// When
		Response response = target("/trains").path("search").queryParam("departure", departure)
				.queryParam("arrival", arrival).queryParam("departureTime", departureTime)
				.request(MediaType.APPLICATION_JSON_TYPE).get();

		// Then
		Assert.assertEquals("Http Response should be 200: ", Status.OK.getStatusCode(), response.getStatus());
		Assert.assertEquals(departure, response.getHeaderString("departure"));
		Assert.assertEquals(arrival, response.getHeaderString("arrival"));
		Assert.assertEquals(departureTime, response.getHeaderString("departureTime"));
		List<Train> readEntities = response.readEntity(new GenericType<List<Train>>() {});
		Assert.assertNotNull(readEntities);
		Assert.assertEquals(2, readEntities.size());
	}
}
