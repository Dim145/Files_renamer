# Files_renamer

File Renamer permet de renommer des fichiers à partir d'un répertoire et ses sous-répertoires.
Cela peut être particulièrement utile pour une gestion des séries ect... <br/>
C'est d'ailleurs pour cette
raison que j'ai eu l'idée de ce projet. <br/>
En plus de ces fonctions automatiques, une recherche est effectuée sur le web pour chercher les séries ou films sur des bases de données en ligne.
Cela permet de corriger, traduire ou d'ajouter certaines informations telle que le titre de l'épisode.

## Pour commencer

Cette application as trois fonctions différentes :

- SERIES qui est le script le plus automatique.
- ALEANAME qui est plus spécifique et permet de mettre un nombre aléatoire devant les noms de fichiers.
- AUTRES qui est le plus global mais aussi le moins automatique. Renomme TOUS les fichiers selon un paterne.

### Pré-requis

Ce qu'il est requis pour commencer avec votre projet...

- **Java 8** (1.8)
- **Microsoft.NET Frameworks 3.5** ou supérieur pour le Launcher Windows
- Pour le développement, **IntelliJ Idea** est recommandé
- Pour compiler, **Maven** doit être installé et configuré

### Installation

Le fichier JAR peut être placé n'importe où pour être exécuté.
Si vous voulez execute le jar avec une commande, il faudra compiler le Launcher.
Pour Windows uniquement (pour l'instant ?).

Compilation du launcher :
    csc renameFile.cs
    
## Démarrage
    
Execution du launcher:
    renameFile.exe [SERIES,ALEANAME,AUTRES]

Pour exécuter le jar:
    java -jar path/jarName.jar ./ [SERIES,ALEANAME,AUTRES]
    
Le ./ permets au programme de démarrer directement avec le repertoire courant de saisie,
et les trois options permettent à celle-ci d'être pré sélectionné.

## Fabriqué avec
* [IntelliJ Idea](https://www.jetbrains.com/fr-fr/idea/) - IDE version Community spécialisé en Java
* [Maven](https://maven.apache.org/) - Framework Java pour compiler et créer les Jars

## Versions
Liste des versions :
* **Dernière version :** 5.3
* **Liste des versions :** [Cliquer pour afficher](https://github.com/Dim145/Files_renamer/tags)

## Auteurs
* **Dimitri Dubois** _alias_ [@Dim145](https://github.com/Dim145)
