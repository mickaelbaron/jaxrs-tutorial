# Exercice 4 (JAX-RS) : client de service web REST « Interrogation et réservation de billets de train »

Ce quatrième exercice s'intéresse à la mise en place d'un client pour l'accès au service web REST développé dans les exercices 2 et 3. Une interface utilisateur en ligne de commande développée avec [Picocli](https://picocli.info/) permet d'invoquer les services web pour les ressources de *train* et de *réservation de billets de train*.

Le projet de l'exercice 3 fournira l'implémentation du service web REST que le client que nous allons développer va invoquer. Exécuter la classe `TrainBookingLauncher` de l'exercice 3 pour rendre disponible le service web REST.

## But

* Utiliser l'API cliente de JAX-RS et son implémentation via Jersey.
* Savoir manipuler le type de contenu.

## Étapes à suivre

* Démarrer l'éditeur [VSCode](https://code.visualstudio.com/ "Visual Studio Code").

* Ouvrir le dossier du projet Maven **jaxrs-tutorial-exercice4**.

> Le projet importé contient déjà une implémentation complète de l'interface utilisateur avec [Picocli](https://picocli.info/). Aucune compétence dans l'utilisation de [Picocli](https://picocli.info/) n'est demandée puisque l'objectif de cet exercice est de manipuler exclusivement l'API cliente JAX-RS. Si vous souhaitez proposer des améliorations à l'interface utilisateur, les *pull requests* sont les bienvenues.

* Éditer le fichier de description Maven _pom.xml_ et ajouter la dépendance suivante afin d'utiliser l'API cliente JAX-RS et son implémentation Jersey.

```xml
<dependency>
    <groupId>org.glassfish.jersey.core</groupId>
    <artifactId>jersey-client</artifactId>
</dependency>
```

* Ouvrir la classe `TrainBookingsCLIMain` et compléter le code de la méthode `initializeService` afin d'initialiser l'attribut `WebTarget target`. Pour rappel, l'URL d'accès au service web REST est <http://localhost:9993/api/trains>.

```java
    private void initializeService() {
        Client client = ClientBuilder.newClient();
        target = client.target("http://localhost:9993/api/trains");
    }
```

Pour récupérer l'ensemble des trains, le concept de Train doit être modélisé, mais la classe `Train` n'existe pas dans le projet. Comme nous n'utilisons pas une démarche Top/Down, toutes les classes utilisées pour transmettre des données via les requêtes et les réponses ne sont pas générées automatiquement. La solution la plus rapide est de « copier/coller » la classe `Train` de l'exercice 3.

* Copier depuis l'exercice 3, la classe `Train` en vous assurant de modifier le nom du package par `fr.mickaelbaron.jaxrstutorialexercice4`.

```java
package fr.mickaelbaron.jaxrstutorialexercice5;

@XmlRootElement(name = "train")
public class Train {

    private String id;

    private String departure;

    private String arrival;

    @JsonProperty("departure_time")
    private int departureTime; // Format : 1230 = 12h30

    ...
```

* Compléter la méthode `callGetTrains` permettant de récupérer l'ensemble des trains et de les afficher sur la console.

```java
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
```

* Pour créer et récupérer des réservations de billets de train, une classe `TrainBooking` doit êtré créée car elle n'existe pas. Copier depuis l'exercice 3, la classe `TrainBooking` en vous assurant si possible de modifier le nom du package par `fr.mickaelbaron.jaxrstutorialexercice4`.

```java
package fr.mickaelbaron.jaxrstutorialexercice5;

@XmlRootElement(name = "trainbooking")
public class TrainBooking {

    private String id;

    @JsonProperty("current_train")
    private String trainId;

    @JsonProperty("number_places")
    private int numberPlaces;

    ...
```

* Compléter la méthode `createTrainBooking` permettant de créer des réservations de billet de train. Les informations pour la création comme l'identifiant du train et le nombre de places sont renseignées en paramètre de la méthode (valeurs extraites depuis les paramètres `-n` et `-q` de la ligne de commande).

```java
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
```

* Compléter la méthode `callGetTrainBookings` permettant de récupérer l'ensemble des réservations de billet de train et de les afficher sur la sorite console.

```java
    private void callGetTrainBookings() {
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
```

Tester votre programme client en invoquant les trois commandes `trains`, `trainbooking` et `trainbookings` développées précédemment.

* Depuis [VSCode](https://code.visualstudio.com/ "Visual Studio Code"), ouvrir les configurations d'exécution (**Run** -> **Open Configurations**) puis ajouter les trois configurations suivantes.

```json
{
    // Use IntelliSense to learn about possible attributes.
    // Hover to view descriptions of existing attributes.
    // For more information, visit: https://go.microsoft.com/fwlink/?linkid=830387
    "version": "0.2.0",
    "configurations": [
        {
            "type": "java",
            "name": "TrainBookingsCLIMain-Trains",
            "request": "launch",
            "mainClass": "fr.mickaelbaron.jaxrstutorialexercice4.TrainBookingsCLIMain",
            "projectName": "jaxrs-tutorial-exercice4",
            "args": "trains"
        },
        {
            "type": "java",
            "name": "TrainBookingsCLIMain-TrainBooking",
            "request": "launch",
            "mainClass": "fr.mickaelbaron.jaxrstutorialexercice4.TrainBookingsCLIMain",
            "projectName": "jaxrs-tutorial-exercice4",
            "args": "trainbooking -n TR123 -q 2"
        },
        {
            "type": "java",
            "name": "TrainBookingsCLIMain-TrainBookings",
            "request": "launch",
            "mainClass": "fr.mickaelbaron.jaxrstutorialexercice4.TrainBookingsCLIMain",
            "projectName": "jaxrs-tutorial-exercice4",
            "args": "trainbookings"
        }
    ]
}
```

* Exécuter les trois configuraations appelées `TrainBookingsCLIMain-Trains`, `TrainBookingsCLIMain-TrainBooking` et `TrainBookingsCLIMain-TrainBookings`. Le résultat attendu est le suivant.

bash
```
# Lister l'ensemble des trains disponibles
# java -cp '...' fr.mickaelbaron.jaxrstutorialexercice4.TrainBookingsCLIMain trains
TR123 - Poitiers - Paris - 1250
AX127 - Poitiers - Paris - 1420
PT911 - Poitiers - Paris - 1710

# Créer un réservation de billets de train
# java -cp '...' fr.mickaelbaron.jaxrstutorialexercice4.TrainBookingsCLIMain trainbooking -n TR123 -q 2
1740810277064

# Lister l'ensemble des réservations de billets de train
# java -cp '...' fr.mickaelbaron.jaxrstutorialexercice4.TrainBookingsCLIMain trainbookings
1740810277064 - TR123 - 2
```