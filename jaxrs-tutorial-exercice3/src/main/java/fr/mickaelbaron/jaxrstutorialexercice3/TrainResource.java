package fr.mickaelbaron.jaxrstutorialexercice3;

import java.util.List;
import java.util.Optional;

import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
@Path("/trains")
@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
public class TrainResource {

	public TrainResource() {
	}

	@GET
	public List<Train> getTrains() {
		System.out.println("getTrains");

		return TrainBookingDB.getTrains();
	}

	@GET
	@Path("trainid-{id}")
	public Train getTrain(@PathParam("id") String trainId) {
		System.out.println("getTrain");

		Optional<Train> trainById = TrainBookingDB.getTrainById(trainId);

		if (trainById.isPresent()) {
			return trainById.get();
		} else {
			throw new NotFoundException();
		}
	}

	@GET
	@Path("/search")
	public Response searchTrainsByCriteria(@QueryParam("departure") String departure,
			@QueryParam("arrival") String arrival, @QueryParam("departure_time") String departureTime) {
		System.out.println("TrainResource.searchTrainsByCriteria()");

		return Response.ok().header("departure", departure).header("arrival", arrival).header("departure_time", departureTime)
				.entity(TrainBookingDB.getTrains().subList(0, 2)).build();
	}

	@Path("/bookings")
	public TrainBookingResource getTrainBookingResource() {
		System.out.println("TrainResource.getTrainBookingResource()");
		
		return new TrainBookingResource();
	}
}
