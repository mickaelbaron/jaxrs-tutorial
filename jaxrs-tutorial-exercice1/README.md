# Exercice 1 (JAX-RS) : développer un service web REST « Bonjour ENSMA »

Le service web REST de ce premier exercice fournit un accès à la ressource « Hello » qui permet de retourner des messages de bienvenue. Le service proposé n'autorise que la lecture à la ressource (GET) et des paramètres peuvent être autorisés. Le format supporté par la ressource « Hello » sera du texte (`text/plain`).

## But

* Développer un service web REST à partir d'une classe Java.
* Déployer le service web REST comme une application Java classique.
* Afficher le contrat de description WADL.
* Tester le service web REST avec **cURL**.

## Étapes à suivre

* Démarrer l'environnement de développement Eclipse.

* Importer le projet Maven **jaxrs-tutorial-exercice1** (**File -> Import -> Maven -> Existing Maven Projects**), choisir le répertoire du projet, puis faire **Finish**.

* Créer une classe qui représentera la ressource « Hello » (**File -> New** puis choisir **Class**). Appeler la classe `HelloResource` et la définir dans le package `fr.mickaelbaron.jaxrstutorialexercice1`.

* Dans la nouvelle classe créée, ajouter l'annotation `@Path("Hello")` pour préciser que la ressource sera accessible via le chemin */hello* et l'annotation `@Produces(MediaType.TEXT_PLAIN)` pour indiquer que le contenu retourné au client sera de type texte (`text/plain`).

```java
@Path("hello")
@Produces(MediaType.TEXT_PLAIN)
public class HelloResource {
    public HelloResource() { }
}
```

* Ajouter une première méthode `String getHello()` permettant de retourner une constante de type chaîne de caractères `Bonjour ENSMA` (ou autre texte de votre création).

```java
    @GET
    public String getHello() {
        return "Bonjour ENSMA";
    }
```

* Afin de résoudre les problèmes de dépendances vers la bibliothèque JAX-RS, compléter le fichier de description Maven *pom.xml* en ajoutant la dépendance suivante (balise `<dependencies>`).

```xml
<dependency>
    <groupId>javax.ws.rs</groupId>
    <artifactId>javax.ws.rs-api</artifactId>
</dependency>
```

Cette dépendance (API de JAX-RS) ne sert qu'à résoudre les problèmes de visibilité des annotations. Nous aurons besoin de dépendances supplémentaires (implémentation de JAX-RS) quand nous souhaiterons déployer notre service web REST.

* Compléter la classe `HelloLauncher` dans le package `fr.mickaelbaron.jaxrstutorialexercice1`. Cette classe sera utilisée pour publier localement notre service web REST.

```java
public class HelloLauncher {

    public static final URI BASE_URI = getBaseURI();

    private static URI getBaseURI() {
        return UriBuilder.fromUri("http://localhost/api/").port(9991).build();
    }

    public static void main(String[] args) {
        ResourceConfig rc = new ResourceConfig();
        rc.registerClasses(HelloResource.class);
        rc.property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_SERVER, Level.WARNING.getName());

        try {
            HttpServer server = GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
            server.start();

            System.out.println(String.format(
                    "Jersey app started with WADL available at " + "%sapplication.wadl\nHit enter to stop it...",
                    BASE_URI, BASE_URI));

            System.in.read();
            server.shutdownNow();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

* Afin de résoudre les problèmes de dépendances vers le serveur Grizzly, compléter le fichier de description Maven *pom.xml*.

```xml
<dependency>
    <groupId>org.glassfish.jersey.containers</groupId>
    <artifactId>jersey-container-grizzly2-http</artifactId>
</dependency>
```

* Exécuter la classe `HelloLauncher`.

* Ouvrir une fenêtre d'un navigateur web et tester la récupération de la ressource « Hello » (requête GET via l'URL <http://localhost:9991/api/hello>).

Ce service web REST n'est pas complet, puisqu'il n'est pas possible de paramétrer le message de bienvenue (utilisation de *template parameter*) ni de connaître l'auteur du message de bienvenue (utilisation d'un paramètre d'en-tête).

* Ajouter une nouvelle méthode Java `getHello` dans la classe `HelloResource` qui prend en paramètre une chaîne de caractères initialisée par un _template parameter_ (annotations `@Path` et `@PathParam`) et une autre chaîne de caractères initialisée par un paramètre d'en-tête (annotation `@HeaderParam`) dont la clé sera `name`. La valeur par défaut de l'en-tête sera fixée `votre serviteur` (annotation `@DefaultValue`).

```java
    @GET
    @Path("{id}")
    public String getHello(@PathParam("id") String id,
                           @DefaultValue("votre serviteur") @HeaderParam("name") String name) {
        return "Bonjour " + id + " de la part de " + name;
    }
