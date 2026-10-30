package inscryption.logique;

import inscryption.logique.carte.Animal;
import inscryption.logique.carte.Carte;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class JoueurTest {

    private Joueur joueur;
    private Rang rang;

    @Before
    public void setUp() {
        joueur = new Joueur();
        rang = new Rang();
    }




    // -------------------------------------------------------------------------
    // Mise à jour du sang
    // -------------------------------------------------------------------------

    @Test
    public void testUpdateSangAugmente() {
        joueur.updateSang(2);
        assertEquals(2, joueur.getSang());
    }

    @Test
    public void testUpdateSangAccumule() {
        joueur.updateSang(1);
        joueur.updateSang(3);
        assertEquals(4, joueur.getSang());
    }
}