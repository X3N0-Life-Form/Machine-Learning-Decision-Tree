Machine-Learning-Decision-Tree
==============================

Auteurs:
  Adrien Droguet
  Sara Tari

Utilisation:
  java -jar MLDT.jar
  
  Prend jusqu'à 3 arguments:
    -file [string]      chemin du fichier à traiter, par défaut resources/data/weather.nominal.arff
    -maxDepth [int]     profondeur maximale de l'arbre, par défaut 5
    -impurity [double]  taux d'impureté toléré, par défaut 0.05

Resources:
  Rapport.pdf       rapport de projet
  MLDT.jar          .jar exécutable
  resources/data/   ensemble de fichiers .arff
  doc/index.html    javadoc de l'application
  src/              code source de l'application
  test/             code source des tests de l'application
  build.xml         utilisé par ant pour générer un nouveau .jar
  LICENSE           cette application est sous licence GPL
