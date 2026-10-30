package inscryption.affichage;

import java.util.ArrayList;
import java.io.IOException;

import inscryption.logique.carte.Carte;
import inscryption.affichage.components.Cadre;
import inscryption.logique.Plateau;
import inscryption.logique.Rang;
import inscryption.Inscryption;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;
import com.googlecode.lanterna.terminal.swing.TerminalEmulatorDeviceConfiguration;
import com.googlecode.lanterna.terminal.swing.TerminalEmulatorColorConfiguration;
import com.googlecode.lanterna.TerminalSize;

public class Affichage {
    private Screen m_ecran;
    private TextGraphics m_tg;

    public static final int CARTE_WIDTH = 14;
    public static final int CARTE_HEIGHT = 7;
    public static final int CARTE_PADDING = 1;

    private static final String[] LIBELLES_ACTIONS = {
        "[Fin de tour]",
        "[Piocher]",
        "[Placer une carte]",
        "[Sacrifier]"
    };
    private static final TextColor[] COULEURS_ACTIONS = {
        TextColor.ANSI.GREEN,
        TextColor.ANSI.CYAN,
        TextColor.ANSI.YELLOW,
        TextColor.ANSI.RED
    };

    private final GererTouches m_gererTouches;

    public Affichage(GererTouches gererTouches) {
        this.m_gererTouches = gererTouches;
        initEcran();
        initTextGraphics();
    }

