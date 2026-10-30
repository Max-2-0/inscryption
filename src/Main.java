import inscryption.Inscryption;

public class Main {

    public static void main(String[] args) throws Exception {
        Inscryption jeu = new Inscryption();
        jeu.initialiserPartie();

        while (jeu.isJeuEnCours()) {
            jeu.mainLoop();
        }

        jeu.mainLoopFin();
        jeu.fermer();
    }
}
