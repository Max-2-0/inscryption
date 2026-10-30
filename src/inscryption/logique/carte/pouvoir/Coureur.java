package inscryption.logique.carte.pouvoir;

import inscryption.logique.carte.Carte;
import inscryption.logique.Rang;

public class Coureur extends GenericPouvoir {

    public Coureur() {
        super("Coureur");
    }

    @Override
    public void onInfligerDegats(Carte self, Carte cible, int degats) {
        int slot = self.getSlot();
        Rang rang = self.getRang();

        boolean droiteLibre = slot + 1 < 4 && rang.isSlotEmpty(slot + 1);
        boolean gaucheLibre = slot - 1 >= 0 && rang.isSlotEmpty(slot - 1);

        if (droiteLibre) {
            self.updateSlot(slot + 1);
        } else if (gaucheLibre) {
            self.updateSlot(slot - 1);
        }
    }

    @Override
    public String toString() { return "Pouvoir[Coureur]"; }
}
