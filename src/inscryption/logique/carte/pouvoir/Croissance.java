package inscryption.logique.carte.pouvoir;

import inscryption.logique.Rang;
import inscryption.logique.carte.Animal;
import inscryption.logique.carte.Carte;
import inscryption.logique.Joueur;

public class Croissance extends GenericPouvoir {

    public Croissance() {
        super("Croissance");
    }

    @Override
    public void onTourCarteDeux(Carte self) {
        int slot = self.getSlot();
        Rang rang = self.getRang();
        Joueur proprietaire = self.getProprietaire();
        self.defausser();

        Animal loup = new Animal(rang, slot, "Loup", 3, 2, 2, 0, false, null);
        loup.setProprietaire(proprietaire);
        rang.ajouterCarte(slot, loup);
    }

    @Override
    public String toString() { return "Pouvoir[Croissance]"; }
}
