
# Bati-Cuisine

**Version :** 1.0  
**Auteur :** Noura Malek

## Description

Bati-Cuisine est une application Java destinée aux professionnels de la construction et de la rénovation de cuisines. Elle permet d'estimer les coûts des projets en fonction des matériaux, de la main-d'œuvre, et des spécifications des clients.

## Fonctionnalités principales

- **Estimation des coûts** : Calcule les coûts des matériaux et de la main-d'œuvre facturée à l'heure.
- **Gestion des clients** : Gère les informations des clients et leurs projets.
- **Génération de devis** : Produit des devis détaillés pour chaque projet.
- **Suivi des projets** : Vue d'ensemble des aspects financiers et logistiques des projets.

## Prérequis

- **Java 8** ou supérieur
- **PostgreSQL** pour la base de données
- **Maven** pour la gestion des dépendances

## Installation

1. **Cloner le projet :**
   ```bash
   git clone https://github.com/ton-compte/Bati-Cuisine.git
   ```

2. **Compiler le projet :**
   Depuis le répertoire du projet, exécutez :
   ```bash
   mvn clean install
   ```

3. **Exécuter l'application :**
   ```bash
   java -jar target/Bati-Cuisine-1.0-SNAPSHOT.jar
   ```

## Utilisation

1. Lancer l'application pour gérer les clients et estimer les coûts de projets.
2. Les données sont stockées dans une base de données PostgreSQL.

## Base de données

Exemple de configuration pour `application.properties` :
```
jdbc.url=jdbc:postgresql://localhost:5432/BatiCuisine
jdbc.username=your-username
jdbc.password=your-password
```






## Diagramme de classe UML

Le diagramme de classe UML est disponible à l'adresse suivante :  
**[Lien vers le diagramme UML](https://app.diagrams.net/#G1mdNM9pOPBKtzyJ29zDI3KpWzr5p-m3og#%7B%22pageId%22%3A%227w7gfXotGb9U5uPjj1Nj%22%7D)**

## Présentation du projet

La présentation détaillée de Bati-Cuisine est disponible ici :  
**[Lien vers la présentation](https://www.canva.com/design/DAGRrcxCZFk/Owwob4iYclU2XMfJ5FJdIw/edit?utm_content=DAGRrcxCZFk&utm_campaign=designshare&utm_medium=link2&utm_source=sharebutton)**

## Gestion de projet

La planification et le suivi du projet sont gérés sur JIRA. Pour consulter les tâches et l'avancement du projet, visitez :  
**[Lien vers JIRA](https://maleknoura098.atlassian.net/jira/software/c/projects/BAT/boards/7/backlog?epics=visible)**


