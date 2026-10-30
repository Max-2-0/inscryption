package inscryption.logique.carte.pouvoir;

import inscryption.logique.carte.Carte;

public class ContactMortel extends GenericPouvoir {

    public ContactMortel() {
        super("Contact Mortel");
    }

    @Override
    public void onInfligerDegats(Carte self, Carte cible, int degats) {
        if (cible != null && cible.peutEtrePlacee()) {
            cible.defausser();
        }
    }

    @Override
    public String toString() { return "Pouvoir[Contact Mortel]"; }
}
