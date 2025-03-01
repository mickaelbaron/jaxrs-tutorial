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
        // TODO: invoquer le service web REST pour récupérer l'ensemble des trains
        // disponibles.
        
        if (response.getStatus() == 200) {
            // TODO: si la réponse a un code de statut de 200, alors récupérer
            // un objet de type List<Train>

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
        // TODO: créer un objet de type TrainBooking à partir des paramètres d'entrées.

        // TODO: invoquer le service web REST pour créer une réservation de billet de train
        // à partir de l'objet TrainBooking précédent.

        if (Status.OK.getStatusCode() == post.getStatus()) {
            // TODO: à partir de la réponse obtenue, récupérer l'identifiant de réservation.

            System.out.println(id);
            return 0;
        } else {
            return 1;
        }
    }

    public Integer callGetTrainBookings() {
        // TODO: invoquer le service web REST pour récupérer l'ensemble des réservations
        // de billet de train.

        if (response.getStatus() == 200) {
            // TODO: si la réponse a un code de statut de 200, alors récupérer
            // un objet de type List<TrainBooking>

            for (TrainBooking current : trains) {
                System.out.println(current.getId() + " - " + current.getTrainId() + " - " + current.getNumberPlaces());
            }
            return 0;
        } else {

            return 1;
        }
    }
}
