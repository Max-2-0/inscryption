package inscryption.logique.carte.pouvoir;

import inscryption.logique.carte.Carte;

public class PiquesPointues extends GenericPouvoir {

    public PiquesPointues() {
        super("Piques Pointues");
    }

    @Override
    public void onRecevoirDegats(Carte self, Carte attaquant, int degats) {
        attaquant.recevoirDegats(1, self);
    }

    @Override
    public String toString() { return "Pouvoir[Piques Pointues]"; }
}
