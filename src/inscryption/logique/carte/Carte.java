package inscryption.logique.carte;

import inscryption.logique.Rang;
import inscryption.logique.Joueur;
import inscryption.logique.carte.pouvoir.GenericPouvoir;
import java.util.Optional;

public abstract class Carte {
    protected boolean m_active;
    protected String m_nom;
    protected int m_vie;
    protected int m_tourencours;
    protected Rang m_rang;
    protected int m_slot;
    protected Joueur m_proprietaire;

    protected Carte() {
        m_active = true;
        m_tourencours = 0;
        m_rang = null;
        m_proprietaire = null;
    }

    public void placer(Rang rang, int slot) {
        this.m_rang = rang;
        this.m_slot = slot;
    }

    public void setProprietaire(Joueur joueur) { this.m_proprietaire = joueur; }
    public Joueur getProprietaire() { return this.m_proprietaire; }

    public boolean estActive() { return m_active; }

    public void defausser() {
        m_active = false;
        if (m_rang != null) m_rang.retirerCarte(m_slot);
        if (m_proprietaire != null) m_proprietaire.updateOs(1);
    }

    public boolean updateSlot(int newSlot) {
        if (newSlot < 0 || newSlot >= 4) return false;
        m_rang.deplacerCarte(this.m_slot, newSlot);
        this.m_slot = newSlot;
        return true;
    }

    public int getCoutSang() { return 0; }
    public int getCoutOs() { return 0; }
    public boolean peutEtrePlacee() { return false; }
    public int modifierDegatsEntrants(int degats) { return degats; }
    public void declencherEffetsAttaque(Carte cible, int degats) {}
    public Optional<String> getNomPouvoir() { return Optional.empty(); }
    public void sacrifier(Joueur player) { this.defausser(); }
    public void activerEffetTour() {}
    public Optional<GenericPouvoir> getPouvoir() { return Optional.empty(); }
    public void setPouvoir(GenericPouvoir pouvoir) {}

    public int getSlot() { return m_slot; }
    public Rang getRang() { return m_rang; }
    public String getNom() { return m_nom; }
    public int getVie() { return m_vie; }
    public int getTourEnCours() { return m_tourencours; }
    public void updateTourEnCours() { m_tourencours++; }
    public String getNomRang() { return m_rang.getNom(); }

    public abstract void infligerDegats(int degats, Carte other);
    public abstract void recevoirDegats(int degats, Carte attaquant);
    public abstract int getAttaque();
    public abstract boolean estVolant();

    @Override
    public String toString() {
        return m_nom + " (PV:" + m_vie + " ATK:" + getAttaque() + ")";
    }
}
