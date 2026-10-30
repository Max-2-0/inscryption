package inscryption.logique.carte.pouvoir;

import inscryption.logique.carte.Carte;
import inscryption.logique.Joueur;

public class NombreusesVies extends GenericPouvoir {

    public NombreusesVies() {
        super("Nombreuses Vies");
    }

    @Override
    public void onSacrifier(Carte self, Joueur player) {}

    @Override
    public String toString() { return "Pouvoir[Nombreuses Vies]"; }
}
