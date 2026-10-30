package inscryption.logique.carte;

import inscryption.logique.Rang;
import inscryption.logique.carte.pouvoir.*;
import inscryption.logique.Joueur;
import java.util.Optional;

public class Animal extends Carte {
    private int m_attaque;
    private int m_coutSang;
    private int m_coutOs;
    private boolean m_volant;
    private GenericPouvoir m_pouvoir;

    public Animal(Rang rang, int slot, String nom, int attaque, int vie, int sang, int os, boolean volant, GenericPouvoir pouvoir) {
        super();
        this.m_rang = rang;
        this.m_slot = slot;
        this.m_nom = nom;
        this.m_attaque = attaque;
        this.m_vie = vie;
        this.m_coutSang = sang;
        this.m_coutOs = os;
        this.m_volant = volant;
        this.m_pouvoir = pouvoir;
    }

    public Optional<GenericPouvoir> getPouvoir() {
        return Optional.ofNullable(m_pouvoir);
    }

    public void setPouvoir(GenericPouvoir pouvoir) {
        this.m_pouvoir = pouvoir;
    }

    @Override
    public int getAttaque() { return m_attaque; }

    @Override
    public int getCoutSang() { return m_coutSang; }

    @Override
    public int getCoutOs() { return m_coutOs; }

    @Override
    public boolean peutEtrePlacee() { return true; }

    @Override
    public boolean estVolant() { return this.m_volant; }

    @Override
    public int modifierDegatsEntrants(int degats) {
        return getPouvoir().map(p -> p.modifierDegatsEntrants(degats)).orElse(degats);
    }

    @Override
    public void declencherEffetsAttaque(Carte cible, int degats) {
        getPouvoir().ifPresent(p -> p.onInfligerDegats(this, cible, degats));
    }

    @Override
    public Optional<String> getNomPouvoir() {
        return getPouvoir().map(GenericPouvoir::getNom);
    }

    @Override
    public void infligerDegats(int degats, Carte other) {
        if (other.getSlot() == this.getSlot()) {
            other.recevoirDegats(degats, this);
            declencherEffetsAttaque(other, degats);
        }
    }

    @Override
    public void recevoirDegats(int degats, Carte attaquant) {
        this.m_vie -= degats;
        if (attaquant != this) {
            getPouvoir().ifPresent(p -> p.onRecevoirDegats(this, attaquant, degats));
        }
        if (this.m_vie <= 0) {
            this.defausser();
        }
    }

    @Override
    public void sacrifier(Joueur player) {
        getPouvoir().ifPresent(p -> p.onSacrifier(this, player));
        if (!getPouvoir().isPresent()) {
            this.defausser();
        }
    }

    @Override
    public void activerEffetTour() {
        if (m_tourencours == 2) {
            getPouvoir().ifPresent(p -> p.onTourCarteDeux(this));
        }
    }

    public boolean possedePouvoir() {
        return m_pouvoir != null;
    }

    @Override
    public String toString() {
        String base = m_nom + " (PV:" + m_vie + " ATK:" + m_attaque + " S:" + m_coutSang + " O:" + m_coutOs;
        if (m_volant) base += " Volant";
        base += getNomPouvoir().map(n -> " [" + n + "]").orElse("");
        return base + ")";
    }
}
