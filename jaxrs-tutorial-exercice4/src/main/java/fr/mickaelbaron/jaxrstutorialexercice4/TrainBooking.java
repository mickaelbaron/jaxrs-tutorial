package fr.mickaelbaron.jaxrstutorialexercice4;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
@XmlRootElement(name = "trainbooking")
public class TrainBooking {

	private String id;

	@JsonProperty("current_train")
	private String trainId;

	@JsonProperty("number_places")
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
