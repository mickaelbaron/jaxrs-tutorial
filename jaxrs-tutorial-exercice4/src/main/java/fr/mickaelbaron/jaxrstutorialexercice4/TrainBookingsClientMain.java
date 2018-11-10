package fr.mickaelbaron.jaxrstutorialexercice4;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
public class TrainBookingsClientMain extends JFrame {

	private static final long serialVersionUID = -4847763241088473465L;

	private WebTarget target;

	private JTextArea trainsConsole;

	private JTextArea trainBookingsConsole;

	public TrainBookingsClientMain() {
		super("TrainBookings Application");

		this.initializeService();
		this.initializeUI();
	}

	private void initializeService() {
		Client client = ClientBuilder.newClient();
		target = client.target("http://localhost:9993/api/trains");
	}

	private void callGetTrains() {
		List<Train> result = target.request(MediaType.APPLICATION_JSON_TYPE).get(new GenericType<List<Train>>() {
		});

		trainsConsole.setText("");
		for (Train current : result) {
			trainsConsole.append(current.getId() + " - " + current.getDeparture() + " - " + current.getArrival() + " - "
					+ current.getDepartureTime() + "\n");
		}
	}

	private void createTrainBooking(String numTrain, int numberPlaces) {
		TrainBooking trainBooking = new TrainBooking();
		trainBooking.setTrainId(numTrain);
		trainBooking.setNumberPlaces(numberPlaces);

		Response post = target.path("bookings").request(MediaType.APPLICATION_JSON_TYPE)
				.post(Entity.entity(trainBooking, MediaType.APPLICATION_JSON));
		if (Status.OK.getStatusCode() == post.getStatus()) {
			System.out.println(post.readEntity(TrainBooking.class).getId());
		}
	}

	private void callGetTrainBookings() {
		List<TrainBooking> result = target.path("bookings").request(MediaType.APPLICATION_JSON_TYPE)
				.get(new GenericType<List<TrainBooking>>() {
				});

		for (TrainBooking current : result) {
			trainBookingsConsole.append(current.getId() + " - " + current.getTrainId() + " - " + current.getNumberPlaces());
		}
	}

	private void initializeUI() {
		this.setLayout(new BorderLayout());

		JPanel panelCenter = new JPanel();
		panelCenter.setLayout(new GridLayout(2, 1));

		// Trains.
		JPanel panelTrains = new JPanel();
		panelTrains.setLayout(new BorderLayout());

		JButton getTrainsButton = new JButton("Trains ?");
		panelTrains.add(BorderLayout.NORTH, getTrainsButton);
		getTrainsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				callGetTrains();
			}
		});
		trainsConsole = new JTextArea();
		panelTrains.add(BorderLayout.CENTER, trainsConsole);
		panelCenter.add(panelTrains);

		JPanel panelTrainBookings = new JPanel();
		panelTrainBookings.setLayout(new BorderLayout());

		JButton getTrainBookingsButton = new JButton("TrainBookings ?");
		panelTrainBookings.add(BorderLayout.NORTH, getTrainBookingsButton);
		getTrainBookingsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				callGetTrainBookings();
			}
		});
		trainBookingsConsole = new JTextArea();
		panelTrainBookings.add(BorderLayout.CENTER, trainBookingsConsole);
		panelCenter.add(panelTrainBookings);

		JPanel createTrainBookingsPanel = new JPanel();
		createTrainBookingsPanel.setLayout(new GridLayout(1, 3));
		final JTextField numTrain = new JTextField();
		final JTextField numberPlaces = new JTextField();
		JButton createTrainBookingsButton = new JButton("CreateTrainBooking");
		createTrainBookingsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createTrainBooking(numTrain.getText(), Integer.parseInt(numberPlaces.getText()));
			}
		});
		createTrainBookingsPanel.add(createTrainBookingsButton);
		createTrainBookingsPanel.add(numTrain);
		createTrainBookingsPanel.add(numberPlaces);

		this.add(BorderLayout.CENTER, panelCenter);
		this.add(BorderLayout.SOUTH, createTrainBookingsPanel);

		this.pack();
		this.setVisible(true);
	}

	public static void main(String[] args) {
		new TrainBookingsClientMain();
	}
}