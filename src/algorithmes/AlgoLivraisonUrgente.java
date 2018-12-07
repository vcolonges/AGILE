package algorithmes;

import modele.Livraison;
import modele.Livreur;
import modele.Tournee;
import utils.ListeLivreurs;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

public class AlgoLivraisonUrgente {

    private HashMap<Livreur,Date> debutTravailLivreur;
    private HashMap<Livreur,Date> finTravailleLivreur;
    private HashMap<Livreur,Tournee> tourneeModifiable;

    private void getDebutFinTravail(Collection<Tournee> tournees, Date heureActuelle, int nbLivreurs)
    {
        for (Tournee item: tournees) {
            Livreur livreur = item.getLivreur();
            Date debut = item.getHeureDepart();
            Date fin = item.getRetourEntrepot();

            if(debutTravailLivreur.containsKey(livreur))
            {
                if(debut.compareTo(debutTravailLivreur.get(livreur))<0)
                {
                    debutTravailLivreur.replace(livreur,debut);
                }
            }else{
                debutTravailLivreur.put(livreur,debut);
            }

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

        for (int i = 0; i < nbLivreurs; i++) {
            Livreur livreur = ListeLivreurs.livreurs[i];


        }
    }




}
