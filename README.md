# Tutoriel JAX-RS

L'objectif de cette leçon est d'apprendre à manipuler l'API JAX-RS pour le développement de services web REST à partir de la plateforme de développement Java.

Chaque exercice est fourni dans un dossier avec à l'intérieur un projet Java Maven contenant des classes et des fichiers de configuration qu'il faudra compléter au fur et à mesure des questions.

**Buts pédagogiques** : transformation d'une classe Java en service web REST, manipulation des annotations JAX-RS, tests d'intégration, client Java, implémentation Jersey, invocation des services REST via **cURL**, compilation avec Maven, déploiement du service web REST avec un serveur d'application lui-même déployé dans un conteneur Docker, déploiement du service web REST depuis une classe principale Java.

> Ce dépôt est utilisé dans le cadre d'un cours sur les architectures orientées services que je dispense à l'[ISAE-ENSMA](https://www.ensma.fr) et à l'[Université de Poitiers](http://www.univ-poitiers.fr/) en français. Tous les supports de cours et tutoriaux sont disponibles sur mon blog personnel : [https://mickael-baron.fr/soa/](https://mickael-baron.fr/soa/).

## Prérequis logiciels

Avant de démarrer cette série d'exercices sur l'utilisation de l'API JAX-RS, veuillez préparer votre environnement de développement en installant les outils suivants :

* [Java via OpenJDK](https://jdk.java.net/ "Java") ;
* [Maven](https://maven.apache.org/ "Maven") ;
* [VSCode](https://code.visualstudio.com/ "Visual Studio Code") et le [pack d'extension pour Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack "Extension Pack for Java") ;
* [cURL](https://curl.haxx.se "cURL") ;
* [Docker (exercice 5)](https://www.docker.com/ "Docker").

## Ressources et remerciements

Retrouver les autres tutoriels :

* [Tutoriel sur SOAP-UI pour inspecter et invoquer un service web étendus/SOAP via l’outil SOAP-UI](https://github.com/mickaelbaron/soapui-tutorial) ;
* [Tutoriel sur JAX-WS pour implémenter des services web étendus/SOAP](https://github.com/mickaelbaron/jaxws-tutorial) ;
* [Tutoriel sur le développement de WebSocket et de Server-Sent Event avec le langage Java et les bibliothèques JAX-RS et Tyrus](https://github.com/mickaelbaron/streaminghttp-tutorial).
* [Tutoriel sur le développement d'une application basée sur une architecture microservice avec Docker](https://github.com/mickaelbaron/javamicroservices-tutorial).

Pour aller plus loin, vous pouvez consulter les ressources suivantes :

* [Support de cours SOA](https://mickael-baron.fr/soa/introduction-soa "Support de cours SOA") ;
* [Support de cours WSDL](https://mickael-baron.fr/soa/decrire-configurer-wsdl "Support de cours WSDL") ;
* [Support de cours SOAP](https://mickael-baron.fr/soa/communiquer-soap "Support de cours SOAP") ;
* [Support de cours JAX-WS](https://mickael-baron.fr/soa/developper-serviceweb-jaxws "Support de cours JAX-WS").
* [Support de cours REST](https://mickael-baron.fr/soa/comprendre-style-architecture-rest "Support de cours REST") ;
* [Support de cours JAX-RS](https://mickael-baron.fr/soa/developper-serviceweb-rest-jaxrs "Support de cours JAX-RS") ;
* [Support de cours sur le Streaming HTTP](https://mickael-baron.fr/soa/introduction-streaminghttp) ;
* [Support de cours sur la mise en œuvre de technologies de Streaming HTTP avec le langage Java](https://mickael-baron.fr/soa/streaminghttp-mise-en-oeuvre) ;
* [Support de cours sur une introduction aux architectures microservices](https://mickael-baron.fr/soa/introduction-microservices "Support de cours sur une introduction aux architectures microservices") ;
* [Support de cours sur les outils et bibliothèques pour la mise en œuvre d'architectures microservices](https://mickael-baron.fr/soa/microservices-mise-en-oeuvre "Support de cours sur les outils et bibliothèques pour la mise en œuvre d'architectures microservic").