    private void initEcran() {
        try {
            SwingTerminalFrame terminal = new SwingTerminalFrame("Inscryption",
                new TerminalSize(100, 50),
                TerminalEmulatorDeviceConfiguration.getDefault(),
                SwingTerminalFontConfiguration.getDefault(),
                TerminalEmulatorColorConfiguration.getDefault());
            terminal.setVisible(true);
            m_ecran = new TerminalScreen(terminal);
            m_ecran.startScreen();
            m_ecran.setCursorPosition(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initTextGraphics() {
        m_tg = m_ecran.newTextGraphics();
    }

    public void effacer() { m_ecran.clear(); }

    public void rafraichir() {
        try {
            m_ecran.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void affichageJeu(Inscryption inscryption) {
        int xBase = 1;
        int yBase = 1;

        afficherEntete(inscryption, xBase, yBase);

        int yPlateau = yBase + 2;
        afficherLegendesRangs(xBase, yPlateau);
        affichagePlateau(inscryption.getPlateau(), xBase, yPlateau, inscryption);

        int yMain = yPlateau + 3 * (CARTE_HEIGHT + CARTE_PADDING) + 1;
        affichageMain(inscryption.getJoueur().getMain(), xBase, yMain);

        int xPioche = xBase + 4 * (CARTE_WIDTH + CARTE_PADDING) + 2;
        affichagePiocheEtRessources(inscryption, xPioche, yMain);

        int xDetails = xPioche + 15;
        afficherPanelDetails(inscryption, xDetails, yMain);

        int yActions = yMain + CARTE_HEIGHT + 1;
        affichageActions(xBase, yActions, inscryption);

        afficherMessage(inscryption.getMessage(), xBase, yActions + Action.MAX_ACTION + 1);
    }

    private void afficherEntete(Inscryption inscryption, int x, int y) {
        String entete = "=== Inscryption === Partie " + inscryption.getPartie() + "/3"
            + "  |  Score : Vous " + inscryption.getScoreJoueur()
            + " - Adversaire " + inscryption.getScoreIa() + "  ===";
        m_tg.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
        m_tg.putString(x, y, entete);
        m_tg.setForegroundColor(TextColor.ANSI.WHITE);
    }

    private void afficherLegendesRangs(int x, int yPlateau) {
        m_tg.setForegroundColor(TextColor.ANSI.BLACK_BRIGHT);
        m_tg.putString(x, yPlateau - 1, "Prochain coup adverse (E):");
        m_tg.putString(x, yPlateau + CARTE_HEIGHT + CARTE_PADDING - 1, "Plateau adverse (A):");
        m_tg.putString(x, yPlateau + 2 * (CARTE_HEIGHT + CARTE_PADDING) - 1, "Votre plateau (B):");
        m_tg.setForegroundColor(TextColor.ANSI.WHITE);
    }

    private void affichagePiocheEtRessources(Inscryption inscryption, int x, int y) {
        m_tg.putString(x, y, "Pioche :");
        affichagePioche(inscryption.getJoueur().getTaillePioche(), x, y + 1);

        m_tg.setForegroundColor(TextColor.ANSI.RED);
        m_tg.putString(x, y + CARTE_HEIGHT + 2, "Sang: " + inscryption.getJoueur().getSang());
        m_tg.setForegroundColor(TextColor.ANSI.WHITE);
        m_tg.putString(x, y + CARTE_HEIGHT + 3, "Os:   " + inscryption.getJoueur().getOs());
    }

    private void afficherMessage(String message, int x, int y) {
        m_tg.setForegroundColor(TextColor.ANSI.YELLOW);
        m_tg.putString(x, y, ">> " + message);
        m_tg.setForegroundColor(TextColor.ANSI.BLACK_BRIGHT);
        m_tg.putString(x, y + 1, "[Echap] Annuler | [Entree] Confirmer | [<>] Naviguer | [^v] Actions");
        m_tg.setForegroundColor(TextColor.ANSI.WHITE);
    }

    public void affichagePlateau(Plateau plateau, int x, int y, Inscryption inscryption) {
        Etape etape = m_gererTouches.getEtape();
        int slotSelected = m_gererTouches.get_slotSelected();

        for (int i = 0; i < Plateau.RANGS.length; i++) {
            Character rangChar = Plateau.RANGS[i];
            Rang unRang = plateau.getRangByCharacter(rangChar);
            int yRang = y + i * (CARTE_HEIGHT + CARTE_PADDING);
            affichageRang(unRang, x, yRang, etape, slotSelected, rangChar);
        }
    }

    private void affichageRang(Rang rang, int x, int y, Etape etape, int slotSelected, Character rangChar) {
        for (int slot = 0; slot < 4; slot++) {
            Carte carte = rang.getCarte(slot);
            int xSlot = x + slot * (CARTE_WIDTH + CARTE_PADDING);
            boolean surbrillance = estSlotSurbrille(rangChar, etape, slot, slotSelected);
            affichageSlot(rang.getNom(), carte, xSlot, y, surbrillance);
        }
    }

    private boolean estSlotSurbrille(Character rangChar, Etape etape, int slot, int slotSelected) {
        boolean selectionActive = etape == Etape.CHOIX_SLOT
            || etape == Etape.CHOIX_SACRIFICE
            || etape == Etape.CHOIX_SACRIFICE_LIBRE;
        return rangChar.equals('B') && selectionActive && slot == slotSelected;
    }

    private void affichageSlot(String nomRang, Carte carte, int x, int y, boolean surbrille) {
        Cadre cadre = new Cadre(x, y, CARTE_HEIGHT, CARTE_WIDTH);

        if (surbrille) {
            m_tg.setForegroundColor(TextColor.ANSI.YELLOW_BRIGHT);
            m_tg.setBackgroundColor(TextColor.ANSI.BLUE);
        } else {
            m_tg.setBackgroundColor(TextColor.ANSI.BLACK);
            m_tg.setForegroundColor(TextColor.ANSI.WHITE);
        }

        if (carte != null) {
            cadre.draw(m_tg, false);
            cadre.drawSeparation(m_tg, 2);
            affichageCarte(carte, x, y);
        } else {
            cadre.draw(m_tg, true);
            affichageSlotVide(nomRang, x, y);
        }

        m_tg.setForegroundColor(TextColor.ANSI.WHITE);
        m_tg.setBackgroundColor(TextColor.ANSI.BLACK);
    }

    private void affichageCarte(Carte carte, int x, int y) {
        String nom = carte.getNom().length() > CARTE_WIDTH - 2
            ? carte.getNom().substring(0, CARTE_WIDTH - 2)
            : carte.getNom();

        m_tg.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
        m_tg.putString(x + 1, y + 1, nom);
        m_tg.setForegroundColor(TextColor.ANSI.WHITE);
        m_tg.putString(x + 1, y + 3, "PV:" + carte.getVie());
        m_tg.putString(x + 1, y + 4, "ATK:" + carte.getAttaque());

        if (carte.estVolant()) m_tg.putString(x + 6, y + 4, "Volant");

        carte.getNomPouvoir().ifPresent(nomP -> {
            String affP = nomP.length() > CARTE_WIDTH - 2 ? nomP.substring(0, CARTE_WIDTH - 2) : nomP;
            m_tg.setForegroundColor(TextColor.ANSI.CYAN);
            m_tg.putString(x + 1, y + 5, affP);
            m_tg.setForegroundColor(TextColor.ANSI.WHITE);
        });
    }

    private void affichageSlotVide(String nom, int x, int y) {
        int offset = Math.max(0, (CARTE_WIDTH - nom.length()) / 2);
        m_tg.setForegroundColor(TextColor.ANSI.BLACK_BRIGHT);
        m_tg.putString(x + offset, y + CARTE_HEIGHT / 2, nom);
        m_tg.setForegroundColor(TextColor.ANSI.WHITE);
    }

    private void affichageActions(int x, int y, Inscryption inscryption) {
        Etape etape = m_gererTouches.getEtape();

        if (etape == Etape.CHOIX_SACRIFICE_PIERRE) {
            m_tg.setForegroundColor(TextColor.ANSI.MAGENTA);
            m_tg.putString(x, y, "Pierre de sacrifice - Choisissez une carte a sacrifier");
            m_tg.setForegroundColor(TextColor.ANSI.WHITE);
            return;
        }
        if (etape == Etape.CHOIX_CIBLE_PIERRE) {
            m_tg.setForegroundColor(TextColor.ANSI.MAGENTA);
            m_tg.putString(x, y, "Pierre de sacrifice - Choisissez la carte cible du pouvoir");
            m_tg.setForegroundColor(TextColor.ANSI.WHITE);
            return;
        }
        if (etape == Etape.FIN_PARTIE || etape == Etape.FIN_JEU) {
            m_tg.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
            m_tg.putString(x, y, "[Entree] Continuer");
            m_tg.setForegroundColor(TextColor.ANSI.WHITE);
            return;
        }

        if (etape != Etape.CHOIX_ACTION) return;

        for (int i = 0; i < Action.MAX_ACTION; i++) {
            boolean estSelected = m_gererTouches.get_actionSelected().getValue() == i;

            if (estSelected) {
                m_tg.setForegroundColor(TextColor.ANSI.BLACK);
                m_tg.setBackgroundColor(COULEURS_ACTIONS[i]);
            } else {
                m_tg.setForegroundColor(COULEURS_ACTIONS[i]);
                m_tg.setBackgroundColor(TextColor.ANSI.BLACK);
            }
            m_tg.putString(x, y + i, LIBELLES_ACTIONS[i]);
        }

        m_tg.setForegroundColor(TextColor.ANSI.RED);
        m_tg.setBackgroundColor(TextColor.ANSI.BLACK);
        m_tg.putString(x + 24, y + 0, "Sang: " + inscryption.getJoueur().getSang());
        m_tg.putString(x + 24, y + 1, "Os:   " + inscryption.getJoueur().getOs());
        m_tg.putString(x, y + Action.MAX_ACTION + 1, " ");

        m_tg.setForegroundColor(TextColor.ANSI.WHITE);
        m_tg.setBackgroundColor(TextColor.ANSI.BLACK);
    }

    private void affichageMain(ArrayList<Carte> main, int x, int y) {
        if (main.isEmpty()) {
            m_tg.setForegroundColor(TextColor.ANSI.BLACK_BRIGHT);
            m_tg.putString(x, y + CARTE_HEIGHT / 2, "(main vide)");
            m_tg.setForegroundColor(TextColor.ANSI.WHITE);
            return;
        }

        Etape etapeMain = m_gererTouches.getEtape();
        boolean enSelection = etapeMain == Etape.CHOIX_CARTE_MAIN
            || etapeMain == Etape.CHOIX_SACRIFICE_PIERRE
            || etapeMain == Etape.CHOIX_CIBLE_PIERRE;
        int slotMainSelected = (etapeMain == Etape.CHOIX_SACRIFICE_PIERRE || etapeMain == Etape.CHOIX_CIBLE_PIERRE)
            ? m_gererTouches.get_slotSelected()
            : m_gererTouches.get_slotMainSelected();

        int selectedIndex = enSelection && slotMainSelected >= 0 && slotMainSelected < main.size()
            ? slotMainSelected
            : 0;

        int xCourant = x;

        for (int i = 0; i < selectedIndex; i++) {
            Cadre cadre = new Cadre(xCourant, y, CARTE_HEIGHT, CARTE_WIDTH);
            cadre.drawTwoFromLeft(m_tg);
            xCourant += 2;
        }

        afficherCarteMain(main.get(selectedIndex), xCourant, y, true, enSelection);

        xCourant += CARTE_WIDTH;

        for (int i = selectedIndex + 1; i < main.size(); i++) {
            Cadre cadre = new Cadre(xCourant, y, CARTE_HEIGHT, CARTE_WIDTH);
            cadre.drawTwoFromRight(m_tg);
            xCourant += 2;
        }
    }

    private void afficherCarteMain(Carte carte, int x, int y, boolean selected, boolean enSelection) {
        Cadre cadre = new Cadre(x, y, CARTE_HEIGHT, CARTE_WIDTH);
        m_tg.setForegroundColor(TextColor.ANSI.WHITE);
        m_tg.setBackgroundColor(TextColor.ANSI.BLACK);
        cadre.draw(m_tg, !enSelection);
        m_tg.setForegroundColor(TextColor.ANSI.WHITE);
        cadre.drawSeparation(m_tg, 2);
        affichageCarte(carte, x, y);

        m_tg.setForegroundColor(TextColor.ANSI.RED);
        m_tg.putString(x + 1, y + CARTE_HEIGHT, "S:" + carte.getCoutSang() + " O:" + carte.getCoutOs());
        m_tg.setForegroundColor(TextColor.ANSI.WHITE);
        m_tg.setBackgroundColor(TextColor.ANSI.BLACK);
    }

    private Carte getCarteSelectionnee(Inscryption inscryption) {
        Etape etape = m_gererTouches.getEtape();
        ArrayList<Carte> main = inscryption.getJoueur().getMain();

        if (etape == Etape.CHOIX_CARTE_MAIN || etape == Etape.CHOIX_SACRIFICE_PIERRE || etape == Etape.CHOIX_CIBLE_PIERRE) {
            int index = (etape == Etape.CHOIX_SACRIFICE_PIERRE || etape == Etape.CHOIX_CIBLE_PIERRE)
                ? m_gererTouches.get_slotSelected()
                : m_gererTouches.get_slotMainSelected();
            if (index >= 0 && index < main.size()) {
                return main.get(index);
            }
        }

        int slot = m_gererTouches.get_slotSelected();
        Plateau plateau = inscryption.getPlateau();
        for (Character rangChar : Plateau.RANGS) {
            Rang rang = plateau.getRangByCharacter(rangChar);
            if (rang != null && slot >= 0 && slot < 4) {
                Carte carte = rang.getCarte(slot);
                if (carte != null) {
                    return carte;
                }
            }
        }
        return null;
    }

    private void afficherPanelDetails(Inscryption inscryption, int x, int y) {
        Cadre cadre = new Cadre(x, y, CARTE_HEIGHT * 3 + 4, 28);
        cadre.draw(m_tg, false);

        m_tg.setForegroundColor(TextColor.ANSI.CYAN_BRIGHT);
        m_tg.putString(x + 1, y + 1, "DETAILS DE LA CARTE");
        m_tg.setForegroundColor(TextColor.ANSI.WHITE);

        Carte carteSelectionnee = getCarteSelectionnee(inscryption);
        if (carteSelectionnee == null) {
            m_tg.setForegroundColor(TextColor.ANSI.BLACK_BRIGHT);
            m_tg.putString(x + 1, y + 3, "Aucune carte selectionnee");
            m_tg.setForegroundColor(TextColor.ANSI.WHITE);
            return;
        }

        String nom = carteSelectionnee.getNom();
        m_tg.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
        m_tg.putString(x + 1, y + 3, nom.length() > 24 ? nom.substring(0, 24) : nom);
        m_tg.setForegroundColor(TextColor.ANSI.WHITE);
        m_tg.putString(x + 1, y + 5, "PV : " + carteSelectionnee.getVie());
        m_tg.putString(x + 1, y + 6, "ATK: " + carteSelectionnee.getAttaque());
        m_tg.putString(x + 1, y + 7, "Sang: " + carteSelectionnee.getCoutSang() + "   Os: " + carteSelectionnee.getCoutOs());
        m_tg.putString(x + 1, y + 9, "Volante : " + (carteSelectionnee.estVolant() ? "oui" : "non"));

        carteSelectionnee.getNomPouvoir().ifPresentOrElse(
            pouvoir -> m_tg.putString(x + 1, y + 10, "Pouvoir: " + pouvoir),
            () -> m_tg.putString(x + 1, y + 10, "Pouvoir: aucun")
        );

        m_tg.setForegroundColor(TextColor.ANSI.BLACK_BRIGHT);
        m_tg.putString(x + 1, y + 12, "Selection courante : " + m_gererTouches.getEtape());
        m_tg.setForegroundColor(TextColor.ANSI.WHITE);
    }

    private void affichagePiochePierre(ArrayList<Carte> pioche, int x, int y) {
        if (pioche.isEmpty()) {
            m_tg.setForegroundColor(TextColor.ANSI.BLACK_BRIGHT);
            m_tg.putString(x, y + CARTE_HEIGHT / 2, "(pioche vide)");
            m_tg.setForegroundColor(TextColor.ANSI.WHITE);
            return;
        }
        int selected = m_gererTouches.get_slotSelected();
        for (int i = 0; i < pioche.size(); i++) {
            int xCarte = x + i * (CARTE_WIDTH + CARTE_PADDING);
            boolean sel = i == selected;
            Cadre cadre = new Cadre(xCarte, y, CARTE_HEIGHT, CARTE_WIDTH);
            if (sel) m_tg.setForegroundColor(TextColor.ANSI.MAGENTA);
            cadre.draw(m_tg, false);
            m_tg.setForegroundColor(TextColor.ANSI.WHITE);
            cadre.drawSeparation(m_tg, 2);
            affichageCarte(pioche.get(i), xCarte, y);
        }
    }

    private void affichagePioche(int nbrCartes, int x, int y) {
        Cadre cadre = new Cadre(x, y, CARTE_HEIGHT, CARTE_WIDTH);
        cadre.draw(m_tg, false);
        String label = nbrCartes + " carte" + (nbrCartes > 1 ? "s" : "");
        m_tg.setForegroundColor(TextColor.ANSI.CYAN);
        m_tg.putString(x + 1, y + CARTE_HEIGHT / 2, label);
        m_tg.setForegroundColor(TextColor.ANSI.WHITE);
    }

    public KeyStroke waitForKey() throws IOException {
        KeyStroke key;
        do {
            key = m_ecran.readInput();
        } while (key == null);
        return key;
    }

    public void fermer() {
        try {
            m_ecran.stopScreen();
            m_ecran.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Affichage[ecran=" + m_ecran + "]";
    }
}
