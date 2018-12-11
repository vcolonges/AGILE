package modele;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

public class Plan {

    private HashMap<Long, Noeud> noeuds;
    private HashSet<Troncon> troncons;
    private HashMap<Long, Livraison> livraisons;
    private Livraison entrepot;
    private Date heureDepart;
    private ArrayList<Tournee> tournees;
    private int nbLivreurs;

    public Plan(){
        this.noeuds = new HashMap<>();
        this.troncons = new HashSet<>();
        this.livraisons = new HashMap<>();
        this.tournees = new ArrayList<>();
        this.nbLivreurs = 1;
    }

    public HashMap<Long, Noeud> getNoeuds(){
        return this.noeuds;
    }

    public boolean addNoeud(Noeud noeud){
        if(this.noeuds.containsKey(noeud.getId())){
            return false;
        }
        this.noeuds.put(noeud.getId(), noeud);
        return true;
    }

    public HashSet<Troncon> getTroncons() {
        return troncons;
    }

    public boolean addTroncon(Troncon troncon) {
        return this.troncons.add(troncon);
    }

    public double getMaxLat(){
        double max = -180;
        for(Noeud n : noeuds.values())
        {
            if(max<n.getLatitude()) max = n.getLatitude();
        }
        return max;
    }

    public double getMaxLong(){
        double max = -180;
        for(Noeud n : noeuds.values())
        {
            if(max<n.getLongitude()) max = n.getLongitude();
        }
        return max;
    }

    public double getMinLat(){
        double min = 180;
        for(Noeud n : noeuds.values())
        {
            if(min>n.getLatitude()) min = n.getLatitude();
        }
        return min;
    }

    public double getMinLong(){
        double min = 180;
        for(Noeud n : noeuds.values())
        {
            if(min>n.getLongitude()) min = n.getLongitude();
        }
        return min;
    }
    public HashMap<Long, Livraison> getLivraisons() {
        return livraisons;
    }

    public boolean addLivraison(Livraison livraison) {
        if(this.livraisons.containsKey(livraison.getNoeud().getId())){
            return false;
        }
        this.livraisons.put(livraison.getNoeud().getId(), livraison);
        return true;
    }

    public Livraison getEntrepot() {
        return entrepot;
    }

    public void setEntrepot(Livraison entrepot) {
        this.entrepot = entrepot;
    }

    public Date getHeureDepart() {
        return heureDepart;
    }

    public void setHeureDepart(Date heureDepart) {
        this.heureDepart = heureDepart;
    }

    public ArrayList<Tournee> getTournees() {
        return tournees;
    }

    public void setTournees(ArrayList<Tournee> tournees) {
        this.tournees = tournees;
    }

    public void addTournee(Tournee tournee){
        tournees.add(tournee);
    }

    public void removeTournee(Tournee tournee){
        tournees.remove(tournee);
    }

    /**
     * Renvoie le nombre de livreur max que peut gerer le plan
     *
     * @return nblivreurs
     */
    public int getNbLivreurs() {
        return nbLivreurs;
    }

    public void resetLivraisons(){
        for(Livraison livraison : livraisons.values()){
            for (Chemin chemin : livraison.getChemins()) {
                chemin.resetChemin();
            }
            livraison.resetChemin();
        }
        livraisons.clear();
    }

    public void resetTournees(){
        for(Tournee tournee : tournees) {
            for (Chemin chemin : tournee.getChemins()) {
                chemin.resetChemin();
            }
        }
        tournees.clear();
    }

    /**
     * Definit le nombre de livreur max que peut gerer le plan
     *
     * @param nbLivreurs Nombre de livreur maximum
     */
    public void setNbLivreurs(int nbLivreurs) {
        this.nbLivreurs = nbLivreurs;
    }

    @Override
    public String toString() {
        return "Plan{" +
                "noeuds=" + noeuds +
                ", troncons=" + troncons +
                ", livraisons=" + livraisons +
                ", entrepot=" + entrepot +
                ", heureDepart=" + heureDepart +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Plan plan = (Plan) o;

        if (noeuds != null ? !noeuds.equals(plan.noeuds) : plan.noeuds != null) return false;
        if (troncons != null ? !troncons.equals(plan.troncons) : plan.troncons != null) return false;
        if (entrepot != null ? !entrepot.equals(plan.entrepot) : plan.entrepot != null) return false;
        return heureDepart != null ? heureDepart.equals(plan.heureDepart) : plan.heureDepart == null;
    }

    @Override
    public int hashCode() {
        int result = noeuds != null ? noeuds.hashCode() : 0;
        result = 31 * result + (troncons != null ? troncons.hashCode() : 0);
        result = 31 * result + (entrepot != null ? entrepot.hashCode() : 0);
        result = 31 * result + (heureDepart != null ? heureDepart.hashCode() : 0);
        return result;
    }

    /**
     * Renvoie la tournee associee a un livreur donne
     *
     * @param livreur livreur dont on veut connaitre la tournee
     * @return tournee ou null
     */
    public Tournee getTourneeParLivreur(Livreur livreur){
        for(Tournee tournee : tournees){
            if(tournee.getLivreur() == livreur)
                return tournee;
        }
        return null;
    }

    /**
     * Renvoie la liste des livreurs en charge d'une tournee
     *
     * @return liste des livreurs
     */
    public ArrayList<Livreur> getLivreursEnCours() {
        ArrayList<Livreur> livreursCourants = new ArrayList<>();
        for (Tournee tournee : tournees) {
            if(!livreursCourants.contains(tournee.getLivreur()))
                livreursCourants.add(tournee.getLivreur());
        }
        return livreursCourants;
    }

    /**
     * Renvoie la tournee dans laquelle se trouve la livraison donnee
     *
     * @param livraison livraison dont on veut connaitre la tournee
     * @return tournee ou null
     */
    public Tournee getTourneeParLivraison(Livraison livraison){
        for(Tournee tournee : tournees) {
            for(Livraison liv : tournee.getLivraisons())
                if (liv == livraison)
                    return tournee;
        }
        return null;
    }
}
