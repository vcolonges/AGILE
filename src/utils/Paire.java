package utils;

/**
 * Classe generique pour la gestion des Paires
 * @param <A> Type du premier element de la paire
 * @param <B> Type du second element de la paire
 */
public class Paire<A, B> {

    /**
     * Premier element de la paire
     */
    private A premier;
    /**
     * Second element de la paire
     */
    private B second;

    /**
     *
     * @param premier Premier element de la paire
     * @param second Second element de la paire
     */
    public Paire(A premier, B second) {
        super();
        this.premier = premier;
        this.second = second;
    }

    public int hashCode() {
        int hashFirst = premier != null ? premier.hashCode() : 0;
        int hashSecond = second != null ? second.hashCode() : 0;

        return (hashFirst + hashSecond) * hashSecond + hashFirst;
    }

    public boolean equals(Object other) {
        if (other instanceof Paire) {
            Paire otherPair = (Paire) other;
            return ((this.premier == otherPair.premier
                    || (this.premier != null && otherPair.premier != null
                    && this.premier.equals(otherPair.premier)))
                    && (this.second == otherPair.second
                    || (this.second != null && otherPair.second != null
                    && this.second.equals(otherPair.second))));
        }

        return false;
    }

    public String toString() {
        return "(" + premier + ", " + second + ")";
    }

    /**
     * @return Premier element de la paire
     */
    public A getPremier() {
        return premier;
    }

    /**
     * @return Second element de la paire
     */
    public B getSecond() {
        return second;
    }
}
