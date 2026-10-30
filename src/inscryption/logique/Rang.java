package inscryption.logique;

import inscryption.logique.carte.Carte;

public class Rang {
    private Carte[] m_slots;
    private String m_nom;

    public Rang(String nom) {
        m_slots = new Carte[4];
        m_nom = nom;
    }

    public Rang() {
        this("?");
    }

    public boolean ajouterCarte(int numeroSlot, Carte carte) {
        if (numeroSlot < 0 || numeroSlot >= 4) return false;
        if (m_slots[numeroSlot] != null) return false;
        m_slots[numeroSlot] = carte;
        return true;
    }

    public boolean retirerCarte(int numeroSlot) {
        if (numeroSlot < 0 || numeroSlot >= 4) return false;
        m_slots[numeroSlot] = null;
        return true;
    }

    public Carte getCarte(int numeroSlot) {
        if (numeroSlot < 0 || numeroSlot >= 4) return null;
        return m_slots[numeroSlot];
    }

    public boolean isSlotEmpty(int numeroSlot) {
        if (numeroSlot < 0 || numeroSlot >= 4) return false;
        return m_slots[numeroSlot] == null;
    }

    public void deplacerCarte(int source, int dest) {
        if (dest < 0 || dest >= 4 || source < 0 || source >= 4) return;
        if (isSlotEmpty(dest)) {
            m_slots[dest] = m_slots[source];
            m_slots[source] = null;
        }
    }

    public String getNom() { return m_nom; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Rang[" + m_nom + ": ");
        for (int i = 0; i < 4; i++) {
            sb.append(i).append("=");
            sb.append(m_slots[i] != null ? m_slots[i].getNom() : "_");
            if (i < 3) sb.append(", ");
        }
        return sb.append("]").toString();
    }
}
