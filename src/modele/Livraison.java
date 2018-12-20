package modele;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;

/**
 * Classe definissant les proprietes du point de livraison
 */
public class Livraison {

    private Noeud noeud;
    private int duree;
    private HashSet<Chemin> chemins;
    private Date heureArrivee;

    /**
     * Creation de l'enttite livraison grace au noeud auquel elle est associee et la duree necessaire sur place
     *
     * @param noeud noeud associe a la livraison
     * @param duree duree sur place
     */
    public Livraison(Noeud noeud, int duree){
        this.noeud = noeud;
        this.duree = duree;
        chemins = new HashSet<>();
    }

    /**
     * Creation de l'entite livraison a partir d'une autre entite de livraison
     *
     * @param l lviraison a copiee
     */
    public Livraison(Livraison l){
        this.noeud = l.getNoeud();
        this.duree = l.getDuree();
        this.chemins = l.getChemins();
        this.heureArrivee = (Date)l.heureArrivee.clone();
    }

    /**
     * Renvoie le noeud associe au point de livraison
     *
     * @return noeud
     */
    public Noeud getNoeud() {
        return noeud;
    }

    /**
     * Defini le noeud associe a la livraison
     *
     * @param noeud noeud associe a la livraison
     */
    public void setNoeud(Noeud noeud) {
        this.noeud = noeud;
    }

    /**
     * Renvoie la duree necessaire sur place
     *
     * @return duree
     */
    public int getDuree() {
        return duree;
    }

    /**
     * Deifni la duree necessaire su rplace pour realiser la livraison
     *
     * @param duree duree necessaire
     */
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

    /**
     * Renvoie les chemins adjents a la livraison
     *
     * @return chemins
     */
    public HashSet<Chemin> getChemins() {
        return chemins;
    }

    /**
     * Ajoute un chemin a la liste des chemins adjacents
     *
     * @param c chemin adjacent
     * @return boolean true si tout s'est bien passe, false sinon
     */
    public boolean addChemin(Chemin c){
        return this.chemins.add(c);
    }

    /**
     * Renvoie le chemin allant de la livraison courante a celle passee en parametre
     *
     * @param destination livraison a atteindre
     * @return chemin
     */
    public Chemin getCheminVers(Livraison destination){
        for (Chemin item: chemins) {
            if(item.getDestination() == destination){
                return item;
            }
        }
        return  null;
    }

    /**
     * Renvoie l'heure prevue d'arrivee au point de livraison
     *
     * @return heureArrivee
     */
    public Date getHeureArrivee() {
        return heureArrivee;
    }

    /**
     * Defini l'heure prevue d'arrivee au point de livraison
     *
     * @param heureArrivee heure prevue
     */
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
