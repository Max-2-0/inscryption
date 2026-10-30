package inscryption.logique;

import inscryption.logique.carte.Animal;
import inscryption.logique.carte.Carte;
import inscryption.logique.carte.Obstacle;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttaqueScoreTest {

    private Rang rangJoueur;
    private Rang rangAdversaire;

    @Before
    public void setUp() {
        rangJoueur    = new Rang();
        rangAdversaire = new Rang();
    }

    // -------------------------------------------------------------------------
    // Attaque d'une carte contre une autre carte
    // -------------------------------------------------------------------------

    @Test
    public void testAttaqueReduitPointsDeVie() {
        Animal loup = new Animal(rangJoueur, 0, "Loup", 3, 2, 2, 0, false, null);
        Animal hermine = new Animal(rangAdversaire, 0, "Hermine", 1, 3, 1, 0, false, null);

        loup.infligerDegats(loup.getAttaque(), hermine);

        assertEquals("L'hermine doit avoir 0 PV après 3 dégâts", 0, hermine.getVie());
    }

    @Test
    public void testAttaqueSurObstacleReduitVie() {
        Animal loup = new Animal(rangJoueur, 0, "Loup", 3, 2, 2, 0, false, null);
        Obstacle rocher = new Obstacle(rangAdversaire, 0, 5, "Rocher");

        loup.infligerDegats(loup.getAttaque(), rocher);

        assertEquals("Le rocher doit avoir 2 PV après 3 dégâts", 2, rocher.getVie());
    }

    @Test
    public void testCarteDefausseeQuandPvNuls() {
        Animal grizzly = new Animal(rangJoueur, 0, "Grizzly", 4, 6, 3, 0, false, null);
        Animal ecureuil = new Animal(rangAdversaire, 0, "Ecureuil", 0, 1, 0, 0, false, null);
        rangAdversaire.ajouterCarte(0, ecureuil);

        grizzly.infligerDegats(4, ecureuil);

        assertFalse("L'écureuil doit être inactif après avoir reçu des dégâts fatals", ecureuil.estActive());
        assertTrue("Le slot doit être libéré après défausse", rangAdversaire.isSlotEmpty(0));
    }

    @Test
    public void testCarteResteActiveAvecPvRestants() {
        Animal loup = new Animal(rangJoueur, 0, "Loup", 3, 2, 2, 0, false, null);
        Animal grizzly = new Animal(rangAdversaire, 0, "Grizzly", 4, 6, 3, 0, false, null);
        rangAdversaire.ajouterCarte(0, grizzly);

        loup.infligerDegats(loup.getAttaque(), grizzly);

        assertTrue("Le grizzly doit rester actif avec 3 PV restants", grizzly.estActive());
    }

    // -------------------------------------------------------------------------
    // Attaque fin de tour : toutes les cartes du joueur attaquent
    // -------------------------------------------------------------------------

    @Test
    public void testToutesCartesAttaquentEnFinDeTour() {
        // Rang joueur : Loup (slot 0) + Moineau (slot 2)
        Animal loup = new Animal(rangJoueur, 0, "Loup", 3, 2, 2, 0, false, null);
        Animal moineau = new Animal(rangJoueur, 2, "Moineau", 1, 2, 1, 0, true, null);
        rangJoueur.ajouterCarte(0, loup);
        rangJoueur.ajouterCarte(2, moineau);

        // Rang adversaire : Hermine en face du Loup
        Animal hermine = new Animal(rangAdversaire, 0, "Hermine", 1, 3, 1, 0, false, null);
        rangAdversaire.ajouterCarte(0, hermine);

    }

    // -------------------------------------------------------------------------
    // Mise à jour du score
    // -------------------------------------------------------------------------

    @Test
    public void testScoreInitialementZero() {
        Joueur joueur = new Joueur();
        assertEquals(0, joueur.getSang());
    }

    @Test
    public void testUpdateScoreAugmente() {
        Joueur joueur = new Joueur();
        joueur.updateSang(3);
        assertEquals(3, joueur.getSang());
    }

    @Test
    public void testUpdateScoreMultiplesAppels() {
        Joueur joueur = new Joueur();
        joueur.updateSang(2);
        joueur.updateSang(2);
        joueur.updateSang(1);
        assertEquals(5, joueur.getSang());
    }
}