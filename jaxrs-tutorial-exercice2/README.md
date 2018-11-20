# Exercice 2 (JAX-RS) : développer un service web REST « Interrogation et réservation de billet de train »

Le service web REST de ce deuxième exercice consiste à créer un système CRUD pour l'interrogation et la réservation de billet de train. Les ressources manipulées par ce service web REST est donc un **train** et une **réservation de billet de train** pour un train donné. Le service web REST doit pouvoir lister l'ensemble des trains, lister les trains qui satisfont un critère de recherche (ville de départ, ville d'arrivée, jour de départ et un intervalle de temps) puis de créer, lister et supprimer une réservation de billet de train.

Nous insisterons sur la mise en place du service web REST et non sur le code métier (le code Java dans le corps des méthodes est « sans importance »). Les formats supportés par les deux ressources seront de l'XML et du JSON.

## But

* Développer un service web REST à partir de classes Java.
* Utiliser un *Sub-Resource Locator*.
* Manipuler des types personnalisés.
* Manipuler des formats de représentations (XML et JSON).

## Étapes à suivre

* Démarrer l'environnement de développement Eclipse.

* Importer le projet Maven **jaxrs-tutorial-exercice2** (**File -> Import -> Maven -> Existing Maven Projects**), choisir le répertoire du projet puis faire **Finish**.

> Le projet importé contient déjà des classes. À ce stade, de nombreuses erreurs de compilation sont présentes dûes à l'absence de certaines classes Java. Pas d'inquiétude, nous allons les ajoutons progressivement.

* Créer une classe `Train` (dans le package `fr.mickaelbaron.jaxrstutorialexercice2`) qui modélise le concept de **train** et qui contient un attribut `String id` (identifiant fonctionnel d'un train), un attribut `String departure` (la ville de départ du train), un attribut `String arrival` (la ville d'arrivée du train) et un attribut `int departureTime` (heure de départ). Ajouter des modificateurs et des accesseurs sur tous les attributs. Ci-dessous un résultat du code que vous devez obtenir.

```java
package fr.mickaelbaron.jaxrstutorialexercice2;

public class Train {

    private String id;

    private String departure;

    private String arrival;

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
```

* Examiner la classe `TrainBookingBD` qui joue le rôle de DAO et de base de données. En effet, toutes les instances créées se feront en mémoire.

* Créer la classe `TrainResource` (dans le package `fr.mickaelbaron.jaxrstutorialexercice2`) permettant de représenter la ressource **train**. La classe contient trois méthodes qui permettent respectivement 1) de retourner une liste de tous les trains 2) d'obtenir un train par son identifiant et 3) de rechercher un train par des critères passés en paramètres (ville de départ, ville d'arrivée et heure de départ). Noter que les formats de retour peuvent être de l'XML ou du JSON. Compléter la classe ci-dessous en remplaçant `TODO` par les bonnes instructions JAX-RS.

```java
package fr.mickaelbaron.jaxrstutorialexercice2;

// TODO: le chemin racine de la ressource doit débuter par `/trains`.
// TODO: les formats du contenu des réponses sont XML puis JSON.
public class TrainResource {

    public TrainResource() { }

    // TODO: préciser le verbe HTTP.
    public List<Train> getTrains() {
        System.out.println("getTrains");

        return TrainBookingDB.getTrains();
    }

    // TODO: préciser le verbe HTTP
    // TODO: le chemin doit commencer par `/numTrain-`  et se finir
    // par un template paramètre désignant l'identifiant du train.
    public Train getTrain(String trainId) {
        System.out.println("getTrain");

        Optional<Train> trainById = TrainBookingDB.getTrainById(trainId);

        if (trainById.isPresent()) {
            return trainById.get();
        } else {
            // TODO: déclencher une exception avec un statut NOT_FOUND.
        }
    }

    // TODO: préciser le verbe HTTP.
    // TODO: le chemin doit commencer par `/search`.
    // Les paramètres sont tous des paramètres de requête.
    public ??? searchTrainsByCriteria(String departure, String arrival, String arrivalHour) {
        System.out.println("TrainResource.searchTrainsByCriteria()");

        // TODO: retourner une réponse avec :
        //   1/ les trois paramètres de requête en en-tête
        //   2/ un sous ensemble de la liste des trains 
        //      (exemple : `TrainBookingDB.getTrains().subList(0, 2)`)
    }
}
```

* Créer la classe `TrainBookingLauncher` utilisée pour tester ce service web REST.

