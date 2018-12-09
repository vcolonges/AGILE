package modele;

import utils.Paire;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

public class Tournee {
    private ArrayList<Livraison> livraisons;
    private ArrayList<Chemin> chemins;
    private Date heureDepart;
    private Date retourEntrepot;
    private static final double VITESSE = 4.17;
    private Livreur livreur;

    public Tournee(ArrayList<Livraison> livraisons, ArrayList<Chemin> chemins, Date heureDepart, Livreur livreur)
    {
        this.livraisons = livraisons;
        this.chemins = chemins;
        this.heureDepart = heureDepart;
        this.livreur = livreur;
    }

    public ArrayList<Chemin> getChemins(){
        return this.chemins;
    }

    public void calculeHoraire(){
        Date heure = new Date(heureDepart.getTime());

        for (int i = 0; i < livraisons.size(); i++) {
            double dureeChemin = chemins.get(i).getLongueur()/VITESSE;
            heure.setTime((long) (heure.getTime()+dureeChemin*1000));
            livraisons.get(i).setHeureArrivee(new Date(heure.getTime()));
            heure.setTime(heure.getTime()+livraisons.get(i).getDuree()*1000);
        }

        double dureeChemin = chemins.get(chemins.size()-1).getLongueur()/VITESSE;
        heure.setTime((long) (heure.getTime()+dureeChemin*1000));
        retourEntrepot = new Date(heure.getTime());
    }

    public ArrayList<Livraison> getLivraisons() {
        return livraisons;
    }

    public Date getHeureDepart() {
        return heureDepart;
    }

    public Livreur getLivreur() {
        return livreur;
    }

    public void addLivraison(Livraison livraison){
        livraisons.add(livraison);
    }

    public void removeLivraison(Livraison livraison){
        livraisons.remove(livraison);
    }

    public void setLivreur(Livreur livreur) {
        this.livreur = livreur;
    }

    @Override
    public String toString() {
        return "Tournee{" +
                "chemins=" + chemins +
                '}';
    }

    public Date getRetourEntrepot() {
        return retourEntrepot;
    }

    public Paire<Double,Double> getPositionAt(Date time)
    {
        Noeud entrepot = chemins.get(0).getOrigine().getNoeud();
        if(time.compareTo(retourEntrepot) >= 0) return new Paire(entrepot.getLongitude(),entrepot.getLatitude()); // si la livraison est fini, on renvoie la position de l'entrepot

        Livraison firstLivraison = chemins.get(0).getOrigine(); // l'entrepot
        Chemin currentChemin = chemins.get(0);
        Livraison secondLivraison = chemins.get(0).getDestination(); // le premier point a livrer
        int indexLivraison = 1;
        while(time.compareTo(secondLivraison.getHeureArrivee())>=0) // si apres ou egale
        {
            currentChemin = chemins.get(indexLivraison);
            firstLivraison = currentChemin.getOrigine();
            secondLivraison = currentChemin.getDestination();
         }// on connait maintenant entre quels livraison se trouvera le livreur

        Date heureArriveSecondNode = secondLivraison.getHeureArrivee();
        if(heureArriveSecondNode == null) // si on tombe sur l entrepot
            heureArriveSecondNode = retourEntrepot;


        Date heureArriveeFirstNode = firstLivraison.getHeureArrivee();
        if(heureArriveeFirstNode == null)
            heureArriveeFirstNode = heureDepart;
        Date heureDepartFirstNode = new Date(heureArriveeFirstNode.getTime() + firstLivraison.getDuree());
        // si le livreur sera encore sur le lieu de livraison, on renvoie ses coordonnées
        if(time.before(heureDepartFirstNode))
            return new Paire(firstLivraison.getNoeud().getLongitude(),firstLivraison.getNoeud().getLatitude());

        Date nextStop = new Date(heureDepartFirstNode.getTime() + currentChemin.troncons.get(0).getDuree(VITESSE));
        int indexTroncon = 1;
        Troncon lastTroncon = currentChemin.getTroncons().get(0);
        while(time.compareTo(nextStop) >= 0)
        {
            long duree = lastTroncon.getDuree(VITESSE);
            nextStop = new Date(nextStop.getTime() + duree);
            lastTroncon = currentChemin.troncons.get(indexTroncon++);
        }


        Paire<Double,Double> averagePos = new Paire((lastTroncon.getOrigine().getLongitude() + lastTroncon.getDestination().getLongitude())/2,(lastTroncon.getOrigine().getLatitude() + lastTroncon.getDestination().getLatitude())/2);

        return averagePos;
    }
}
