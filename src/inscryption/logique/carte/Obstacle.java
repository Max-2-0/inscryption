package inscryption.logique.carte;

import inscryption.logique.Rang;

public class Obstacle extends Carte {

    public Obstacle(Rang rang, int slot, int vie, String nom) {
        super();
        this.m_rang = rang;
        this.m_slot = slot;
        this.m_nom = nom;
        this.m_vie = vie;
    }

    @Override
    public void infligerDegats(int degats, Carte other) {}

    @Override
    public void recevoirDegats(int degats, Carte attaquant) {
        this.m_vie -= degats;
        if (this.m_vie <= 0) {
            this.defausser();
        }
    }

    @Override
    public int getAttaque() { return 0; }

    @Override
    public boolean estVolant() { return false; }

    @Override
    public String toString() {
        return m_nom + " (obstacle, PV:" + m_vie + ")";
    }
}
