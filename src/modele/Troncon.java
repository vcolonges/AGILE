package modele;

import java.util.Objects;

public class Troncon {

    private Noeud origine;
    private Noeud destination;
    private double longueur;
    private String nomRue;

    public Troncon(Noeud origine, Noeud destination, double longueur, String nomRue){
        this.origine = origine;
        this.destination = destination;
        this.longueur = longueur;
        this.nomRue = nomRue;
    }

    public Noeud getOrigine() {
        return origine;
    }

    public Noeud getDestination() {
        return destination;
    }

    public double getLongueur() {
        return longueur;
    }

    public String getNomRue() {
        return nomRue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Troncon troncon = (Troncon) o;
        return Objects.equals(origine, troncon.origine) &&
                Objects.equals(destination, troncon.destination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(origine, destination);
    }

    @Override
    public String toString() {
        return "Troncon{" +
                "origine=" + origine.getId() +
                ", destination=" + destination.getId() +
                ", longueur=" + longueur +
                ", nomRue='" + nomRue + '\'' +
                "}\r\n";
    }

    /**
     *
     * @param vitesse Vitesse en m/s
     * @return duree en ms
     */
    public long getDuree(double vitesse)
    {
        return (long)((longueur/vitesse)*1000);
    }
}
