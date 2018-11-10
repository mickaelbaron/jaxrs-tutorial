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
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
public class TrainResourceIntegrationTest {

	@Test
	public void getTrainTest() {
		// Given
		String trainId = "TR123";

		// When
		// TODO: invoquer le service dédié à la récupération d'un train
		// par son identifiant fonctionnel (trainId = "TR123").

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
        // TODO: assertions à respecter ?
        //  * le code de statut doit être `200` ;
        //  * la réponse doit contenir trois paramètres d'en-tête qui correspondent 
        //    aux paramètres de la requête initiale (`departure`, `arrival` et `departureTime`) ;
        //  * le contenu doit être une liste de trains d'une taille de deux éléments.
	}
}
