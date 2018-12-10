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
    public void setOrigine(Noeud origine) {
        this.origine = origine;
    }
    public Noeud getDestination() {
        return destination;
    }
    public void setDestination(Noeud destination) {
        this.destination = destination;
    }
    public double getLongueur() {
        return longueur;
    }
    public void setLongueur(double longueur) {
        this.longueur = longueur;
    }
    public String getNomRue() {
        return nomRue;
    }
    public void setNomRue(String nomRue) {
        this.nomRue = nomRue;
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
     * @param vitesse
     * @return duree en ms
     */
    public long getDuree(double vitesse)
    {
        return (long)((longueur/vitesse)*1000);
    }
}
