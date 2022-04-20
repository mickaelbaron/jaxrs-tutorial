package fr.mickaelbaron.jaxrstutorialexercice2;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
public class TrainBooking {

	private String id;
	
	private String trainId;

	private int numberPlaces;

	public String getId() {
		return id;
	}

	public void setId(String pId) {
		this.id = pId;
	}

	public int getNumberPlaces() {
		return numberPlaces;
	}

	public void setNumberPlaces(int numberPlaces) {
		this.numberPlaces = numberPlaces;
	}

	public String getTrainId() {
		return trainId;
	}

	public void setTrainId(String trainId) {
		this.trainId = trainId;
	}
}
