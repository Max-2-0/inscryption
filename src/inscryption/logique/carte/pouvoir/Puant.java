package inscryption.logique.carte.pouvoir;

public class Puant extends GenericPouvoir {

    public Puant() {
        super("Puant");
    }

    @Override
    public int modifierDegatsEntrants(int degats) {
        return Math.max(0, degats - 1);
    }

    @Override
    public String toString() { return "Pouvoir[Puant]"; }
}
