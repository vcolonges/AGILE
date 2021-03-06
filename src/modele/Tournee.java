package modele;

import utils.Paire;

import java.util.ArrayList;
import java.util.Date;

/**
 * Classe utilisee pour definir les entites de tournees, une tournee etant associee a un livreur et contenant l'ensemble des points de livraisons que le livreur doit livrer
 */
public class Tournee {
    private ArrayList<Livraison> livraisons;
    private ArrayList<Chemin> chemins;
    private Date heureDepart;

    private Date retourEntrepot;
    public static final double VITESSE = 4.17;
    private Livreur livreur;

    /**
     * Creation de l'entite tournee a partir de la liste des livraisons a livrer, les chemins a emprunter durant la tournee, l'heure de depart du livreur ainsi que le livreur charge de cette tournee.
     * Les livraisons et chemins sont ordonnes par ordre de passage
     *
     * @param livraisons livraisons a livrer
     * @param chemins chemins a emprunter
     * @param heureDepart heure de depart de l'entreprot
     * @param livreur livreur en cahrge de la tournee
     */
    public Tournee(ArrayList<Livraison> livraisons, ArrayList<Chemin> chemins, Date heureDepart, Livreur livreur)
    {
        this.livraisons = livraisons;
        this.chemins = chemins;
        this.heureDepart = heureDepart;
        this.livreur = livreur;
    }

    /**
     * Renvoie la liste des chemins qu'aura en emprunter le livreur durant se tournee
     *
     * @return chemins
     */
    public ArrayList<Chemin> getChemins(){
        return this.chemins;
    }


    /**
     * Cette méthode calcule les horaires de passage du livreur aux Livraisons et l'heure de retour à l'entrepot
     */
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

    /**
     * Cette méthode calcul l'heure de retour si on lui ajoutait la Livraison livraison en fin de tournee
     * Cette méthode ne modifie pas la tournee !!
     * @param livraison
     * @param entrepot
     * @return
     */

    public Date getHeureAvecLivraisonSupplementaire(Livraison livraison, Livraison entrepot)
    {
        double dureeCheminRetirer = chemins.get(chemins.size()-1).getLongueur()/VITESSE;
        double dureeCheminVersEntrepot = livraison.getCheminVers(entrepot).getLongueur()/VITESSE;
        double dureeCheminVersLivraison = livraisons.get(livraisons.size()-1).getCheminVers(livraison).getLongueur()/VITESSE;

        return new Date((long) (retourEntrepot.getTime()+(dureeCheminVersEntrepot+dureeCheminVersLivraison-dureeCheminRetirer)*1000));
    }

    /**
     * Renvoie la lsite des livraisons a livrer durant la tournee
     *
     * @return livraisons
     */
    public ArrayList<Livraison> getLivraisons() {
        return livraisons;
    }

    /**
     * Renvoie l'heure de depart de l'entreprot du livreur
     *
     * @return heureDepart
     */
    public Date getHeureDepart() {
        return heureDepart;
    }

    /**
     * Renvoie le livreur associe a la tournee
     *
     * @return livreur
     */
    public Livreur getLivreur() {
        return livreur;
    }

    /**
     * Ajoute une livraison à la tournee
     * @param livraison Livraison à ajouter
     */
    public void addLivraison(Livraison livraison){
        livraisons.add(livraison);
    }

    /**
     * Supprime une livraison de la tournee
     * @param livraison Livraison à supprimer
     */
    public void removeLivraison(Livraison livraison){
        livraisons.remove(livraison);
    }

    /**
     * Definit le livreur associe a la tournee
     *
     * @param livreur livreur qui fait la tournee
     */
    public void setLivreur(Livreur livreur) {
        this.livreur = livreur;
    }

    /**
     * Definit la date de retour à l'entrepot
     * @param retourEntrepot
     */
    public void setRetourEntrepot(Date retourEntrepot) {
        this.retourEntrepot = retourEntrepot;
    }


