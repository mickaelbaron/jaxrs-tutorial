package fr.mickaelbaron.jaxrstutorialexercice4;

import java.util.List;
import java.util.concurrent.Callable;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "trainbooking-cli", header = "%nTrainBooking-CLI: Réserve ton train en ligne de commande.%n", mixinStandardHelpOptions = true, version = "1.0", footer = "Développé avec ♥ by Mickael BARON (Follow me on Github @mickaelbaron).", subcommands = {
        getTrainsFunction.class, getTrainBookingsFunction.class, createTrainBookingFunction.class })
public class TrainBookingsCLIMain {
    public static void main(String[] args) {
        int exitCode = new CommandLine(new TrainBookingsCLIMain()).execute(args);
        System.exit(exitCode);
    }
}

@Command(name = "trains", description = "Lister l'ensemble des trains.")
class getTrainsFunction implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        return new ApiUtils().callGetTrains();
    }
}

@Command(name = "trainbookings", description = "Lister l'ensemble des réservations.")
class getTrainBookingsFunction implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        return new ApiUtils().callGetTrainBookings();
    }
}

@Command(name = "trainbooking", description = "Réserver un train à partir du numéro de train et le nombre de place souhaité.")
class createTrainBookingFunction implements Callable<Integer> {
    @Option(names = { "-n", "--numtrain" }, required = true, description = "Numéro du train.")
    private String numTrain;

    @Option(names = { "-q", "--quantity" }, required = true, description = "Nombre de places.")
    private int quantity;

    @Override
    public Integer call() throws Exception {
        return new ApiUtils().createTrainBooking(numTrain, quantity);
    }
}

class ApiUtils {

    private WebTarget target;

    public ApiUtils() {
        this.initializeService();
    }

    private void initializeService() {
        Client client = ClientBuilder.newClient();
        target = client.target("http://localhost:9993/api/trains");
    }

    public Integer callGetTrains() {
        Response response = target.request(MediaType.APPLICATION_JSON_TYPE).get();

        if (response.getStatus() == 200) {
            List<Train> trains = response.readEntity(new GenericType<List<Train>>() {
            });

            for (Train current : trains) {
                System.out
                        .println(current.getId() + " - " + current.getDeparture() + " - " + current.getArrival() + " - "
                                + current.getDepartureTime());
            }

            return 0;
        } else {
            return 1;
        }
    }

    public Integer createTrainBooking(String numTrain, int numberPlaces) {
        TrainBooking trainBooking = new TrainBooking();
        trainBooking.setTrainId(numTrain);
        trainBooking.setNumberPlaces(numberPlaces);

        Response post = target.path("bookings").request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(trainBooking, MediaType.APPLICATION_JSON));
        if (Status.OK.getStatusCode() == post.getStatus()) {
            String id = post.readEntity(TrainBooking.class).getId();
            System.out.println(id);
            return 0;
        } else {
            return 1;
        }
    }

    public Integer callGetTrainBookings() {
        Response response = target.path("bookings").request(MediaType.APPLICATION_JSON_TYPE).get();

        if (response.getStatus() == 200) {
            List<TrainBooking> trains = response.readEntity(new GenericType<List<TrainBooking>>() {
            });
            for (TrainBooking current : trains) {
                System.out.println(current.getId() + " - " + current.getTrainId() + " - " + current.getNumberPlaces());
            }
            return 0;
        } else {

            return 1;
        }
    }
}
