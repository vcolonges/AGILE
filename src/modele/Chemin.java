package modele;

import java.util.ArrayList;

/**
 * Classe determinant l’ensemble des tronçons d’une livraison à une autre dont la somme des longueurs est minimale.
 */
public class Chemin {

    private Livraison origine;
    private Livraison destination;
    private double longueur;
    ArrayList<Troncon> troncons;

    /**
     * Creation du chemin a partir d'un noeud d'origine, de destination et d'une longueur
     *
     * @param origine noeud d'origine
     * @param destination noeud de destination
     * @param longueur longueur du chemin
     */
    public Chemin(Livraison origine, Livraison destination, double longueur){
        this.origine = origine;
        this.destination = destination;
        this.longueur = longueur;
        this.troncons = new ArrayList<>();
    }

    /**
     * Creation d'un chemin
     */
    public Chemin() {
        this.origine = null;
        this.destination = null;
        this.longueur = 0;
        this.troncons = new ArrayList<>();
    }

    /**
     * Defini les troncons a emprunter dans l'ordre pour aller de l'origine a la destination du chemin
     *
     * @param troncons troncons a emprunter
     */
    public void setTroncons(ArrayList<Troncon> troncons) {
        this.troncons = troncons;
    }

    /**
     * Defini la longueur du chemin
     *
     * @param longueur du chemin
     */
    public void setLongueur(double longueur) {
        this.longueur = longueur;
    }

    /**
     * Renvoie l'origine du chemin
     *
     * @return origine
     */
    public Livraison getOrigine() {
        return origine;
    }

    /**
     * Renvoie la destination du chemin
     *
     * @return destination
     */
    public Livraison getDestination() {
        return destination;
    }

    /**
     * Renvoie la longueur du chemin
     *
     * @return longueur
     */
    public double getLongueur() {
        return longueur;
    }

    /**
     * Renvoie les troncons dont est compose le chemin
     *
     * @return troncons
     */
    public ArrayList<Troncon> getTroncons() {
        return troncons;
    }

}