    /**
     * Renvoie true si la tournee est modifiable
     * Une tournee est modifiable si elle n'a pas débuté
     * @param heureActuelle
     * @return
     */
    public boolean isModifiable(Date heureActuelle)
    {
        return heureActuelle.compareTo(heureDepart)<0;
    }

    /**
     *
     * @return La date de retour à l'entrepot de cette Tournee
     */
    public Date getRetourEntrepot() {
        return retourEntrepot;
    }

    /**
     *
     * @param time Heure à laquelle on veut connaitre la position
     * @return Une Paire Latitude,Longitude de la position à l'instant 'time'
     */
    public Paire<Double,Double> getPositionAt(Date time)
    {
        Noeud entrepot = chemins.get(0).getOrigine().getNoeud();
        if(time.compareTo(retourEntrepot) >= 0) return new Paire<>(entrepot.getLongitude(),entrepot.getLatitude()); // si la livraison est fini, on renvoie la position de l'entrepot

        Livraison firstLivraison = chemins.get(0).getOrigine(); // l'entrepot
        Chemin currentChemin = chemins.get(0);
        Livraison secondLivraison = chemins.get(0).getDestination(); // le premier point a livrer
        int indexLivraison = 1;
        Date heureArriveSecondNode = secondLivraison.getHeureArrivee();
        while(time.compareTo(heureArriveSecondNode)>=0) // si apres ou egale
        {
            currentChemin = chemins.get(indexLivraison++);
            firstLivraison = currentChemin.getOrigine();
            secondLivraison = currentChemin.getDestination();
            heureArriveSecondNode = secondLivraison.getHeureArrivee();
            if(heureArriveSecondNode == null)
                heureArriveSecondNode = retourEntrepot;
         }// on connait maintenant entre quels livraison se trouvera le livreur

        heureArriveSecondNode = secondLivraison.getHeureArrivee();
        if(heureArriveSecondNode == null) // si on tombe sur l entrepot
            heureArriveSecondNode = retourEntrepot;


        Date heureArriveeFirstNode = firstLivraison.getHeureArrivee();
        if(heureArriveeFirstNode == null)
            heureArriveeFirstNode = heureDepart;
        Date heureDepartFirstNode = new Date(heureArriveeFirstNode.getTime() + firstLivraison.getDuree()*1000);
        // si le livreur sera encore sur le lieu de livraison, on renvoie ses coordonnées
        if(time.before(heureDepartFirstNode)){
            return new Paire<>(firstLivraison.getNoeud().getLongitude(),firstLivraison.getNoeud().getLatitude());
        }


        Date nextStop = new Date(heureDepartFirstNode.getTime() + currentChemin.troncons.get(0).getDuree(VITESSE));
        int indexTroncon = 0;
        Troncon lastTroncon = currentChemin.getTroncons().get(0);
        while(time.compareTo(nextStop) >= 0 && indexTroncon != currentChemin.troncons.size())
        {
            long duree = lastTroncon.getDuree(VITESSE);
            nextStop = new Date(nextStop.getTime() + duree);
            lastTroncon = currentChemin.troncons.get(indexTroncon++);
        }

        return new Paire<>((lastTroncon.getOrigine().getLongitude() + lastTroncon.getDestination().getLongitude())/2,(lastTroncon.getOrigine().getLatitude() + lastTroncon.getDestination().getLatitude())/2);
    }

    /**
     * Ajoute la livraison en fin de tournee
     * @param livraison Livraison a ajouter
     * @param entrepot Entrepot d'ou il faut partir
     */
    public void ajouteLivraisonFinTournee(Livraison livraison, Livraison entrepot) {
        Chemin cheminVersLivraison = livraisons.get(livraisons.size() - 1).getCheminVers(livraison);
        Chemin cheminVersEntrepot = livraison.getCheminVers(entrepot);

        chemins.remove(chemins.size() - 1);
        chemins.add(cheminVersLivraison);
        chemins.add(cheminVersEntrepot);

        livraisons.add(livraison);
        calculeHoraire();
    }
}
