# Exercice 5 (JAX-RS) : déployer le service web REST « Interrogation et réservation de billets de train »

Ce cinquième exercice s'intéresse au déploiement du service web REST développé dans les précédents exercices. Nous montrerons le déploiement comme une application Java classique (exécuter depuis un jar) et un déploiement sur le serveur d'application Tomcat (déployer un fichier war). Nous ferons abstraction de l'éditeur [VSCode](https://code.visualstudio.com/ "Visual Studio Code") afin d'être le plus proche de l'environnement de production.

Le projet contient tout le code du service web REST.

## But

* Déployer comme une application Java classique (exécuter depuis un fichier jar).
* Packager un service web REST dans une archive war.
* Déployer un service web REST sur le serveur d'applications Java Tomcat.
* Gérer les problèmes de dépendances.

## Étapes à suivre pour effectuer un déploiement comme une application Java classique

* Saisir la ligne de commande suivante depuis la racine du projet pour compiler et construire le fichier jar du projet.

```bash
mvn clean package
```

La sortie console attendue :

```bash
[INFO] Scanning for projects...
[INFO]
[INFO] --------------< fr.mickaelbaron:jaxrs-tutorial-exercice5 >--------------
[INFO] Building TrainBooking Client 0.1-SNAPSHOT
[INFO]   from pom.xml
[INFO] --------------------------------[ jar ]---------------------------------
[INFO]
[INFO] --- clean:3.2.0:clean (default-clean) @ jaxrs-tutorial-exercice5 ---
[INFO] Deleting /Users/baronm/workspacepersowebserviceslabs/jaxrs-tutorial-solution/jaxrs-tutorial-exercice5/target
[INFO]
[INFO] --- resources:3.3.1:resources (default-resources) @ jaxrs-tutorial-exercice5 ---
[WARNING] Using platform encoding (UTF-8 actually) to copy filtered resources, i.e. build is platform dependent!
[INFO] skip non existing resourceDirectory /Users/baronm/workspacepersowebserviceslabs/jaxrs-tutorial-solution/jaxrs-tutorial-exercice5/src/main/resources
[INFO]
[INFO] --- compiler:3.14.0:compile (default-compile) @ jaxrs-tutorial-exercice5 ---
[INFO] Recompiling the module because of changed source code.
[WARNING] File encoding has not been set, using platform encoding UTF-8, i.e. build is platform dependent!
[INFO] Compiling 6 source files with javac [debug target 11] to target/classes
[INFO]
[INFO] --- resources:3.3.1:testResources (default-testResources) @ jaxrs-tutorial-exercice5 ---
[WARNING] Using platform encoding (UTF-8 actually) to copy filtered resources, i.e. build is platform dependent!
[INFO] skip non existing resourceDirectory /Users/baronm/workspacepersowebserviceslabs/jaxrs-tutorial-solution/jaxrs-tutorial-exercice5/src/test/resources
[INFO]
[INFO] --- compiler:3.14.0:testCompile (default-testCompile) @ jaxrs-tutorial-exercice5 ---
[INFO] No sources to compile
[INFO]
[INFO] --- surefire:3.2.5:test (default-test) @ jaxrs-tutorial-exercice5 ---
[INFO] No tests to run.
[INFO]
[INFO] --- jar:3.4.1:jar (default-jar) @ jaxrs-tutorial-exercice5 ---
[INFO] Building jar: /Users/baronm/workspacepersowebserviceslabs/jaxrs-tutorial-solution/jaxrs-tutorial-exercice5/target/jaxrstutorialexercice5.jar
[INFO]
[INFO] --- dependency:3.8.1:copy-dependencies (copy-dependencies) @ jaxrs-tutorial-exercice5 ---
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  2.632 s
[INFO] Finished at: 2025-03-01T07:35:58+01:00
[INFO] ------------------------------------------------------------------------
```

* Saisir la ligne de commande suivante pour démarrer le projet.

```bash
java -cp "target/jaxrstutorialexercice5.jar" fr.mickaelbaron.jaxrstutorialexercice5.TrainBookingLauncher
```

La sortie console attendue :

```bash
Exception in thread "main" java.lang.NoClassDefFoundError: jakarta/ws/rs/core/UriBuilder
	at fr.mickaelbaron.jaxrstutorialexercice5.TrainBookingLauncher.getBaseURI(TrainBookingLauncher.java:21)
	at fr.mickaelbaron.jaxrstutorialexercice5.TrainBookingLauncher.<clinit>(TrainBookingLauncher.java:18)
Caused by: java.lang.ClassNotFoundException: jakarta.ws.rs.core.UriBuilder
	at java.base/jdk.internal.loader.BuiltinClassLoader.loadClass(BuiltinClassLoader.java:583)
	at java.base/jdk.internal.loader.ClassLoaders$AppClassLoader.loadClass(ClassLoaders.java:178)
	at java.base/java.lang.ClassLoader.loadClass(ClassLoader.java:521)
	... 2 more
```

Vous remarquerez que le projet ne démarre pas du fait de l'absence de certaines dépendances. Il est donc obligatoire de fournir les dépendances nécessaires lors de l'exécution (dans le classpath).

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

```bash
mvn clean package
java -cp "target/jaxrstutorialexercice5.jar:target/dependency/*" fr.mickaelbaron.jaxrstutorialexercice5.TrainBookingLauncher
```

La sortie console attendue :

