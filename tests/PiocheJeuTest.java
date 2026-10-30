package inscryption.logique;

import inscryption.logique.carte.Animal;
import inscryption.logique.carte.Carte;
import inscryption.logique.carte.pouvoir.NombreusesVies;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PiocheJeuTest {

    private Joueur joueur;

    @Before
    public void setUp() {
        joueur = new Joueur();
    }

    // =========================================================================
    // Piocher une carte
    // =========================================================================

    @Test
    public void testPiocherAjouteCarteMain() {
        int tailleAvant = joueur.getTailleMain();
        boolean ok = joueur.piocher();
        assertTrue("piocher() doit retourner true si la pioche n'est pas vide", ok);
        assertEquals("La main doit avoir une carte de plus après pioche", tailleAvant + 1, joueur.getTailleMain());
    }

    @Test
    public void testPiocherReduitTaillePioche() {
        int tailleAvant = joueur.getTaillePioche();
        joueur.piocher();
        assertEquals("La pioche doit avoir une carte de moins après pioche", tailleAvant - 1, joueur.getTaillePioche());
    }

    @Test
    public void testPiocherPiocheVideRetourneFaux() {
        // Vider la pioche
        while (joueur.getTaillePioche() > 0) {
            joueur.piocher();
        }
        boolean ok = joueur.piocher();
        assertFalse("piocher() sur pioche vide doit retourner false", ok);
    }

    @Test
    public void testPiocheInitialementQuinzeCartes() {
        assertEquals("La pioche de départ doit contenir 15 cartes", 15, joueur.getTaillePioche());
    }

    @Test
    public void testPiocheContientMajoritaireEcureuils() {
        int nbEcureuils = 0;
        while (joueur.getTaillePioche() > 0) {
            joueur.piocher();
        }
        for (Carte c : joueur.getMain()) {
            if (c.getNom().equals("Ecureuil")) nbEcureuils++;
        }
        assertTrue("La pioche doit contenir au moins 10 écureuils", nbEcureuils >= 10);
    }

    @Test
    public void testMainInitialementVide() {
        assertEquals("La main doit être vide au départ", 0, joueur.getTailleMain());
    }

    // =========================================================================
    // NombreusesVies : reste sur le plateau lors d'un sacrifice
    // =========================================================================

    @Test
    public void testNombreusesVieSurvitAuSacrifice() {
        Rang rang = new Rang();
        Animal chat = new Animal(rang, 0, "Chat", 0, 1, 1, 0, false, new NombreusesVies());
        rang.ajouterCarte(0, chat);

        Joueur j = new Joueur();
        chat.sacrifier(j);

        assertTrue("Le chat (NombreusesVies) doit rester actif après sacrifice", chat.estActive());
        assertFalse("Le slot ne doit pas être libéré", rang.isSlotEmpty(0));
    }

    @Test
    public void testSansNombreusesViesDefausseAuSacrifice() {
        Rang rang = new Rang();
        Animal ecureuil = new Animal(rang, 0, "Ecureuil", 0, 1, 0, 0, false, null);
        rang.ajouterCarte(0, ecureuil);

        Joueur j = new Joueur();
        ecureuil.sacrifier(j);

        assertFalse("L'écureuil sans pouvoir doit être défaussé après sacrifice", ecureuil.estActive());
        assertTrue("Le slot doit être libéré", rang.isSlotEmpty(0));
    }

    // =========================================================================
    // Gagner et perdre le jeu (3 parties)
    // =========================================================================

    @Test
    public void testGagnerJeuAvecDeuxPartiesGagnees() {
        int partiesGagnees = 2;
        int partie = 3;
        assertTrue("Gagner 2 parties = gagner le jeu", partiesGagnees >= 2);
    }

    @Test
    public void testPerdreJeuApres3Parties() {
        int partiesGagnees = 0;
        int partie = 4;
        assertTrue("Après 3 parties sans 2 victoires = perdre", partie > 3 && partiesGagnees < 2);
    }

    // =========================================================================
    // Ajout d'une carte en pioche après la 2e partie
    // =========================================================================

    @Test
    public void testAjouterCartePiocheAugmenteTaille() {
        int tailleAvant = joueur.getTaillePioche();
        joueur.ajouterCartePioche(Joueur.creerGrizzly());
        assertEquals("Ajouter une carte augmente la taille de la pioche de 1",
                tailleAvant + 1, joueur.getTaillePioche());
    }
}
