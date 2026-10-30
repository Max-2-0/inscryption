package inscryption.logique;

import inscryption.logique.carte.Carte;
import inscryption.logique.carte.Animal;
import inscryption.logique.carte.pouvoir.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

public class Joueur {
    private ArrayList<Carte> m_main;
    private ArrayList<Carte> m_pioche;
    private int m_sang;
    private int m_os;
    private Optional<GenericPouvoir> m_pouvoirPierre;

    public Joueur() {
        m_sang = 0;
        m_os = 0;
        m_main = new ArrayList<>();
        m_pioche = new ArrayList<>();
        m_pouvoirPierre = Optional.empty();
        initialiserPioche();
    }

    private void initialiserPioche() {
        for (int i = 0; i < 10; i++) m_pioche.add(creerEcureuil());
        m_pioche.add(creerHermine());
        m_pioche.add(creerHermine());
        m_pioche.add(creerHermine());
        m_pioche.add(creerChat());
        m_pioche.add(creerLouveteau());
        Collections.shuffle(m_pioche);
    }

    public boolean piocher() {
        if (m_pioche.isEmpty()) return false;
        m_main.add(m_pioche.remove(m_pioche.size() - 1));
        return true;
    }

    public void ajouterCartePioche(Carte carte) { m_pioche.add(0, carte); }
    public int getTaillePioche() { return m_pioche.size(); }
    public int getTailleMain() { return m_main.size(); }
    public ArrayList<Carte> getMain() { return m_main; }
    public ArrayList<Carte> getPioche() { return m_pioche; }

    public void updateSang(int sang) { m_sang += sang; }
    public int getSang() { return m_sang; }

    public boolean consommerSang(int sang) {
        if (m_sang < sang) return false;
        m_sang -= sang;
        return true;
    }

    public void updateOs(int os) { m_os += os; }
    public int getOs() { return m_os; }

    public boolean consommerOs(int os) {
        if (m_os < os) return false;
        m_os -= os;
        return true;
    }

    public void setPouvoirPierre(GenericPouvoir pouvoir) { m_pouvoirPierre = Optional.ofNullable(pouvoir); }
    public Optional<GenericPouvoir> getPouvoirPierre() { return m_pouvoirPierre; }

    public static Animal creerEcureuil() {
        return new Animal(null, -1, "Ecureuil", 0, 1, 0, 0, false, null);
    }

    public static Animal creerHermine() {
        return new Animal(null, -1, "Hermine", 1, 3, 1, 0, false, null);
    }

    public static Animal creerChat() {
        return new Animal(null, -1, "Chat", 0, 1, 1, 0, false, new NombreusesVies());
    }

    public static Animal creerLouveteau() {
        return new Animal(null, -1, "Louveteau", 1, 1, 1, 0, false, new Croissance());
    }

    public static Animal creerLoup() {
        return new Animal(null, -1, "Loup", 3, 2, 2, 0, false, null);
    }

    public static Animal creerGrizzly() {
        return new Animal(null, -1, "Grizzly", 4, 6, 3, 0, false, null);
    }

    public static Animal creerCoyote() {
        return new Animal(null, -1, "Coyote", 2, 1, 0, 4, false, null);
    }

    public static Animal creerMoineau() {
        return new Animal(null, -1, "Moineau", 1, 2, 1, 0, true, null);
    }

    public static Animal creerCorbeau() {
        return new Animal(null, -1, "Corbeau", 2, 3, 2, 0, true, null);
    }

    public static Animal creerPunaise() {
        return new Animal(null, -1, "Punaise", 1, 2, 0, 2, false, new Puant());
    }

    public static Animal creerElan() {
        return new Animal(null, -1, "Elan", 2, 4, 2, 0, false, new Coureur());
    }

    public static Animal creerVipere() {
        return new Animal(null, -1, "Vipère", 1, 1, 2, 0, false, new ContactMortel());
    }

    public static Animal creerPorcEpic() {
        return new Animal(null, -1, "Porc-épic", 1, 2, 1, 0, false, new PiquesPointues());
    }

    @Override
    public String toString() {
        return "Joueur(main=" + m_main.size() + ", pioche=" + m_pioche.size()
            + ", sang=" + m_sang + ", os=" + m_os + ")";
    }
}
