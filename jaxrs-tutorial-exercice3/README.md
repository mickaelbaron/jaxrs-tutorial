# Exercice 3 (JAX-RS) : tests d'intégration de service web REST « Interrogation et réservation de billets de train »

Ce troisième exercice s'intéresse aux tests d'intégration de service web REST développés avec JAX-RS et Jersey. Le code du service web REST est déjà fourni et correspond à la solution complète de l'exercice 2 sur l'interrogation et la réservation de billets de train. Nous insistons dans cet exercice sur les bonnes pratiques pour développer des tests des services web REST en s'appuyant sur le style *Given-When-Then*.

Dans la suite de cet exercice le format de représentation des objets sera du JSON. Pour chaque méthode de test implémentée, vous exécuterez le test unitaire associé.

## But

* Utiliser le framework de test **Test-Framework** de Jersey.
* Utiliser un style de développement de tests basé sur *Given-When-Then*.
* Savoir écrire des tests d'intégration.

## Étapes à suivre

* Démarrer l'éditeur [VSCode](https://code.visualstudio.com/ "Visual Studio Code").

* Ouvrir le dossier du projet Maven **jaxrs-tutorial-exercice3**.

> Le projet importé contient déjà une implémentation complète du service web REST dédié à l'interrogation et la réservation de billets de train.

