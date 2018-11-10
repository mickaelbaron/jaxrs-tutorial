# Exercice 5 (JAX-RS) : déployer le service web REST « Interrogation et réservation de billet de train »

Ce cinquième exercice s'intéresse au déploiement du service web REST développé dans les précédents exercices. Nous montrerons le déploiement comme une application Java classique (exécuter depuis un jar) et un déploiement sur le serveur d'application Tomcat (déployer un fichier war). Nous ferons abstraction de l'environnement de développement Eclipse afin d'être le plus proche de l'environnement de production.

Le projet contient tout le code du service web REST

## But

* Déployer comme une application Java classique (exécuter depuis un fichier jar).
* Packager un service web REST dans une archive war.
* Déployer un service web REST sur le serveur d'applications Java Tomcat.
* Gérer les problèmes de dépendances.

## Étapes à suivre pour effectuer un déploiement comme une application Java classique

* Saisir la ligne de commande suivante depuis la racine du projet pour compiler et construire le fichier jar du projet.

```sh
mvn clean package
```

* Saisir la ligne de commande suivante pour démarrer le projet.

```sh
$ java -cp "target/jaxrstutorialexercice5.jar" fr.mickaelbaron.jaxrstutorialexercice5.TrainBookingLauncher
Exception in thread "main" java.lang.NoClassDefFoundError: javax/ws/rs/core/UriBuilder
    at fr.mickaelbaron.jaxrstutorialexercice5.TrainBookingLauncher.getBaseURI(TrainBookingLauncher.java:21)
    at fr.mickaelbaron.jaxrstutorialexercice5.TrainBookingLauncher.<clinit>(TrainBookingLauncher.java:18)
Caused by: java.lang.ClassNotFoundException: javax.ws.rs.core.UriBuilder
    at java.base/jdk.internal.loader.BuiltinClassLoader.loadClass(BuiltinClassLoader.java:582)
    at java.base/jdk.internal.loader.ClassLoaders$AppClassLoader.loadClass(ClassLoaders.java:178)
    at java.base/java.lang.ClassLoader.loadClass(ClassLoader.java:521)
    ... 2 more
```

Vous remarquerez que le projet ne démarre pas du fait de l'absence de certaines dépendances. Il est donc nécessaire de fournir les dépendances nécessaires lors de l'exécution (dans le classpath).

* Modifier le fichier _pom.xml_ afin d'ajouter le plugin **maven-dependency-plugin** qui permettra de lister toutes les bibliothèques nécessaires.

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-dependency-plugin</artifactId>
    <version>${maven.dependency.version}</version>
    <executions>
        <execution>
            <id>copy-dependencies</id>
            <phase>package</phase>
            <goals>
                <goal>copy-dependencies</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

* Saisir les lignes de commande suivantes pour compiler, construire et démarrer le projet.

```sh
$ mvn clean package
...
$ java -cp "target/jaxrstutorialexercice5.jar:target/dependency/*" fr.mickaelbaron.jaxrstutorialexercice5.TrainBookingLauncher
```

## Étapes à suivre pour effectuer un déploiement sur le serveur d'applications Tomcat

Pour un déploiement de service web REST avec Jersey vers un serveur d'applications il est nécessaire de 1/ fournir un fichier _web.xml_ où il est précisé un objet `ResourceConfig` à prendre en compte 2/ construire le fichier war et 3/ fournir au serveur d'application le fichier war.

* Créer une classe `TrainBookingApplication` de type `ResourceConfig`.

```java
package fr.mickaelbaron.jaxrstutorialexercice5;

public class TrainBookingApplication extends ResourceConfig {

    public TrainBookingApplication() {
        this.registerClasses(TrainBookingResource.class, TrainResource.class);
        this.property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_SERVER, Level.WARNING.getName());
    }
}
```

* Éditer le fichier _src/main/configuration/web.xml_ et ajouter la déclaration de la classe `TrainBookingApplication`.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee 
        http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
    version="3.1">
    <display-name>HelloWorldRestWebService</display-name>
    <servlet>
        <servlet-name>jersey-servlet</servlet-name>
        <servlet-class>org.glassfish.jersey.servlet.ServletContainer</servlet-class>
        <init-param>
            <param-name>javax.ws.rs.Application</param-name>
            <param-value>fr.mickaelbaron.jaxrstutorialexercice5.TrainBookingApplication</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>
    <servlet-mapping>
    <servlet-name>jersey-servlet</servlet-name>
        <url-pattern>/api/*</url-pattern>
    </servlet-mapping>
</web-app>
```

* Éditer le fichier _pom.xml_ et à la ligne 102, préciser le chemin et le nom du fichier _web.xml_ qui sera utilisé comme descripteur pour l'application web Java.

```xml
<properties>
    <project.packaging>war</project.packaging>
    <maven.war.webxml>TODO</maven.war.webxml>
</properties>
```

* Saisir la ligne de commande suivante pour compiler et construire le projet vers un fichier war.

```sh
mvn clean package -P war
```

> L'option -P war permet d'utiliser le profile Maven appelé war. Depuis le fichier _pom.xml_ examiner la balise `<profiles>`. Cette astuce permet de générer un fichier jar ou un fichier war depuis un même fichier _pom.xml_.

* Saisir la ligne de commande suivante pour télécharger une image Docker de Tomcat.

```sh
docker pull tomcat:9-jre11-slim
```

* Enfin, saisir la ligne de commande suivante pour créer un conteneur Docker qui permettra de démarrer une instance de Tomcat. Le fichier `jaxrstutorialexercice5.war` contient tous les codes et dépendances de ce projet.

```sh
docker run --rm --name helloworldrestservice-tomcat -v $(pwd)/target/jaxrstutorialexercice5.war:/usr/local/tomcat/webapps/jaxrstutorialexercice5.war -it -p 8080:8080 tomcat:9-jre11-slim
```

* Tester le service web REST déployé avec CURL.

```sh
$ curl --header "Accept: application/xml" http://localhost:8080/jaxrstutorialexercice5/api/trains
<?xml version="1.0" encoding="UTF-8" standalone="yes"?><trains><train><arrival>Paris</arrival><departure>Poitiers</departure><departureTime>1250</departureTime><id>TR123</id></train><train><arrival>Paris</arrival><departure>Poitiers</departure><departureTime>1420</departureTime><id>AX127</id></train><train><arrival>Paris</arrival><departure>Poitiers</departure><departureTime>1710</departureTime><id>PT911</id></train></trains>

$ curl --header "Accept: application/json" http://localhost:8080/jaxrstutorialexercice5/api/trains
[{"id":"TR123","departure":"Poitiers","arrival":"Paris","departure_time":1250},{"id":"AX127","departure":"Poitiers","arrival":"Paris","departure_time":1420},{"id":"PT911","departure":"Poitiers","arrival":"Paris","departure_time":1710}]
```