# JAX-RS Tutoriel

L'objectif de cette leçon est d'apprendre à manipuler l'API JAX-RS pour le développement de services web REST à partir de la plateforme de développement Java.

Chaque exercice est fourni dans un dossier avec à l'intérieur un projet Java Maven contenant des classes et des fichiers de configuration qu'il faudra compléter au fur et à mesure des questions.

**Buts pédagogiques** : transformation d'une classe Java en service web REST, manipulation des annotations JAX-RS, tests d'intégration, client Java, implémentation Jersey, invocation des services REST via **cURL**, compilation avec Maven, déploiement du service web REST avec un serveur d'application lui-même déployé dans un conteneur Docker, déploiement du service web REST depuis une classe principale Java.

> Ce dépôt est utilisé dans le cadre d'un cours sur les architectures orientées services que je dispense à l'[ISAE-ENSMA](https://www.ensma.fr) et à l'[Université de Poitiers](http://www.univ-poitiers.fr/) en français. Tous les supports de cours et tutoriaux sont disponibles sur ma page Developpez.com : [https://mbaron.developpez.com](https://mbaron.developpez.com/#page_soa).

## Prérequis logiciels

Avant de démarrer cette série d'exercices sur l'utilisation de l'API JAX-RS, veuillez préparer votre environnement de développement en installant les outils suivants :

* [Java via OpenJDK](http://jdk.java.net/ "Java 8 à 11") ;
* [Maven](https://maven.apache.org/ "Maven") ;
* [Eclipse](https://www.eclipse.org/ "Eclipse") ;
* [cURL](https://curl.haxx.se "cURL") ;
* [Docker (exercice 5)](https://www.docker.com/ "Docker").

## Ressources et remerciements

Retrouver les précédentes leçons :

* [Tutoriel sur SOAP-UI pour inspecter et invoquer un service web étendus/SOAP via l’outil SOAP-UI](https://github.com/mickaelbaron/soapui-tutorial) ;
* [Tutoriel sur JAX-WS pour implémenter des services web étendus/SOAP](https://github.com/mickaelbaron/jaxws-tutorial).

Pour aller plus loin, vous pouvez consulter les ressources suivantes :

* [Support de cours REST](http://mbaron.developpez.com/soa/rest "Support de cours REST") ;
* [Support de cours JAX-RS](http://mbaron.developpez.com/soa/jaxrs "Support de cours JAX-RS").