* Éditer le fichier de description Maven _pom.xml_ et ajouter les dépendances suivantes afin d'utiliser le framework de test *Test-Framework* de Jersey qui s'appuie sur la version 5 de [JUnit](https://junit.org/junit5/).

```xml
<dependency>
    <groupId>org.glassfish.jersey.test-framework</groupId>
    <artifactId>jersey-test-framework-core</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.glassfish.jersey.test-framework.providers</groupId>
    <artifactId>jersey-test-framework-provider-grizzly2</artifactId>
    <scope>test</scope>
</dependency>
```

* Ouvrir la classe `TrainResourceIntegrationTest` et compléter le code afin que le framework de test **Test-Framework** soit supporté.

```java
public class TrainResourceIntegrationTest extends JerseyTest {

    @Override
    protected Application configure() {
        ResourceConfig resourceConfig = new ResourceConfig(TrainResource.class);
        resourceConfig.property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_SERVER, Level.WARNING.getName());
        return resourceConfig;
    }
```

* Ajouter la méthode `getTrainsTest` qui permet de tester le comportement du service dédié à la récupération de tous les trains `/trains`.

```java
    @Test
    public void getTrainsTest() {
        // Given

        // When
        Response response = target("/trains").request(MediaType.APPLICATION_JSON_TYPE).get();

        // Then
        Assertions.assertEquals(Status.OK.getStatusCode(), response.getStatus(), "Http Response should be 200: ");
        List<Train> readEntities = response.readEntity(new GenericType<List<Train>>() {});
        Assertions.assertNotNull(readEntities);
        Assertions.assertEquals(3, readEntities.size());
        Assertions.assertTrue(readEntities.stream().anyMatch(current -> "TR123".equals(current.getId())));
    }
```

* Exécuter le test de la méthode `getTrainsTest` en vous assurant que le test passe.

> Dans la suite de cet exercice, compléter les sections `TODO` de chaque méthode de test. Une fois cela fait, exécuter le test correspondant pour vérifier son bon fonctionnement.

* Compléter la méthode `getTrainTest` au niveau de la partie *When* afin de satisfaire les différentes assertions.

```java
    @Test
    public void getTrainTest() {
        // Given
        String trainId = "TR123";

        // When
        // TODO: invoquer le service dédié à la récupération d'un train
        // par son identifiant fonctionnel (trainid = "TR123").

        // Then
        Assertions.assertEquals(Status.OK.getStatusCode(), response.getStatus(), "Http Response should be 200: ");
        Assertions.assertEquals(MediaType.APPLICATION_JSON_TYPE, response.getMediaType());
        Train readEntity = response.readEntity(Train.class);
        Assertions.assertNotNull(readEntity);
        Assertions.assertEquals("Poitiers", readEntity.getDeparture());
    }
```

* Compléter la méthode `searchTrainsByCriteriaTest` au niveau de la partie *Then* afin d'ajouter des assertions qui satisfont les contraintes suivantes :
  * le code de statut doit être `200` ;
  * la réponse doit contenir trois paramètres d'en-tête qui correspondent aux paramètres de la requête initiale (`departure`, `arrival` et `departure_time`) ;
  * le contenu doit être une liste de trains d'une taille de deux éléments.

```java
    @Test
    public void searchTrainsByCriteriaTest() {
        // Given
        String departure = "Poitiers";
        String arrival = "Paris";
        String departureTime = "1710";

        // When
        Response response = target("/trains").path("search").queryParam("departure", departure)
                .queryParam("arrival", arrival).queryParam("departure_time", departureTime)
                .request(MediaType.APPLICATION_JSON_TYPE).get();

        // Then
        // TODO: assertions à respecter ?
        //  * le code de statut doit être `200` ;
        //  * la réponse doit contenir trois paramètres d'en-tête qui correspondent
        //    aux paramètres de la requête initiale (`departure`, `arrival` et `departure_time`) ;
        //  * le contenu doit être une liste de trains d'une taille de deux éléments.
    }
```

Nous allons maintenant implémenter les tests d'intégration concernant la ressource de *réservation de billets de train*.

* Ouvrir la classe `TrainBookingResourceIntegrationTest` et compléter le code afin que le framework de test **Test-Framework** soit supporté.

```java
public class TrainResourceIntegrationTest extends ??? {

    @Override
    protected Application configure() {
        // TODO: prendre en compte `TrainResource` et `TrainBookingResource`.
    }
```

* Ajouter une méthode `createTrainBookingTest` qui permet de tester le comportement du service dédié à la création des réservations de billet de train.

```java
    @Test
    public void createTrainBookingTest() {
        // Given
        TrainBooking trainBooking = new TrainBooking();
        trainBooking.setNumberPlaces(3);
        trainBooking.setTrainId("TR123");

        // When
        // TODO: invoquer le service web pour la création des réservations de billet de train.

        // Then
        Assert.assertEquals("Http Response should be 200: ", Status.OK.getStatusCode(), response.getStatus());
    }
```

* Ajouter une méthode `createTrainBookingWithBadTrainIdTest` qui permet de tester que si un identifiant de train n'existe pas, une erreur de type `404` est retournée (`NOT_FOUND`).

```java
    @Test
    public void createTrainBookingWithBadTrainIdTest() {
        // Given
        TrainBooking trainBooking = new TrainBooking();
        trainBooking.setNumberPlaces(3);
        trainBooking.setTrainId("BADTR123");

        // When
        // TODO: invoquer le service web pour la création des réservations de billet de train.

        // Then
        Assert.assertEquals("Http Response should be 404: ", Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }
```

* Ajouter une méthode `getTrainBookingsTest` qui permet de tester la récupération de toutes les réservation de train. Pour ce test, nous allons utiliser une fabrique de *réservation de billets de train* dans la partie `Given`.

```java
    @Test
    public void getTrainBookingsTest() {
        // Given
        TrainBooking currentTrainBooking = createTrainBooking("TR123", 3);

        // When
        // TODO: invoquer le service web pour la récupérer de toutes
        // les réservations de billet de train.

        // Then
        // TODO: assertions à respecter ?
        //  * le code statut doit être `200` ;
        //  * une seule réservation de billets de train ;
        //  * l'identifiant du train passé lors de la création
        //    est le même que celui transmis par la réponse.
    }
```

* Ajouter une méthode `getTrainBookingTest` qui permet de tester la récupération d'une réservation de billets de train à partir d'un identifiant de réservation.

```java
    @Test
    public void getTrainBookingTest() {
        // Given
        TrainBooking currentTrainBooking = createTrainBooking("TR123", 3);

        // When
        // TODO: invoquer le service web pour la récupération
        // d'une réservation de billets de train à partir de currentTrainBooking.getId().

        // Then
        Assertions.assertEquals(Status.OK.getStatusCode(), response.getStatus(), "Http Response should be 200: ");
    }
```

* Ajouter une méthode `getTrainBookingWithBadTrainBookingIdTest` qui permet de tester que si un identifiant de réservation de billets de train n'existe pas, une erreur de type `404` est retournée (`NOT_FOUND`).

```java
    @Test
    public void getTrainBookingWithBadTrainBookingIdTest() {
        // Given
        String trainBookingId = "FAKETRAINBOOKINGID";

        // When
        // TODO: invoquer le service web pour la création des réservations de billet de train.

        // Then
        // TODO: assertion doit vérifier que le code statut est `404`.
    }
```

* Ajouter une méthode `removeTrainBookingTest` qui permet de tester la suppression d'une réservation d'un billet de train. Le code statut de la réponse doit être `204`.

```java
    @Test
    public void removeTrainBookingTest() {
        // Given
        TrainBooking currentTrainBooking = createTrainBooking("TR123", 3);

        // When
        // TODO: invoquer le service web pour la suppression d'une réservation de billets de train à partir de currentTrainBooking.getId()

        // Then
        // TODO: assertion doit vérifier que le code statut est `204`.
    }
```

* Ajouter une méthode `removeTrainBookingWithBadTrainBookingIdTest` qui permet de tester que si un identifiant de réservation de billets de train n'existe pas, le code statut de la réponse doit être `204` puisque le méthode `DELETE` est idempotente.

```java
    @Test
    public void removeTrainBookingWithBadTrainBookingIdTest() {
        // Given
        String trainBookingId = "FAKETRAINBOOKINGID";

        // When
        // TODO: invoquer le service web pour la suppression
        // d'une réservation de billets de train avec un mauvais identifiant.

        // Then
        // TODO: assertion doit vérifier que le code statut est `204`.
    }
```
