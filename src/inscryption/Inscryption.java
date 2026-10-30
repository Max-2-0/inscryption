package inscryption;

import inscryption.affichage.Affichage;
import inscryption.affichage.GererTouches;
import inscryption.affichage.Etape;
import inscryption.logique.Joueur;
import inscryption.logique.Plateau;
import inscryption.logique.Rang;
import inscryption.logique.carte.Carte;
import inscryption.logique.carte.Obstacle;
import inscryption.logique.carte.pouvoir.GenericPouvoir;
import com.googlecode.lanterna.input.KeyStroke;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class Inscryption {
    private Affichage m_affichage;
    private Joueur m_joueur;
    private Joueur m_ia;
    private Plateau m_plateau;
    private GererTouches m_gererTouches;

    private int m_scoreJoueur;
    private int m_scoreIa;
    private int m_partie;
    private int m_partiesGagnees;
    private boolean m_piocherEffectue;
    private boolean m_jeuEnCours;
    private boolean m_partieEnCours;
    private String m_message;
    private ArrayList<Carte> m_cartesProposees;
    private int m_indexCarteEnCoursPlacement;
    private int m_sangNecessaire;
    private int m_tourCourant;
    private boolean m_pierrePending;
    private final HashSet<Carte> m_cartesSacrifieesCeTour = new HashSet<>();
    private final Random m_random = new Random();

    public Inscryption() {
        m_gererTouches = new GererTouches(this);
        m_affichage = new Affichage(m_gererTouches);
        m_joueur = new Joueur();
        m_ia = new Joueur();
        m_plateau = new Plateau();
        m_scoreJoueur = 0;
        m_scoreIa = 0;
        m_partie = 1;
        m_partiesGagnees = 0;
        m_piocherEffectue = false;
        m_jeuEnCours = true;
        m_partieEnCours = true;
        m_tourCourant = 0;
        m_pierrePending = false;
        m_message = "Bienvenue ! ↑↓ pour choisir une action, Entrée pour confirmer.";
        m_cartesProposees = new ArrayList<>();
        m_indexCarteEnCoursPlacement = -1;
        m_sangNecessaire = 0;
    }

    public Joueur getJoueur() { return m_joueur; }
    public Joueur getIa() { return m_ia; }
    public Plateau getPlateau() { return m_plateau; }
    public int getScoreJoueur() { return m_scoreJoueur; }
    public int getScoreIa() { return m_scoreIa; }
    public int getPartie() { return m_partie; }
    public String getMessage() { return m_message; }
    public void setMessage(String message) { this.m_message = message; }
    public ArrayList<Carte> getCartesProposees() { return m_cartesProposees; }
    public int getIndexCarteEnCoursPlacement() { return m_indexCarteEnCoursPlacement; }
    public int getSangNecessaire() { return m_sangNecessaire; }
    public boolean isJeuEnCours() { return m_jeuEnCours; }
    public boolean isPartieEnCours() { return m_partieEnCours; }
    public boolean isPierrePending() { return m_pierrePending; }
    public void setPierrePending(boolean val) { m_pierrePending = val; }
    public ArrayList<Carte> getPiocheJoueur() { return m_joueur.getPioche(); }
    public Etape getEtape() { return m_gererTouches.getEtape(); }
    public int getSlotSelected() { return m_gererTouches.get_slotSelected(); }
    public int getSlotMainSelected() { return m_gererTouches.get_slotMainSelected(); }

    public void setJeuEnCours(boolean val) { m_jeuEnCours = val; }
    public void setIndexCarteEnCoursPlacement(int val) { m_indexCarteEnCoursPlacement = val; }
    public void setSangNecessaire(int val) { m_sangNecessaire = val; }

    public void initialiserPartie() {
        m_plateau = new Plateau();
        m_scoreJoueur = 0;
        m_scoreIa = 0;
        m_piocherEffectue = false;
        m_indexCarteEnCoursPlacement = -1;
        m_sangNecessaire = 0;
        m_partieEnCours = true;
        m_cartesSacrifieesCeTour.clear();
        m_tourCourant = 0;
        m_pierrePending = false;
        m_message = "Partie " + m_partie + " - Bonne chance !";

        m_joueur = new Joueur();
        m_ia = new Joueur();

        for (int i = 0; i < 4; i++) {
            m_joueur.piocher();
            m_ia.piocher();
        }

        placerObstaclesDepart();
        majRangE();
    }

    private void placerObstaclesDepart() {
        int nombreObstacles = (m_partie == 1) ? 1 : 2;

        for (int i = 0; i < nombreObstacles; i++) {
            Rang rang = m_random.nextBoolean() ? m_plateau.getRangByCharacter('A') : m_plateau.getRangByCharacter('B');
            int slot = m_random.nextInt(4);

            if (!rang.isSlotEmpty(slot)) {
                i--;
                continue;
            }

            String nom = m_random.nextBoolean() ? "Rocher" : "Sapin";
            int vie = "Rocher".equals(nom) ? 5 : 3;
            rang.ajouterCarte(slot, new Obstacle(rang, slot, vie, nom));
        }
    }

    public void mainLoop() {
        m_affichage.effacer();
        m_affichage.affichageJeu(this);
        m_affichage.rafraichir();

        try {
            KeyStroke key = m_affichage.waitForKey();
            m_gererTouches.acheminer(key);
        } catch (IOException e) {
            m_gererTouches.gererErreur(e);
        }
    }

    public void mainLoopFin() {
        m_affichage.effacer();
        m_affichage.affichageJeu(this);
        m_affichage.rafraichir();
        try { m_affichage.waitForKey(); } catch (java.io.IOException e) {}
    }

    public void fermer() { m_affichage.fermer(); }

    public void piocher() {
        if (m_piocherEffectue) {
            m_message = "Vous avez déjà pioché ce tour.";
            return;
        }
        m_piocherEffectue = m_joueur.piocher();
        m_message = m_piocherEffectue ? "Vous piochez une carte." : "La pioche est vide !";
    }

    public boolean demarrerPlacement(int indexMain) {
        if (indexMain < 0 || indexMain >= m_joueur.getTailleMain()) return false;

        Carte carte = m_joueur.getMain().get(indexMain);
        if (!carte.peutEtrePlacee()) {
            m_message = "Impossible de placer un obstacle.";
            return false;
        }

        if (carte.getCoutOs() > 0 && m_joueur.getOs() < carte.getCoutOs()) {
            m_message = "Pas assez d'os (requis: " + carte.getCoutOs() + ", disponible: " + m_joueur.getOs() + ")";
            return false;
        }

        m_indexCarteEnCoursPlacement = indexMain;
        m_sangNecessaire = carte.getCoutSang();

        if (m_sangNecessaire > 0 && m_joueur.getSang() < m_sangNecessaire) {
            m_message = "Sacrifiez des cartes (sang requis: " + m_sangNecessaire + ").";
        }
        return true;
    }

    private boolean estChatNombreusesVies(Carte carte) {
        return carte != null
            && carte.getNom().equals("Chat")
            && carte.getNomPouvoir().map(nom -> nom.equals("Nombreuses Vies")).orElse(false);
    }

    private boolean peutSacrifierCarte(Carte carte) {
        if (carte == null) {
            return false;
        }
        if (!carte.peutEtrePlacee()) {
            return false;
        }
        if (estChatNombreusesVies(carte) && m_cartesSacrifieesCeTour.contains(carte)) {
            return false;
        }
        return true;
    }

    public void sacrifierCartePour(int slotPlateau) {
        Rang rangB = m_plateau.getRangByCharacter('B');
        Carte carte = rangB.getCarte(slotPlateau);
        if (!peutSacrifierCarte(carte)) {
            if (carte == null) {
                m_message = "Pas de carte à sacrifier ici.";
            } else if (!carte.peutEtrePlacee()) {
                m_message = "Impossible de sacrifier un obstacle.";
            } else {
                m_message = "Ce chat a déjà été sacrifié ce tour.";
            }
            return;
        }

        if (estChatNombreusesVies(carte)) {
            m_cartesSacrifieesCeTour.add(carte);
        }

        m_joueur.updateSang(1);
        carte.sacrifier(m_joueur);

        int sangActuel = m_joueur.getSang();
        m_message = sangActuel >= m_sangNecessaire
            ? "Assez de sang ! Choisissez maintenant l'emplacement."
            : "Sang: " + sangActuel + "/" + m_sangNecessaire + " - Continuez à sacrifier.";
    }

    public void sacrifierCarteLibre(int slotPlateau) {
        Rang rangB = m_plateau.getRangByCharacter('B');
        Carte carte = rangB.getCarte(slotPlateau);
        if (!peutSacrifierCarte(carte)) {
            if (carte == null) {
                m_message = "Aucune carte à sacrifier ici.";
            } else if (!carte.peutEtrePlacee()) {
                m_message = "Impossible de sacrifier un obstacle.";
            } else {
                m_message = "Ce chat a déjà été sacrifié ce tour.";
            }
            return;
        }

        if (estChatNombreusesVies(carte)) {
            m_cartesSacrifieesCeTour.add(carte);
        }

        m_joueur.updateSang(1);
        carte.sacrifier(m_joueur);
        m_message = "Vous sacrifiez " + carte.getNom() + " et gagnez 1 sang.";
    }

    public boolean placerCarte(int indexMain, int slot) {
        if (indexMain < 0 || indexMain >= m_joueur.getTailleMain()) return false;

        Carte carte = m_joueur.getMain().get(indexMain);
        if (!carte.peutEtrePlacee()) {
            m_message = "Impossible de placer un obstacle.";
            return false;
        }
        if (!m_plateau.getRangByCharacter('B').isSlotEmpty(slot)) {
            m_message = "Emplacement occupé.";
            return false;
        }
        if (carte.getCoutOs() > 0 && !m_joueur.consommerOs(carte.getCoutOs())) {
            m_message = "Pas assez d'os.";
            return false;
        }
        if (carte.getCoutSang() > 0 && !m_joueur.consommerSang(carte.getCoutSang())) {
            m_message = "Pas assez de sang.";
            return false;
        }

        Rang rangB = m_plateau.getRangByCharacter('B');
        m_joueur.getMain().remove(indexMain);
        carte.placer(rangB, slot);
        carte.setProprietaire(m_joueur);
        rangB.ajouterCarte(slot, carte);

        m_message = carte.getNom() + " placé en B" + (slot + 1) + ".";
        m_indexCarteEnCoursPlacement = -1;
        m_sangNecessaire = 0;
        return true;
    }

    public void finDeTour() {
        m_cartesSacrifieesCeTour.clear();

        int scoreJ = m_plateau.resolveAttaques('B', 'A');
        int scoreI = m_plateau.resolveAttaques('A', 'B');
        m_scoreJoueur += scoreJ;
        m_scoreIa += scoreI;

        m_message = construireMessageAttaques(scoreJ, scoreI);

        if (verifierFinPartie()) return;

        m_ia.piocher();
        tourIA();
        m_plateau.majTourCartes();
        majRangE();
        m_piocherEffectue = false;
        m_tourCourant++;
        if (m_tourCourant == 2) m_pierrePending = true;
    }

    private String construireMessageAttaques(int scoreJ, int scoreI) {
        StringBuilder msg = new StringBuilder();
        if (scoreJ > 0) msg.append("Vous marquez ").append(scoreJ).append(" pt(s). ");
        if (scoreI > 0) msg.append("Adversaire marque ").append(scoreI).append(" pt(s). ");
        return msg.isEmpty() ? "Aucune attaque ce tour." : msg.toString();
    }

    private void tourIA() {
        Rang rangA = m_plateau.getRangByCharacter('A');
        for (int slot = 0; slot < 4; slot++) {
            if (!rangA.isSlotEmpty(slot) || m_ia.getMain().isEmpty()) continue;

            Carte carte = m_ia.getMain().get(0);
            if (!carte.peutEtrePlacee()) {
                m_ia.getMain().remove(0);
                continue;
            }
            m_ia.getMain().remove(0);
            carte.placer(rangA, slot);
            carte.setProprietaire(m_ia);
            rangA.ajouterCarte(slot, carte);
        }
    }

    private boolean verifierFinPartie() {
        if (m_scoreJoueur - m_scoreIa >= 5) {
            m_partiesGagnees++;
            m_partieEnCours = false;
            m_message = "Vous gagnez la partie " + m_partie + " !";
            return true;
        }
        if (m_scoreIa - m_scoreJoueur >= 5) {
            m_partieEnCours = false;
            m_message = "L'adversaire gagne la partie " + m_partie + ".";
            return true;
        }
        return false;
    }

    public boolean verifierFinJeu() {
        if (m_partiesGagnees == 3) {
            m_jeuEnCours = false;
            m_message = "Vous avez gagné les 3 parties ! Bravo !";
            return true;
        }
        if (m_partie >= 3) {
            m_jeuEnCours = false;
            m_message = "Vous avez perdu. " + m_partiesGagnees + "/3 parties gagnées.";
            return true;
        }
        return false;
    }

    public void passerPartieSuivante() {
        m_partie++;
        if (m_partie == 3) {
            preparerCartesAjout();
        } else {
            initialiserPartie();
        }
    }

    private void preparerCartesAjout() {
        m_cartesProposees = new ArrayList<>();
        m_cartesProposees.add(Joueur.creerGrizzly());
        m_cartesProposees.add(Joueur.creerCorbeau());
        m_message = "Choisissez une carte : ←→ pour naviguer, Entrée pour confirmer.";
    }

    public void ajouterCarteChoisie(int index) {
        if (index < 0 || index >= m_cartesProposees.size()) return;
        m_joueur.ajouterCartePioche(m_cartesProposees.get(index));
        m_cartesProposees.clear();
        initialiserPartie();
    }

    public void preparerPierreSacrifice() {
        m_joueur.piocher();
        m_message = "Pierre de sacrifice : une carte piochée ! Choisissez une carte en main à sacrifier (←→, Entrée).";
    }

    public void sacrifierPourPierre(int index) {
        ArrayList<Carte> main = m_joueur.getMain();
        if (index < 0 || index >= main.size()) return;
        Carte carte = main.get(index);
        m_joueur.setPouvoirPierre(carte.getPouvoir().orElse(null));
        main.remove(index);
        if (m_joueur.getPouvoirPierre().isPresent()) {
            m_message = "Pouvoir \"" + m_joueur.getPouvoirPierre().get().getNom()
                + "\" récupéré ! Choisissez la carte qui recevra le pouvoir (←→, Entrée).";
        } else {
            m_message = "Carte sans pouvoir sacrifiée.";
        }
    }

    public void appliquerPouvoirPierre(int index) {
        ArrayList<Carte> main = m_joueur.getMain();
        if (index < 0 || index >= main.size()) return;
        Carte cible = main.get(index);
        cible.setPouvoir(m_joueur.getPouvoirPierre().orElse(null));
        m_message = "Pouvoir appliqué à " + cible.getNom() + " !";
        m_joueur.setPouvoirPierre(null);
    }

    private void majRangE() {
        Rang rangE = m_plateau.getRangByCharacter('E');
        for (int i = 0; i < 4; i++) rangE.retirerCarte(i);
        ArrayList<Carte> mainIa = m_ia.getMain();
        for (int i = 0; i < Math.min(mainIa.size(), 4); i++) {
            rangE.ajouterCarte(i, mainIa.get(i));
        }
    }

    @Override
    public String toString() {
        return "Inscryption[partie=" + m_partie + " score=" + m_scoreJoueur + "/" + m_scoreIa + "]";
    }
}
