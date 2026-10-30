package inscryption.affichage;

import java.io.IOException;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import inscryption.Inscryption;

public class GererTouches {
    private Etape m_etape;
    private int m_slotSelected;
    private int m_slotMainSelected;
    private int m_tailleMain;
    private Action m_actionSelected;
    private final Inscryption m_inscryption;

    public GererTouches(Inscryption inscryption) {
        this.m_inscryption = inscryption;
        this.m_etape = Etape.CHOIX_ACTION;
        this.m_slotSelected = 0;
        this.m_slotMainSelected = 0;
        this.m_tailleMain = 0;
        this.m_actionSelected = Action.FIN;
    }

    public int get_slotSelected() { return this.m_slotSelected; }
    public int get_slotMainSelected() { return this.m_slotMainSelected; }
    public Action get_actionSelected() { return this.m_actionSelected; }
    public Etape getEtape() { return this.m_etape; }
    public void setEtape(Etape etape) { this.m_etape = etape; }

    public void acheminer(KeyStroke key) {
        m_tailleMain = m_inscryption.getJoueur().getTailleMain();
        KeyType keyType = key.getKeyType();

        if (keyType == KeyType.Escape) {
            gererEscape();
            return;
        }

        switch (keyType) {
            case KeyType.ArrowUp:    gererFlecheHaut(); break;
            case KeyType.ArrowDown:  gererFlecheBas(); break;
            case KeyType.ArrowLeft:  gererFlecheGauche(); break;
            case KeyType.ArrowRight: gererFlecheDroite(); break;
            case KeyType.Enter:      gererEntrer(); break;
            default: break;
        }
    }

    public void gererErreur(IOException e) {}

    private void gererEscape() {
        switch (m_etape) {
            case Etape.CHOIX_CARTE_MAIN:
            case Etape.CHOIX_SACRIFICE:
            case Etape.CHOIX_SACRIFICE_LIBRE:
            case Etape.CHOIX_SLOT:
                m_etape = Etape.CHOIX_ACTION;
                m_inscryption.setIndexCarteEnCoursPlacement(-1);
                m_inscryption.setSangNecessaire(0);
                m_inscryption.getMessage(); // pas d'affichage ici
                break;
            default: break;
        }
    }

    private void gererFlecheHaut() {
        if (m_etape == Etape.CHOIX_ACTION) {
            int val = (m_actionSelected.getValue() - 1 + Action.MAX_ACTION) % Action.MAX_ACTION;
            m_actionSelected = Action.fromValue(val);
        }
    }

    private void gererFlecheBas() {
        if (m_etape == Etape.CHOIX_ACTION) {
            int val = (m_actionSelected.getValue() + 1) % Action.MAX_ACTION;
            m_actionSelected = Action.fromValue(val);
        }
    }

    private void gererFlecheGauche() {
        switch (m_etape) {
            case Etape.CHOIX_CARTE_MAIN:
                if (m_tailleMain > 0 && m_slotMainSelected > 0) m_slotMainSelected--;
                break;
            case Etape.CHOIX_SACRIFICE:
            case Etape.CHOIX_SACRIFICE_LIBRE:
            case Etape.CHOIX_SLOT:
                if (m_slotSelected > 0) m_slotSelected--;
                break;
            case Etape.CHOIX_CARTE_AJOUT:
                if (m_slotSelected > 0) m_slotSelected--;
                break;
            case Etape.CHOIX_SACRIFICE_PIERRE:
            case Etape.CHOIX_CIBLE_PIERRE:
                if (m_slotSelected > 0) m_slotSelected--;
                break;

            default: break;
        }
    }

    private void gererFlecheDroite() {
        switch (m_etape) {
            case Etape.CHOIX_CARTE_MAIN:
                if (m_tailleMain > 0 && m_slotMainSelected < m_tailleMain - 1) m_slotMainSelected++;
                break;
            case Etape.CHOIX_SACRIFICE:
            case Etape.CHOIX_SACRIFICE_LIBRE:
            case Etape.CHOIX_SLOT:
                if (m_slotSelected < 3) m_slotSelected++;
                break;
            case Etape.CHOIX_CARTE_AJOUT:
                if (m_slotSelected < m_inscryption.getCartesProposees().size() - 1) m_slotSelected++;
                break;
            case Etape.CHOIX_SACRIFICE_PIERRE:
            case Etape.CHOIX_CIBLE_PIERRE:
                if (m_slotSelected < m_inscryption.getJoueur().getTailleMain() - 1) m_slotSelected++;
                break;
            default: break;
        }
    }

