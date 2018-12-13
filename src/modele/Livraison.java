package modele;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;

public class Livraison {

    private Noeud noeud;
    private int duree; //en secondes
    private HashSet<Chemin> chemins;
    private Date heureArrivee;

    public Livraison(Noeud noeud, int duree){
        this.noeud = noeud;
        this.duree = duree;
        chemins = new HashSet<>();
    }

    public Noeud getNoeud() {
        return noeud;
    }

    public void setNoeud(Noeud noeud) {
        this.noeud = noeud;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Livraison livraison = (Livraison) o;
        return duree == livraison.duree &&
                Objects.equals(noeud, livraison.noeud);
    }

    @Override
    public int hashCode() {
        return Objects.hash(noeud, duree);
    }

    public HashSet<Chemin> getChemins() {
        return chemins;
    }

    public void resetChemin(){
        chemins.clear();
    }

    public Chemin getCheminVers(Livraison destination){
        for (Chemin item: chemins) {
            if(item.getDestination() == destination){
                return item;
            }
        }
        return  null;
    }

    public Date getHeureArrivee() {
        return heureArrivee;
    }

    public void setHeureArrivee(Date heureArrivee) {
        this.heureArrivee = heureArrivee;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Livraison cloneLivraison = new Livraison(this.noeud,this.duree);
        cloneLivraison.heureArrivee = (Date)this.heureArrivee.clone();
        return cloneLivraison;
    }
}
