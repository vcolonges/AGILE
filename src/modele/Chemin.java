package modele;

import java.util.ArrayList;

public class Chemin {

    private Livraison origine;
    private Livraison destination;
    private double longueur;
    ArrayList<Troncon> troncons;

    public Chemin(Livraison origine, Livraison destination, double longueur){
        this.origine = origine;
        this.destination = destination;
        this.longueur = longueur;
        this.troncons = new ArrayList<>();
    }

    public Chemin() {
        this.origine = null;
        this.destination = null;
        this.longueur = 0;
        this.troncons = new ArrayList<>();
    }

    public void setTroncons(ArrayList<Troncon> troncons) {
        this.troncons = troncons;
    }

    public Livraison getOrigine() {
        return origine;
    }

    public Livraison getDestination() {
        return destination;
    }

    public double getLongueur() {
        return longueur;
    }

    public ArrayList<Troncon> getTroncons() {
        return troncons;
    }

    public void resetChemin(){
        origine.resetChemin();
        destination.resetChemin();
        troncons.clear();
    }
}
