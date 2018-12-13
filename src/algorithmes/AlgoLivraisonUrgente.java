package algorithmes;

import modele.Chemin;
import modele.Livraison;
import modele.Livreur;
import modele.Tournee;
import utils.ListeLivreurs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import static modele.Tournee.VITESSE;

public class AlgoLivraisonUrgente {

    private HashMap<Livreur,Date> finTravailleLivreur;
    private HashMap<Livreur,Tournee> tourneeModifiable;
    private int idLivreur;
    private Date heureRetourMin;

    public AlgoLivraisonUrgente() {
        this.finTravailleLivreur = new HashMap<>();
        this.tourneeModifiable = new HashMap<>();
        this.idLivreur = 0;
        this.heureRetourMin = new Date(Long.MAX_VALUE);
    }

    private void remplirMap(Collection<Tournee> tournees, Date heureActuelle){
        for (Tournee item: tournees) {
            Livreur livreur = item.getLivreur();
            Date debut = item.getHeureDepart();
            Date fin = item.getRetourEntrepot();

            if(finTravailleLivreur.containsKey(livreur))
            {
                if(fin.compareTo(finTravailleLivreur.get(livreur))>0)
                {
                    finTravailleLivreur.replace(livreur,fin);
                }
            }else{
                finTravailleLivreur.put(livreur,fin);
            }

            if(item.isModifiable(heureActuelle)){
                tourneeModifiable.put(livreur,item);
            }
        }
    }

    public Tournee modifiTournee(Livraison livraison, Collection<Livraison> livraisonsUrgentes, Livraison entrepot, Collection<Tournee> tournees, Date heureActuelle, int nbLivreurs)
    {
        Tournee tournee;

        AlgoParcour algoParcour = new AlgoParcour();
        ArrayList<Livraison> livraisonsEntrepot = new ArrayList<>(livraisonsUrgentes);
        livraisonsEntrepot.add(entrepot);
        for (Livraison depart: livraisonsEntrepot) {
            ArrayList<Chemin> chemins = algoParcour.calculChemin(depart, livraisonsEntrepot);
            depart.getChemins().addAll(chemins);
        }

        remplirMap(tournees,heureActuelle);

        for (int i = 0; i < nbLivreurs; i++) {
            livreurHeureMin(i,livraison,entrepot,heureActuelle);
        }

        if(tourneeModifiable.containsKey(ListeLivreurs.livreurs[idLivreur]))
        {
            tournee = tourneeModifiable.get(ListeLivreurs.livreurs[idLivreur]);
            tournee.ajouteLivraisonFinTournee(livraison, entrepot);
        }else{
            ArrayList<Livraison> livraisons = new ArrayList<>();
            livraisons.add(livraison);

            ArrayList<Chemin> chemins = new ArrayList<>();
            chemins.add(entrepot.getCheminVers(livraison));
            chemins.add(livraison.getCheminVers(entrepot));

            Date heureDepart = minDate(heureActuelle, idLivreur);

            tournee = new Tournee(livraisons,chemins,heureDepart,ListeLivreurs.livreurs[idLivreur]);
        }
        tournee.calculeHoraire();
        return tournee;
    }

    private Date minDate(Date heureActuelle, int idLivreur) {
        Date heureDepart;
        if(heureActuelle.compareTo(finTravailleLivreur.get(ListeLivreurs.livreurs[idLivreur]))<0){
            heureDepart = new Date(finTravailleLivreur.get(ListeLivreurs.livreurs[idLivreur]).getTime());
        } else {
            heureDepart = new Date(heureActuelle.getTime());
        }
        return heureDepart;
    }

    private void livreurHeureMin(int idLivreurActuel, Livraison livraison, Livraison entrepot, Date heureActuelle){
        Tournee tournee;
        Livreur livreur = ListeLivreurs.livreurs[idLivreurActuel];

        if(tourneeModifiable.containsKey(livreur))
        {
            tournee = tourneeModifiable.get(livreur);
            Date heureRetour = tournee.getHeureAvecLivraisonSupplementaire(livraison, entrepot);

            if(heureRetour.compareTo(heureRetourMin) < 0)
            {
                idLivreur = idLivreurActuel;
                heureRetourMin = heureRetour;
            }
        }else{
            Date heureDepart;
            heureDepart = minDate(heureActuelle, idLivreurActuel);

            double dureeCheminVersEntrepot = livraison.getCheminVers(entrepot).getLongueur()/ VITESSE;
            double dureeCheminDepuisEntrepot = entrepot.getCheminVers(livraison).getLongueur()/VITESSE;

            Date heureRetour = new Date((long) (heureDepart.getTime()+(dureeCheminVersEntrepot+dureeCheminDepuisEntrepot)*1000));

            if(heureRetour.compareTo(heureRetourMin) < 0)
            {
                idLivreur = idLivreurActuel;
                heureRetourMin = heureRetour;
            }
        }
    }


}