```
...
mars 01, 2025 7:42:07 AM org.glassfish.grizzly.http.server.NetworkListener start
INFO: Started listener bound to [localhost:9993]
mars 01, 2025 7:42:07 AM org.glassfish.grizzly.http.server.HttpServer start
INFO: [HttpServer] Started.
Jersey app started with WADL available at http://localhost:9993/api/
Hit enter to stop it...
```

## Étapes à suivre pour effectuer un déploiement sur le serveur d'applications Tomcat

Pour un déploiement de service web REST avec Jersey vers un serveur d'applications il est nécessaire de 1) fournir un fichier _web.xml_ où il est précisé le package des ressouces ; 2) construire le fichier war ; 3) fournir au serveur d'application le fichier war.

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
            <param-name>jersey.config.server.provider.packages</param-name>
            <param-value>fr.mickaelbaron.jaxrstutorialexercice5</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>
    <servlet-mapping>
    <servlet-name>jersey-servlet</servlet-name>
        <url-pattern>/api/*</url-pattern>
    </servlet-mapping>
</web-app>
```

* Éditer le fichier _pom.xml_ et à la ligne 122, préciser le chemin et le nom du fichier _web.xml_ qui sera utilisé comme descripteur pour l'application web Java.

```xml
<configuration>
    <webXml>src\main\configuration\web.xml</webXml>
</configuration>
```

* Saisir la ligne de commande suivante pour compiler et construire le projet vers un fichier war.

```bash
mvn clean package -P war
```

> L'option -P war permet d'utiliser le profil Maven appelé _war_. Depuis le fichier _pom.xml_ examiner la balise `<profiles>`. Cette astuce permet de générer un fichier jar ou un fichier war depuis un même fichier _pom.xml_.

Pour exécuter le serveur d'application [Tomcat](https://tomcat.apache.org/) qui exposera l'application que nous avons développée, nous utiliserons [Docker](https://www.docker.com/).

* Saisir la ligne de commande suivante pour télécharger une image Docker de Tomcat.

```bash
docker pull tomcat:jre11-openjdk-slim
```

La sortie console attendue :

```bash
jre11-openjdk-slim: Pulling from library/tomcat
1efc276f4ff9: Already exists
a2f2f93da482: Already exists
12cca292b13c: Already exists
d73cf48caaac: Already exists
74783450c6bd: Pull complete
86f0e1e2719f: Pull complete
8d05c7b6be0d: Pull complete
33e2c58906c0: Pull complete
Digest: sha256:f11b36ab02cb97e007e8c08530d5caa69390057803058c12bd4a332be9d53daf
Status: Downloaded newer image for tomcat:jre11-openjdk-slim
docker.io/library/tomcat:jre11-openjdk-slim

What's next:
    View a summary of image vulnerabilities and recommendations → docker scout quickview tomcat:jre11-openjdk-slim
```

* Saisir la ligne de commande suivante pour créer un conteneur Docker qui permettra de démarrer une instance de Tomcat. Le fichier _jaxrstutorialexercice5.war_ contient tous les codes et dépendances de ce projet.

```bash
docker run --rm --name helloworldrestservice-tomcat -v $(pwd)/target/jaxrstutorialexercice5.war:/usr/local/tomcat/webapps/jaxrstutorialexercice5.war -it -p 8080:8080 tomcat:jre11-openjdk-slim
```

La sortie console attendue :

```bash
Using CATALINA_BASE:   /usr/local/tomcat
Using CATALINA_HOME:   /usr/local/tomcat
Using CATALINA_TMPDIR: /usr/local/tomcat/temp
Using JRE_HOME:        /usr/local/openjdk-11
Using CLASSPATH:       /usr/local/tomcat/bin/bootstrap.jar:/usr/local/tomcat/bin/tomcat-juli.jar
Using CATALINA_OPTS:
NOTE: Picked up JDK_JAVA_OPTIONS:  --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.io=ALL-UNNAMED --add-opens=java.base/java.util=ALL-UNNAMED --add-opens=java.base/java.util.concurrent=ALL-UNNAMED --add-opens=java.rmi/sun.rmi.transport=ALL-UNNAMED
01-Mar-2025 07:00:42.368 INFO [main] org.apache.catalina.startup.VersionLoggerListener.log Server version name:   Apache Tomcat/10.0.23
...
```

* Tester le service web REST déployé avec **cURL**.

```bash
# Récupérer l'ensemble des trains au format XML
curl --header "Accept: application/xml" http://localhost:8080/jaxrstutorialexercice5/api/trains
```

La sortie console attendue :

```bash
<?xml version="1.0" encoding="UTF-8" standalone="yes"?><trains><train><arrival>Paris</arrival><departure>Poitiers</departure><departureTime>1250</departureTime><id>TR123</id></train><train><arrival>Paris</arrival><departure>Poitiers</departure><departureTime>1420</departureTime><id>AX127</id></train><train><arrival>Paris</arrival><departure>Poitiers</departure><departureTime>1710</departureTime><id>PT911</id></train></trains>
```

```bash
# Récupérer l'ensemble des trains au format JSON
curl --header "Accept: application/json" http://localhost:8080/jaxrstutorialexercice5/api/trains
```

La sortie console attendue :

```bash
[{"id":"TR123","departure":"Poitiers","arrival":"Paris","departure_time":1250},{"id":"AX127","departure":"Poitiers","arrival":"Paris","departure_time":1420},{"id":"PT911","departure":"Poitiers","arrival":"Paris","departure_time":1710}]
```