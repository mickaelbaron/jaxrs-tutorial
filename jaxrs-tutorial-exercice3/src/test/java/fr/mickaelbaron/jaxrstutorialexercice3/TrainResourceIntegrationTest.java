package fr.mickaelbaron.jaxrstutorialexercice3;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

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
		// par son identifiant fonctionnel (trainid = "TR123").

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
				.queryParam("arrival", arrival).queryParam("departure_time", departureTime)
				.request(MediaType.APPLICATION_JSON_TYPE).get();

        // Then
        // TODO: assertions à respecter ?
        //  * le code de statut doit être `200` ;
        //  * la réponse doit contenir trois paramètres d'en-tête qui correspondent 
        //    aux paramètres de la requête initiale (`departure`, `arrival` et `departure_time`) ;
        //  * le contenu doit être une liste de trains d'une taille de deux éléments.
	}
}
