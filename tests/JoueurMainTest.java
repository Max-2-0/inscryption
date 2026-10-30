import static org.junit.Assert.assertEquals;

import org.junit.Test;

import inscryption.logique.Joueur;

public class JoueurMainTest {

    @Test
    public void testGetTailleMainRetourneLeNombreDeCartesEnMain() {
        Joueur joueur = new Joueur();

        assertEquals("La main doit être vide au départ", 0, joueur.getTailleMain());
    }
}
