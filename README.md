# Inscryption

## Introduction

Inscryption is a Java card-game prototype inspired by the eponymous game. Play against an AI opponent in a terminal-style window, build your board, sacrifice cards for blood, and use card abilities to win.

### Gameplay

- Each match is played on a four-slot board.
- Draw cards, play them by paying their blood or bone cost, and sacrifice cards to gain blood.
- End your turn to resolve attacks and let the AI play.
- Win a match by getting at least five points ahead of your opponent. Win all three matches to win the game.

### Requirements

- A Java Development Kit (JDK), with `java` and `javac` available on your `PATH`.
- A graphical desktop environment for the game window.

The required libraries, including Lanterna and JUnit, are included in `deps/`.

### Build and run

Run these commands from the repository root.

**Windows (PowerShell):**

```powershell
New-Item -ItemType Directory -Force out
javac -sourcepath src -cp "deps/*" -d out src/Main.java
java -cp "out;deps/*" Main
```

**Linux / macOS:**

```sh
bash compile.sh
bash run.sh
```

### Tests

Tests use JUnit 4 and are located in `tests/`.

**Windows (PowerShell), after building:**

```powershell
javac -cp "out;deps/*" -d out (Get-ChildItem tests -Filter *.java | ForEach-Object { $_.FullName })
java -cp "out;deps/*" org.junit.runner.JUnitCore JoueurMainTest JoueurTest PiocheJeuTest PlateauRangTest AttaqueScoreTest GagnerPerdreMiseEnPlaceTest PouvoirTest
```

**Linux / macOS, after building:**

```sh
javac -cp "out:deps/*" -d out tests/*.java
java -cp "out:deps/*" org.junit.runner.JUnitCore JoueurMainTest JoueurTest PiocheJeuTest PlateauRangTest AttaqueScoreTest GagnerPerdreMiseEnPlaceTest PouvoirTest
```

### Controls

- **Up / Down:** choose an action.
- **Left / Right:** navigate cards or board slots.
- **Enter:** confirm.
- **Escape:** cancel the current card or slot selection.


## Introduction

Inscryption est un prototype de jeu de cartes en Java, inspiré du jeu du même nom. Affrontez une IA dans une fenêtre au style terminal, développez votre plateau, sacrifiez des cartes pour obtenir du sang et utilisez leurs pouvoirs pour gagner.

### Fonctionnement

- Chaque partie se joue sur un plateau de quatre emplacements.
- Piochez et jouez des cartes en payant leur coût en sang ou en os, et sacrifiez des cartes pour obtenir du sang.
- Terminez votre tour pour résoudre les attaques et laisser l’IA jouer.
- Gagnez une manche en prenant au moins cinq points d’avance sur l’adversaire. Remportez les trois manches pour gagner la partie.

### Prérequis

- Un kit de développement Java (JDK), avec `java` et `javac` disponibles dans le `PATH`.
- Un environnement de bureau graphique pour afficher la fenêtre du jeu.

Les bibliothèques nécessaires, dont Lanterna et JUnit, sont fournies dans `deps/`.

### Compiler et lancer le jeu

Exécutez ces commandes depuis la racine du dépôt.

**Windows (PowerShell) :**

```powershell
New-Item -ItemType Directory -Force out
javac -sourcepath src -cp "deps/*" -d out src/Main.java
java -cp "out;deps/*" Main
```

**Linux / macOS :**

```sh
bash compile.sh
bash run.sh
```

### Tests

Les tests utilisent JUnit 4 et se trouvent dans `tests/`.

**Windows (PowerShell), après la compilation :**

```powershell
javac -cp "out;deps/*" -d out (Get-ChildItem tests -Filter *.java | ForEach-Object { $_.FullName })
java -cp "out;deps/*" org.junit.runner.JUnitCore JoueurMainTest JoueurTest PiocheJeuTest PlateauRangTest AttaqueScoreTest GagnerPerdreMiseEnPlaceTest PouvoirTest
```

**Linux / macOS, après la compilation :**

```sh
javac -cp "out:deps/*" -d out tests/*.java
java -cp "out:deps/*" org.junit.runner.JUnitCore JoueurMainTest JoueurTest PiocheJeuTest PlateauRangTest AttaqueScoreTest GagnerPerdreMiseEnPlaceTest PouvoirTest
```

### Commandes

- **Haut / Bas :** choisir une action.
- **Gauche / Droite :** parcourir les cartes ou les emplacements du plateau.
- **Entrée :** confirmer.
- **Échap :** annuler la sélection de carte ou d’emplacement en cours.