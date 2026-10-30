package inscryption.logique.carte.pouvoir;

import inscryption.logique.carte.Carte;
import inscryption.logique.Joueur;

public abstract class GenericPouvoir {
    private String m_nom;

    public GenericPouvoir(String nom) {
        this.m_nom = nom;
    }

    public String getNom() { return m_nom; }

    public void onRecevoirDegats(Carte self, Carte attaquant, int degats) {}

    public void onInfligerDegats(Carte self, Carte cible, int degats) {}

    public int modifierDegatsEntrants(int degats) { return degats; }

    public void onSacrifier(Carte self, Joueur player) { self.defausser(); }

    public void onTourCarteDeux(Carte self) {}

    @Override
    public String toString() { return "Pouvoir[" + m_nom + "]"; }
}
