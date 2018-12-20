package modele;

import java.util.Objects;

/**
 * Classe definissant une rue grace a son origine, sa destination, sa longueur et le nom de sa rue
 */
public class Troncon {

    private Noeud origine;
    private Noeud destination;
    private double longueur;
    private String nomRue;

    /**
     * Creation d'un troncon avec toutes les infromations necessaires
     *
     * @param origine noeud d'origine
     * @param destination noeud de destination
     * @param longueur longueur de la rue
     * @param nomRue nom de la rue
     */
    public Troncon(Noeud origine, Noeud destination, double longueur, String nomRue){
        this.origine = origine;
        this.destination = destination;
        this.longueur = longueur;
        this.nomRue = nomRue;
    }

    /**
     * Renvoie l'origine du troncon
     *
     * @return origine
     */
    public Noeud getOrigine() {
        return origine;
    }

    /**
     * Renvoie la destination du troncon
     *
     * @return destination
     */
    public Noeud getDestination() {
        return destination;
    }

    /**
     * Renvoie la longueur du troncon
     *
     * @return longueur
     */
    public double getLongueur() {
        return longueur;
    }
    /**
     * Renvoie le nom de la rue du troncon
     *
     * @return nomRue
     */
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
