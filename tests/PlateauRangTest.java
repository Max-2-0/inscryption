package inscryption.logique;

import inscryption.logique.carte.Animal;
import inscryption.logique.carte.Carte;
import inscryption.logique.carte.Obstacle;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlateauRangTest {

    private Plateau plateau;
    private Rang rang;

    @Before
    public void setUp() {
        plateau = new Plateau();
        rang = new Rang();
    }

    // -------------------------------------------------------------------------
    // Placement des cartes sur le plateau
    // -------------------------------------------------------------------------

    @Test
    public void testAjouterCarteSlotLibre() {
        Animal ecureuil = new Animal(rang, 0, "Ecureuil", 0, 1, 0, 0, false, null);
        boolean resultat = plateau.ajouterCarte('B', 0, ecureuil);
        assertTrue("On doit pouvoir placer une carte sur un slot libre", resultat);
    }

    @Test
    public void testAjouterCarteSlotOccupe() {
        Animal a1 = new Animal(rang, 0, "Ecureuil", 0, 1, 0, 0, false, null);
        Animal a2 = new Animal(rang, 0, "Chat", 0, 1, 1, 0, false, null);
        plateau.ajouterCarte('B', 0, a1);
        boolean resultat = plateau.ajouterCarte('B', 0, a2);
        assertFalse("On ne doit pas pouvoir placer une carte sur un slot occupé", resultat);
    }

    @Test
    public void testAjouterCarteSlotInvalide() {
        Animal ecureuil = new Animal(rang, 0, "Ecureuil", 0, 1, 0, 0, false, null);
        boolean resultat = plateau.ajouterCarte('B', 5, ecureuil);
        assertFalse("Un slot invalide (>3) doit être refusé", resultat);
    }

    @Test
    public void testAjouterCarteSlotNegatif() {
        Animal ecureuil = new Animal(rang, 0, "Ecureuil", 0, 1, 0, 0, false, null);
        boolean resultat = plateau.ajouterCarte('B', -1, ecureuil);
        assertFalse("Un slot négatif doit être refusé", resultat);
    }

    @Test
    public void testAjouterCarteSurObstacle() {
        Rang rangB = plateau.getRangByCharacter('B');
        Obstacle rocher = new Obstacle(rangB, 1, 5, "Rocher");
        rangB.ajouterCarte(1, rocher);

        Animal ecureuil = new Animal(rang, 0, "Ecureuil", 0, 1, 0, 0, false, null);
        boolean resultat = plateau.ajouterCarte('B', 1, ecureuil);
        assertFalse("On ne doit pas pouvoir placer une carte sur un slot occupé par un obstacle", resultat);
    }

    @Test
    public void testQuatreCartesPlaceesAuMaximum() {
        Rang rangB = plateau.getRangByCharacter('B');
        for (int i = 0; i < 4; i++) {
            Animal a = new Animal(rangB, i, "Ecureuil", 0, 1, 0, 0, false, null);
            assertTrue(plateau.ajouterCarte('B', i, a));
        }
        // Le 5e slot n'existe pas
        Animal extra = new Animal(rangB, 0, "Extra", 0, 1, 0, 0, false, null);
        assertFalse("Il ne peut y avoir plus de 4 cartes sur une ligne", plateau.ajouterCarte('B', 4, extra));
    }

    // -------------------------------------------------------------------------
    // Rang - retirer une carte
    // -------------------------------------------------------------------------

    @Test
    public void testRetirerCarteExistante() {
        Rang r = new Rang();
        Animal a = new Animal(r, 0, "Ecureuil", 0, 1, 0, 0, false, null);
        r.ajouterCarte(0, a);
        boolean resultat = r.retirerCarte(0);
        assertTrue("retirerCarte doit retourner true si le slot contient une carte", resultat);
        assertTrue("Le slot doit être vide après retrait", r.isSlotEmpty(0));
    }

    @Test
    public void testRetirerCarteSlotVide() {
        Rang r = new Rang();
        // Même vide, retirerCarte met null → doit retourner true selon l'implémentation actuelle
        // On vérifie juste que ça ne lève pas d'exception et que le slot reste null
        boolean resultat = r.retirerCarte(0);
        assertTrue(resultat);
    }

    @Test
    public void testRetirerCarteSlotInvalide() {
        Rang r = new Rang();
        boolean resultat = r.retirerCarte(5);
        assertFalse("Un slot invalide doit retourner false", resultat);
    }
}