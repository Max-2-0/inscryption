package inscryption.logique;

import inscryption.logique.carte.Carte;

public class Plateau {
    private Rang m_rangE;
    private Rang m_rangA;
    private Rang m_rangB;

    static public final Character[] RANGS = {'E', 'A', 'B'};

    public Plateau() {
        m_rangE = new Rang("E");
        m_rangA = new Rang("A");
        m_rangB = new Rang("B");
    }

    public boolean ajouterCarte(Character rang, int slot, Carte carte) {
        return getRangByCharacter(rang).ajouterCarte(slot, carte);
    }

    public Rang getRangByCharacter(Character rang) {
        switch (rang) {
            case 'E': return m_rangE;
            case 'A': return m_rangA;
            case 'B': return m_rangB;
            default:  return m_rangB;
        }
    }

    public int resolveAttaques(Character rangAttaquant, Character rangDefenseur) {
        Rang rattaquant = getRangByCharacter(rangAttaquant);
        Rang rdefenseur = getRangByCharacter(rangDefenseur);
        int score = 0;

        for (int i = 0; i < 4; i++) {
            Carte c = rattaquant.getCarte(i);
            if (c == null || c.getAttaque() <= 0) continue;

            Carte cible = rdefenseur.getCarte(i);

            if (c.estVolant() || cible == null) {
                score += c.getAttaque();
                c.declencherEffetsAttaque(null, c.getAttaque());
            } else {
                int degats = cible.modifierDegatsEntrants(c.getAttaque());
                if (degats > 0) {
                    cible.recevoirDegats(degats, c);
                    c.declencherEffetsAttaque(cible, degats);
                }
            }
        }
        return score;
    }

    public void majTourCartes() {
        for (Character rangChar : new Character[]{'A', 'B'}) {
            Rang rang = getRangByCharacter(rangChar);
            for (int i = 0; i < 4; i++) {
                Carte c = rang.getCarte(i);
                if (c != null) {
                    c.updateTourEnCours();
                    c.activerEffetTour();
                }
            }
        }
    }

    @Override
    public String toString() {
        return "Plateau[E=" + m_rangE + " A=" + m_rangA + " B=" + m_rangB + "]";
    }
}
