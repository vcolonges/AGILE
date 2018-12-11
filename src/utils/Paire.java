package utils;

public class Paire<A, B> {

    private A premier;
    private B second;

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

    public A getPremier() {
        return premier;
    }

    public void setPremier(A premier) {
        this.premier = premier;
    }

    public B getSecond() {
        return second;
    }

    public void setSecond(B second) {
        this.second = second;
    }
}
