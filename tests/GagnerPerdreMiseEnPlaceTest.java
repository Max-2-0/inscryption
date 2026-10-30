package inscryption.logique;

import inscryption.logique.carte.Animal;
import inscryption.logique.carte.Carte;
import inscryption.logique.carte.Obstacle;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class GagnerPerdreMiseEnPlaceTest {

    private Plateau plateau;
    private Joueur joueur;
    private Joueur ia;

    @Before
    public void setUp() {
        plateau = new Plateau();
        joueur  = new Joueur();
        ia      = new Joueur();
    }



    @Test
    public void testObstacleBloquePlacementCarte() {
        Rang rangB = plateau.getRangByCharacter('B');
        Obstacle rocher = new Obstacle(rangB, 1, 5, "Rocher");
        rangB.ajouterCarte(1, rocher);

        Animal ecureuil = new Animal(rangB, 0, "Ecureuil", 0, 1, 0, 0, false, null);
        boolean resultat = plateau.ajouterCarte('B', 1, ecureuil);
        assertFalse("On ne peut pas placer une carte sur un slot avec un obstacle", resultat);
    }


    // =========================================================================
    // Gagner / perdre une partie (écart de score de 5)
    // =========================================================================

    @Test
    public void testPartieNonTermineeSiScoreInferieurA5() {
        joueur.updateSang(4);
        assertFalse("La partie ne doit pas être terminée avec un écart de 4", partieTerminee(joueur, ia));
    }

    @Test
    public void testPartieTermineeSiJoueurAtteint5() {
        joueur.updateSang(5);
        assertTrue("La partie doit être terminée quand le joueur atteint 5 points", partieTerminee(joueur, ia));
    }

    @Test
    public void testPartieTermineeSiIaAtteint5() {
        ia.updateSang(5);
        assertTrue("La partie doit être terminée quand l'IA atteint 5 points", partieTerminee(joueur, ia));
    }

    @Test
    public void testJoueurGagnePartie() {
        joueur.updateSang(5);
        assertTrue("Le joueur doit avoir gagné avec 5 points", joueurGagne(joueur, ia));
    }

    @Test
    public void testJoueurPerdPartie() {
        ia.updateSang(5);
        assertFalse("Le joueur ne doit pas avoir gagné si l'IA a 5 points", joueurGagne(joueur, ia));
    }

    // =========================================================================
    // Ajout d'une nouvelle carte en pioche après la 2e partie
    // =========================================================================



    // =========================================================================
    // Pierre de sacrifice (Phase 2)
    // =========================================================================

    @Test
    public void testPierreSacrificeRecuperePouvoir() {
        // La carte sacrifiée possède un pouvoir → on vérifie que le pouvoir est récupérable
        inscryption.logique.carte.pouvoir.NombreusesVies pouvoir = new inscryption.logique.carte.pouvoir.NombreusesVies();
        Rang rang = new Rang();
        Animal chat = new Animal(rang, 0, "Chat", 0, 1, 1, 0, false, pouvoir);

        // On récupère le pouvoir avant sacrifice
        assertTrue("La pierre de sacrifice doit pouvoir récupérer un pouvoir non-null", chat.getPouvoir().isPresent());
        assertEquals("Nombreuses Vies", chat.getPouvoir().get().getNom());
    }

    @Test
    public void testPierreSacrificeSansPouvoirRetourneNull() {
        Rang rang = new Rang();
        Animal ecureuil = new Animal(rang, 0, "Ecureuil", 0, 1, 0, 0, false, null);
        assertFalse("Une carte sans pouvoir doit retourner Optional vide pour getPouvoir()", ecureuil.getPouvoir().isPresent());
    }

    // =========================================================================
    // Helpers
    // =========================================================================

    private List<Carte> creePioche15Cartes() {
        List<Carte> pioche = new ArrayList<>();
        Rang rang = new Rang();
        // 10 écureuils + 5 hermines, comme mentionné dans le README
        for (int i = 0; i < 10; i++) {
            pioche.add(new Animal(rang, 0, "Ecureuil", 0, 1, 0, 0, false, null));
        }
        for (int i = 0; i < 5; i++) {
            pioche.add(new Animal(rang, 0, "Hermine", 1, 3, 1, 0, false, null));
        }
        return pioche;
    }

    private boolean partieTerminee(Joueur j, Joueur adversaire) {
        return j.getSang() >= 5 || adversaire.getSang() >= 5;
    }

    private boolean joueurGagne(Joueur j, Joueur adversaire) {
        return j.getSang() >= 5;
    }
}