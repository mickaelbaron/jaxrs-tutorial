package fr.mickaelbaron.jaxrstutorialexercice5;

import java.util.List;
import java.util.Optional;

import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
public class TrainBookingResource {

	@POST
	public TrainBooking createTrainBooking(TrainBooking trainBooking) {
		System.out.println("TrainBookingResource.createTrainBooking()");

		Optional<Train> findFirst = TrainBookingDB.getTrainById(trainBooking.getTrainId());

		if (!findFirst.isPresent()) {
			throw new NotFoundException("No matches found.");
		}

		TrainBooking newBookTrain = new TrainBooking();
		newBookTrain.setNumberPlaces(trainBooking.getNumberPlaces());
		newBookTrain.setTrainId(findFirst.get().getId());
		newBookTrain.setId(Long.toString(System.currentTimeMillis()));

		TrainBookingDB.getTrainBookings().add(newBookTrain);

		return newBookTrain;
	}

	@GET
	public List<TrainBooking> getTrainBookings() {
		System.out.println("TrainBookingResource.getTrainBookings()");

		return TrainBookingDB.getTrainBookings();
	}

	@GET
	@Path("{id}")
	public TrainBooking getTrainBooking(@PathParam("id") String trainBookingId) {
		System.out.println("TrainBookingResource.getTrainBooking()");

		Optional<TrainBooking> findFirst = TrainBookingDB.getTrainBookingById(trainBookingId);

		if (findFirst.isPresent()) {
			return findFirst.get();
		} else {
			throw new NotFoundException("No matches found.");
		}
	}

	@DELETE
	@Path("{id}")
	public void removeTrainBooking(@PathParam("id") String trainBookingId) {
		System.out.println("TrainBookingResource.removeTrainBooking()");

		Optional<TrainBooking> findFirst = TrainBookingDB.getTrainBookingById(trainBookingId);

		if (findFirst.isPresent()) {
			TrainBookingDB.getTrainBookings().remove(findFirst.get());
		}
	}
}
