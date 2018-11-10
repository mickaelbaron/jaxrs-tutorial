package fr.mickaelbaron.jaxrstutorialexercice3;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
public class TrainBookingDB {

	private static List<Train> trains = new ArrayList<>();

	private static List<TrainBooking> trainBookings = new ArrayList<>();

	static {
		trains.add(new Train("TR123", "Poitiers", "Paris", 1250));
		trains.add(new Train("AX127", "Poitiers", "Paris", 1420));
		trains.add(new Train("PT911", "Poitiers", "Paris", 1710));
	}

	public static List<Train> getTrains() {
		return trains;
	}

	public static Optional<Train> getTrainById(String trainId) {
		return TrainBookingDB.getTrains().stream().filter(current -> current.getId().equals(trainId)).findFirst();
	}

	public static List<TrainBooking> getTrainBookings() {
		return trainBookings;
	}

	public static Optional<TrainBooking> getTrainBookingById(String trainBookingId) {
		return TrainBookingDB.getTrainBookings().stream().filter(current -> current.getId().equals(trainBookingId))
				.findFirst();
	}
}
