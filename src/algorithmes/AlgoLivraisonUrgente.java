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

    private Tournee getDebutFinTravail(Livraison livraison, Livraison entrepot, Collection<Tournee> tournees, Date heureActuelle, int nbLivreurs)
    {
        Tournee tournee;
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

        int idLivreur = 0;
        Date heureRetourMin = new Date(0);

        for (int i = 0; i < nbLivreurs; i++) {
            Livreur livreur = ListeLivreurs.livreurs[i];

            if(tourneeModifiable.containsKey(livreur))
            {
                tournee = tourneeModifiable.get(livreur);
                Date heureRetour = tournee.getHeureAvecLivraisonSupplementaire(livraison, entrepot);

                if(heureRetour.compareTo(heureRetourMin) < 0)
                {
                    idLivreur = i;
                    heureRetourMin = heureRetour;
                }
            }else{
                Date heureDepart;
                if(heureActuelle.compareTo(finTravailleLivreur.get(ListeLivreurs.livreurs[i]))<0){
                    heureDepart = new Date(finTravailleLivreur.get(ListeLivreurs.livreurs[i]).getTime());
                } else {
                    heureDepart = new Date(heureActuelle.getTime());
                }

                double dureeCheminVersEntrepot = livraison.getCheminVers(entrepot).getLongueur()/ VITESSE;
                double dureeCheminDepuisEntrepot = entrepot.getCheminVers(livraison).getLongueur()/VITESSE;

                Date heureRetour = new Date((long) (heureDepart.getTime()+(dureeCheminVersEntrepot+dureeCheminDepuisEntrepot)*1000));

                if(heureRetour.compareTo(heureRetourMin) < 0)
                {
                    idLivreur = i;
                    heureRetourMin = heureRetour;
                }
            }
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

            Date heureDepart;
            if(heureActuelle.compareTo(finTravailleLivreur.get(ListeLivreurs.livreurs[idLivreur]))<0){
                heureDepart = new Date(finTravailleLivreur.get(ListeLivreurs.livreurs[idLivreur]).getTime());
            } else {
                heureDepart = new Date(heureActuelle.getTime());
            }

            tournee = new Tournee(livraisons,chemins,heureDepart,ListeLivreurs.livreurs[idLivreur]);
        }

        return tournee;
    }




}
