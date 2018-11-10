package fr.mickaelbaron.jaxrstutorialexercice4;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
@XmlRootElement(name = "train")
public class Train {

	private String id;

	private String departure;

	private String arrival;

	@JsonProperty("departure_time")
	private int departureTime; // Format : 1230 = 12h30

	public Train() {
	}

	public Train(String pId, String departure, String arrival, int departureTime) {
		this.id = pId;
		this.departure = departure;
		this.arrival = arrival;
		this.departureTime = departureTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String pId) {
		this.id = pId;
	}

	public String getDeparture() {
		return departure;
	}

	public void setDeparture(String departure) {
		this.departure = departure;
	}

	public String getArrival() {
		return arrival;
	}

	public void setArrival(String arrival) {
		this.arrival = arrival;
	}

	public int getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(int departureTime) {
		this.departureTime = departureTime;
	}
}