```

* Exécuter de nouveau la classe `HelloLauncher` et depuis votre navigateur web saisir l'URL permettant d'invoquer ce nouveau service web REST (requête GET via l'URL <http://localhost:9991/api/hello/ENSMA>). Malheureusement, le navigateur web ne permet pas de préciser la valeur du paramètre d'en-tête `name`. Nous utiliserons donc l'outil en ligne de commande **cURL** pour construire des requêtes HTTP complexes.

* Depuis une invite de commande saisir la commande suivante :

```bash
$ curl --header "name:Mickael BARON" http://localhost:9991/api/hello/ENSMA
Bonjour ENSMA de la part de Mickael BARON
```

Ce service web REST n'est toujours pas complet puisque nous aimerions retourner dans l'en-tête de la réponse l'auteur du message de bienvenue. Comment pourrions-nous retourner à la fois un contenu dans la réponse et une information dans l'en-tête de la réponse ? Pour cela, nous allons utiliser un objet `Response` pour le retour de méthode.

* Ajouter une nouvelle méthode Java `getHelloWithHeaders` dans la classe `HelloResource` qui possède les mêmes paramètres que la précédente méthode. Le chemin pour invoquer cette méthode sera `withheaders/{id}` où `id` est le paramètre du message de bienvenue. Dans le corps de la méthode `getHelloWithHeaders`, compléter le code ci-dessous afin de transmettre le nom de l'auteur dans l'en-tête de la réponse.

```java
    @GET
    @Path("withheaders/{id}")
    public Response getHelloWithHeaders(@PathParam("id") String id,
                                        @DefaultValue("votre serviteur") @HeaderParam("name") String name) {
        return Response.ok().header("name", name).entity("Bonjour " + id + " de la part de (voir l'en-tête).").build();
    }
```

* Exécuter de nouveau la classe `HelloLauncher` puis saisir la ligne de commande **cURL** suivante pour envoyer une requête avec les bons paramètres et détailler le retour de la réponse.

```bash
$ curl --header "name:Mickael BARON" http://localhost:9991/api/hello/withheaders/ENSMA -v
*   Trying ::1...
* TCP_NODELAY set
* Connection failed
* connect to ::1 port 9991 failed: Connection refused
*   Trying 127.0.0.1...
* TCP_NODELAY set
* Connected to localhost (127.0.0.1) port 9991 (#0)
> GET /api/hello/withheaders/ENSMA HTTP/1.1
> Host: localhost:9991
> User-Agent: curl/7.54.0
> Accept: */*
> name:Mickael BARON
>
< HTTP/1.1 200 OK
< name: Mickael BARON
< Content-Type: text/plain
< Content-Length: 46
<
* Connection #0 to host localhost left intact
Bonjour ENSMA de la part de (voir l'en-tête).
```

* Nous allons afficher le contrat de description de ce service web REST au format WADL. Saisir depuis un navigateur l'URL suivante : <http://localhost:9991/api/application.wadl>.

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<application xmlns="http://wadl.dev.java.net/2009/02">
    <doc xmlns:jersey="http://jersey.java.net/" jersey:generatedBy="Jersey: 2.27 2018-04-10 07:34:57"/>
    <doc xmlns:jersey="http://jersey.java.net/" jersey:hint="This is simplified WADL with user and core resources only. To get full WADL with extended resources use the query parameter detail. Link: http://localhost:9991/api/application.wadl?detail=true"/>
    <grammars/>
    <resources base="http://localhost:9991/api/">
        <resource path="hello">
            <method id="getHello" name="GET">
                <response>
                    <representation mediaType="text/plain"/>
                </response>
            </method>
            <resource path="{id}">
                <param xmlns:xs="http://www.w3.org/2001/XMLSchema" name="id" style="template" type="xs:string"/>
                <method id="getHello" name="GET">
                    <request>
                        <param xmlns:xs="http://www.w3.org/2001/XMLSchema" name="name" style="header" type="xs:string" default="votre serviteur"/>
                    </request>
                    <response>
                        <representation mediaType="text/plain"/>
                    </response>
                </method>
            </resource>
            <resource path="withheaders/{id}">
                <param xmlns:xs="http://www.w3.org/2001/XMLSchema" name="id" style="template" type="xs:string"/>
                <method id="getHelloWithHeaders" name="GET">
                    <request>
                        <param xmlns:xs="http://www.w3.org/2001/XMLSchema" name="name" style="header" type="xs:string" default="votre serviteur"/>
                    </request>
                    <response>
                        <representation mediaType="text/plain"/>
                    </response>
                </method>
            </resource>
        </resource>
    </resources>
</application>
```