    private void gererEntrer() {
        switch (m_etape) {
            case Etape.CHOIX_ACTION:          confirmerAction(); break;
            case Etape.CHOIX_CARTE_MAIN:      confirmerCarteMain(); break;
            case Etape.CHOIX_SACRIFICE:       confirmerSacrifice(); break;
            case Etape.CHOIX_SACRIFICE_LIBRE: confirmerSacrificeLibre(); break;
            case Etape.CHOIX_SLOT:            confirmerSlot(); break;
            case Etape.FIN_PARTIE:             confirmerFinPartie(); break;
            case Etape.CHOIX_CARTE_AJOUT:      confirmerCarteAjout(); break;
            case Etape.CHOIX_SACRIFICE_PIERRE: confirmerSacrificePierre(); break;
            case Etape.CHOIX_CIBLE_PIERRE:     confirmerCiblePierre(); break;
            case Etape.FIN_JEU:                m_inscryption.setJeuEnCours(false); break;
            default: break;
        }
    }

    private void confirmerAction() {
        switch (m_actionSelected) {
            case Action.FIN:
                m_inscryption.finDeTour();
                if (!m_inscryption.isPartieEnCours()) {
                    m_etape = Etape.FIN_PARTIE;
                } else if (m_inscryption.isPierrePending()) {
                    m_inscryption.setPierrePending(false);
                    m_inscryption.preparerPierreSacrifice();
                    m_slotSelected = 0;
                    m_etape = Etape.CHOIX_SACRIFICE_PIERRE;
                }
                break;
            case Action.PIOCHER:
                m_inscryption.piocher();
                break;
            case Action.PLACER:
                if (m_tailleMain == 0) return;
                m_slotMainSelected = 0;
                m_etape = Etape.CHOIX_CARTE_MAIN;
                break;
            case Action.SACRIFIER:
                m_slotSelected = 0;
                m_etape = Etape.CHOIX_SACRIFICE_LIBRE;
                m_inscryption.setMessage("Choisissez une carte sur le rang B à sacrifier.");
                break;
        }
    }

    private void confirmerCarteMain() {
        boolean ok = m_inscryption.demarrerPlacement(m_slotMainSelected);
        if (!ok) return;
        m_slotSelected = 0;
        boolean besoinSacrifice = m_inscryption.getSangNecessaire() > 0
            && m_inscryption.getJoueur().getSang() < m_inscryption.getSangNecessaire();
        m_etape = besoinSacrifice ? Etape.CHOIX_SACRIFICE : Etape.CHOIX_SLOT;
    }

    private void confirmerSacrifice() {
        m_inscryption.sacrifierCartePour(m_slotSelected);
        if (m_inscryption.getJoueur().getSang() >= m_inscryption.getSangNecessaire()) {
            m_etape = Etape.CHOIX_SLOT;
            m_slotSelected = 0;
        }
    }

    private void confirmerSacrificeLibre() {
        m_inscryption.sacrifierCarteLibre(m_slotSelected);
        m_slotSelected = 0;
        m_etape = Etape.CHOIX_ACTION;
    }

    private void confirmerSlot() {
        boolean place = m_inscryption.placerCarte(m_inscryption.getIndexCarteEnCoursPlacement(), m_slotSelected);
        if (place) {
            m_etape = Etape.CHOIX_ACTION;
            int tailleMain = m_inscryption.getJoueur().getTailleMain();
            if (m_slotMainSelected >= tailleMain) {
                m_slotMainSelected = Math.max(0, tailleMain - 1);
            }
        }
    }

    private void confirmerFinPartie() {
        if (!m_inscryption.verifierFinJeu()) {
            m_inscryption.passerPartieSuivante();
            boolean choixCarte = !m_inscryption.getCartesProposees().isEmpty();
            m_etape = choixCarte ? Etape.CHOIX_CARTE_AJOUT : Etape.CHOIX_ACTION;
            m_slotSelected = 0;
        } else {
            m_etape = Etape.FIN_JEU;
        }
    }

    private void confirmerCarteAjout() {
        m_inscryption.ajouterCarteChoisie(m_slotSelected);
        m_slotSelected = 0;
        m_etape = Etape.CHOIX_ACTION;
    }

    private void confirmerSacrificePierre() {
        m_inscryption.sacrifierPourPierre(m_slotSelected);
        m_slotSelected = 0;
        m_etape = m_inscryption.getJoueur().getPouvoirPierre().isPresent()
            ? Etape.CHOIX_CIBLE_PIERRE
            : Etape.CHOIX_ACTION;
    }

    private void confirmerCiblePierre() {
        m_inscryption.appliquerPouvoirPierre(m_slotSelected);
        m_slotSelected = 0;
        m_etape = Etape.CHOIX_ACTION;
    }

    @Override
    public String toString() {
        return "GererTouches[etape=" + m_etape + " action=" + m_actionSelected + "]";
    }
}
