package inscryption.affichage;

public enum Action {

    FIN(0),
    PIOCHER(1),
    PLACER(2),
    SACRIFIER(3);

    private final int m_value;
    public static final int MAX_ACTION = 4;

    Action(int value) {
        this.m_value = value;
    }

    public int getValue() { return m_value; }

    public static Action fromValue(int value) {
        for (Action action : Action.values()) {
            if (action.m_value == value) return action;
        }
        throw new IllegalArgumentException("Valeur inconnue : " + value);
    }

    @Override
    public String toString() { return name() + "(" + m_value + ")"; }
}