```java
package fr.mickaelbaron.jaxrstutorialexercice2;

public class TrainBookingLauncher {

    public static final URI BASE_URI = getBaseURI();

    private static URI getBaseURI() {
        return UriBuilder.fromUri("http://localhost/api/").port(9992).build();
    }

    public static void main(String[] args) {
        ResourceConfig rc = new ResourceConfig();
        rc.registerClasses(TrainResource.class);
        rc.property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_SERVER, Level.WARNING.getName());

        try {
            HttpServer server = GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
            server.start();

            System.out.println(String.format(
                "Jersey app started with WADL available at " + "%sapplication.wadl\nHit enter to stop it...", BASE_URI, BASE_URI));

            System.in.read();
            server.shutdownNow();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

* Exécuter la classe `TrainBookingLauncher` puis à partir de **cURL** invoquer le service web permettant de récupérer la liste de tous les trains.

```bash
$ curl http://localhost:9992/api/trains -v
* Connected to localhost (127.0.0.1) port 9992 (#0)
> GET /api/trains HTTP/1.1
> Host: localhost:9992
> User-Agent: curl/7.54.0
> Accept: */*
>
< HTTP/1.1 500 Internal Server Error
< Connection: close
< Content-Length: 0
<
* Closing connection 0
```

Comme observé sur le retour de la commande **cURL**, une erreur 500 (*Internal Server Error*) est retournée. Sur la console du serveur, le message suivant a du être généré.

```bash
nov. 06, 2018 6:29:44 PM org.glassfish.jersey.message.internal.WriterInterceptorExecutor$TerminalWriterInterceptor aroundWriteTo
SEVERE: MessageBodyWriter not found for media type=application/xml, type=class java.util.ArrayList, genericType=java.util.List<fr.mickaelbaron.jaxrstutorialexercice2.Train>.
```

Cette erreur indique que la classe `Train` ne contient pas les informations nécessaires pour transformer un objet en XML (XML est le format choisi dans l'ordre d'apparition de l'annotation `@Produces`). Pour une sérialisation Java <=> XML, chaque classe doit être au moins annotée à la racine pour activer le mapping entre l'XML Schema et les attributs de la classe.

* Éditer la classe `Train` et ajouter l'annotation suivante

```java
@XmlRootElement(name = "train")
public class Train {
    ...
}
```

* Exécuter de nouveau la classe `TrainBookingLauncher` puis à partir de **cURL** invoquer le service web permettant de récupérer la liste de tous les trains. Comme montré ci-dessous, la liste des trains est obtenue via un format XML.

```bash
$ curl http://localhost:9992/api/trains
<?xml version="1.0" encoding="UTF-8" standalone="yes"?><trains><train><arrival>Paris</arrival><departure>Poitiers</departure><departureTime>1250</departureTime><id>TR123</id></train><train><arrival>Paris</arrival><departure>Poitiers</departure><departureTime>1420</departureTime><id>AX127</id></train><train><arrival>Paris</arrival><departure>Poitiers</departure><departureTime>1710</departureTime><id>PT911</id></train></trains>
```

Nous souhaitons désormais obtenir un retour du contenu au format JSON. Cette information doit être précisée dans l'en-tête de la requête avec l'attribut `Accept`.

* Saisir la ligne de commande suivante.

```bash
$ curl --header "Accept:application/json" http://localhost:9992/api/trains -v
* Connected to localhost (127.0.0.1) port 9992 (#0)
> GET /api/trains HTTP/1.1
> Host: localhost:9992
> User-Agent: curl/7.54.0
> Accept: application/json
>
< HTTP/1.1 500 Internal Server Error
< Connection: close
< Content-Length: 0
<
* Closing connection 0
```

Comme observé sur le retour de la commande **cURL**, une erreur 500 est retournée. Sur la console du serveur, le message est identique à la précédente erreur pour le format XML. En d'autres termes, Jersey ne sait pas comment transformer un objet Java en JSON. Pour résoudre cette absence, il suffit d'ajouter la dépendance de la bibliothèque Jackson au fichier _pom.xml_.

```bash
nov. 06, 2018 7:30:14 PM org.glassfish.jersey.message.internal.WriterInterceptorExecutor$TerminalWriterInterceptor aroundWriteTo
SEVERE: MessageBodyWriter not found for media type=application/json, type=class java.util.ArrayList, genericType=java.util.List<fr.mickaelbaron.jaxrstutorialexercice2.Train>.
```

* Éditer le fichier _pom.xml_ et ajouter la dépendance suivante.

```xml
<dependency>
    <groupId>org.glassfish.jersey.media</groupId>
    <artifactId>jersey-media-json-jackson</artifactId>
</dependency>
```

* Exécuter de nouveau la classe `TrainBookingLauncher` puis relancer la commande **cURL** précédente.

```bash
$ curl --header "Accept: application/json" http://localhost:9992/api/trains
[{"id":"TR123","departure":"Poitiers","arrival":"Paris","departureTime":1250},{"id":"AX127","departure":"Poitiers","arrival":"Paris","departureTime":1420},{"id":"PT911","departure":"Poitiers","arrival":"Paris","departureTime":1710}]
```

Vous constatez sur le résultat JSON que le nom des clés correspond exactement au nom des attributs de la classe Java `Train`.

* Modifier la classe `Train` de telle sorte d'obtenir une clé `departure_time` (dans le format JSON) au lieu de `departureTime` tout en respectant les conventions de nommage Java.

```java
@XmlRootElement(name = "train")
public class Train {

    private String id;

    private String departure;

    private String arrival;

    @JsonProperty("departure_time")
    private int departureTime; // Format : 1230 = 12h30
    ...
}
```

* Exécuter la classe `TrainBookingLauncher` puis vérifier que le nom de la clé `departure_time` a été impacté.

```bash
$ curl --header "Accept: application/json" http://localhost:9992/api/trains
[{"id":"TR123","departure":"Poitiers","arrival":"Paris","departure_time":1250},{"id":"AX127","departure":"Poitiers","arrival":"Paris","departure_time":1420},{"id":"PT911","departure":"Poitiers","arrival":"Paris","departure_time":1710}]
```

* Continuer à tester le service web REST de façon à invoquer les méthodes Java `getTrain` et `searchTrainsByCriteria`. Pour cette dernière méthode, afficher la réponse complète pour s'assurer que les trois paramètres de requête sont transmis dans l'en-tête de la réponse.

```bash
$ curl --header "Accept: application/json" http://localhost:9992/api/trains/numTrain-TR123
{"id":"TR123","departure":"Poitiers","arrival":"Paris","departure_time":1250}

$ curl --header "Accept: application/json" http://localhost:9992/api/trains/search\?departure\=poitiers\&arrival\=paris\&arrivalhour\=1050 -v
* Connected to localhost (127.0.0.1) port 9992 (#0)
> GET /api/trains/search?departure=poitiers&arrival=paris&arrivalhour=1050 HTTP/1.1
> Host: localhost:9992
> User-Agent: curl/7.54.0
> Accept: application/json
>
< HTTP/1.1 200 OK
< departure: poitiers
< arrival: paris
< arrivalhour: 1050
< Content-Type: application/json
< Content-Length: 157
<
* Connection #0 to host localhost left intact
[{"id":"TR123","departure":"Poitiers","arrival":"Paris","departure_time":1250},{"id":"AX127","departure":"Poitiers","arrival":"Paris","departure_time":1420}]
```

Nous allons maintenant nous occuper à implémenter le service dédié à la **réservation de billet de train** pour un train donné. La classe `TrainBooking` utilisée pour modéliser une réservation de billet est déjà présente dans le projet. Elle contient un attribut `String id` (identifiant fonctionnel d'une réservation de billet), un attribut `String trainId` (la clé étrangère du train) et un attribut `int numberPlaces` pour le nombre de place à réserver. Toutefois, le paramétrage  La classe `BookTrainResource` est utilisée pour implémenter le service de réservation de billet.

* Modifier la classe `TrainBooking` de telle sorte d'obtenir une clé `current_train` (dans le format JSON) au lieu de `trainId` et une clé `number_places` (dans le format JSON) au lieu de `numberPlaces`.

```java
@XmlRootElement(name = "trainbooking")
public class TrainBooking {

    private String id;

    // TODO: le nom de la clé JSON doit être current_train.
    private String trainId;

    // TODO: le nom de la clé JSON doit être number_places.
    private int numberPlaces;
    ...
```

* Créer la classe `BookTrainResource` (dans le package `fr.mickaelbaron.jaxrstutorialexercice2`). Quatre méthodes sont à définir. La première `createTrainBooking` est invoquée pour la création d'une réservation de billet. La deuxième `getTrainBookings` est utilisée pour lister l'ensemble des réservations. La troisième `getTrainBooking` permet de retourner les informations d'une réservation à partir d'un numéro de réservation. Finalement `removeBookTrain` permet de supprimer une réservation.

```java
package fr.mickaelbaron.jaxrstutorialexercice2;

public class TrainBookingResource {

    // TODO: préciser le verbe HTTP.
    public TrainBooking createTrainBooking(TrainBooking trainBooking) {
        System.out.println("TrainBookingResource.createTrainBooking()");

        Optional<Train> findFirst = TrainBookingDB.getTrainById(trainBooking.getTrainId());

        if (findFirst.isEmpty()) {
             // TODO: déclencher une exception avec un statut NOT_FOUND.
        }

        TrainBooking newBookTrain = new TrainBooking();
        newBookTrain.setNumberPlaces(trainBooking.getNumberPlaces());
        newBookTrain.setTrainId(findFirst.get().getId());
        newBookTrain.setId(Long.toString(System.currentTimeMillis()));

        TrainBookingDB.getTrainBookings().add(newBookTrain);

        return newBookTrain;
    }

    // TODO: préciser le verbe HTTP.
    public List<TrainBooking> getTrainBookings() {
        System.out.println("TrainBookingResource.getTrainBookings()");

        return TrainBookingDB.getTrainBookings();
    }

    // TODO: préciser le verbe HTTP.
    // TODO: template paramètre désignant l'identifiant du train.
    public TrainBooking getTrainBooking(String trainBookingId) {
        System.out.println("TrainBookingResource.getTrainBooking()");

        Optional<TrainBooking> findFirst = TrainBookingDB.getTrainBookingById(trainBookingId);

        if (findFirst.isPresent()) {
            return findFirst.get();
        } else {
            // TODO: déclencher une exception avec un statut NOT_FOUND.
        }
    }

    // TODO: préciser le verbe HTTP.
    // TODO: template paramètre désignant l'identifiant du train.
    public void removeTrainBooking(@PathParam("id") String trainBookingId) {
        System.out.println("TrainBookingResource.removeTrainBooking()");

        Optional<TrainBooking> findFirst = TrainBookingDB.getTrainBookingById(trainBookingId);

        if (findFirst.isPresent()) {
            TrainBookingDB.getTrainBookings().remove(findFirst.get());
        } else {
            // TODO: déclencher une exception avec un statut NOT_FOUND.
        }
    }
}
```

Cette ressource définie par la classe `TrainBookingResource` est accessible via la mise en place d'un *sub-resource locator* depuis la ressource *train*. L'avantage est de pouvoir lier la ressource *train* avec la ressource de *réservation de billet*.

* Compléter la classe `Train` de façon à ajouter une méthode `getTrainBookingResource` qui servira de *sub-resource locator*. Assurer que les exigences suivantes soient respectées : annotée avec `@Path`, non annoté avec les annotations de méthodes HTTP (`@GET`, `@POST`, `@PUT` et `@DELETE`) et doit retourner un objet de type ressource.

```java
...
public class TrainResource {
    ...

    // TODO: la sous-ressource doit être accessible via l'URI `/bookings`
    public ??? getTrainBookingResource() {
        System.out.println("TrainResource.getTrainBookingResource()");

        // TODO: doit retourner un objet du type de la ressource souhaitée => TrainBookingResource
    }
}
```

* Exécuter la classe `BookTrainMain` et à partir de **cURL** invoquer chaque service lié à la réservation de billet de train qui ont été implémentés dans les quatre méthodes `createTrainBooking`, `getTrainBookings`, `getTrainBooking` et `removeTrainBooking`.

```bash
# Récupérer la liste des trains.
$ curl --header "Accept: application/json" http://localhost:9992/api/trains
[{"id":"TR123","departure":"Poitiers","arrival":"Paris","departure_time":1250},{"id":"AX127","departure":"Poitiers","arrival":"Paris","departure_time":1420},{"id":"PT911","departure":"Poitiers","arrival":"Paris","departure_time":1710}]

# Créer une réservation de billet de train.
$ curl --header "Accept: application/json" --header "Content-Type: application/json" --request POST --data '{"current_train":"TR123","number_places":2}' http://localhost:9992/api/trains/bookings
{"id":"1541683057395","current_train":"TR123","number_places":2}

# Récupérer la liste des réservation de billet de train.
$ curl --header "Accept: application/json" http://localhost:9992/api/trains/bookings
[{"id":"1541683057395","current_train":"TR123","number_places":2}]

# Récupérer une réservation de billet de train par un identifiant.
$ curl --header "Accept: application/json" http://localhost:9992/api/trains/bookings/1541683057395
[{"id":"1541683057395","current_train":"TR123","number_places":2}]

# Supprimer une réservation de billet de train par un identifiant (réussie).
$ curl --request DELETE http://localhost:9992/api/trains/bookings/1541683057395 -v
* Connected to localhost (127.0.0.1) port 9992 (#0)
> DELETE /api/trains/bookings/1541685562466 HTTP/1.1
> Host: localhost:9992
> User-Agent: curl/7.54.0
> Accept: */*
>
< HTTP/1.1 204 No Content
<
* Connection #0 to host localhost left intact

# Supprimer une réservation de billet de train par un identifiant (échouée).
$ curl --request DELETE http://localhost:9992/api/trains/bookings/1541683057395 -v
* Connected to localhost (127.0.0.1) port 9992 (#0)
> DELETE /api/trains/bookings/1541685562466 HTTP/1.1
> Host: localhost:9992
> User-Agent: curl/7.54.0
> Accept: */*
>
< HTTP/1.1 404 Not Found
< Content-Length: 0
<
* Connection #0 to host localhost left intact
```
