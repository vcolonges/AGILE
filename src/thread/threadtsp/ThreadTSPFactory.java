package thread.threadtsp;

import modele.Livraison;
import modele.Livreur;

import java.util.ArrayList;
import java.util.Date;

/**
 * Construit un ThreadTSP
 */
public class ThreadTSPFactory {
    /**
     * Renvoie un threadTSP pour calculer une tournee
     * @param livraisonCollection la liste des livraisons
     * @param entrepot l'entrepot
     * @param heureDepart l'heure de depart
     * @param livreur le livreur effectuant la tournee
     * @return un threadTSP pret a etre lance.
     */
    public static ThreadTSP getTSPThread(ArrayList<Livraison> livraisonCollection, Livraison entrepot, Date heureDepart, Livreur livreur)
    {
        return new ThreadTSPTournee(livraisonCollection, entrepot, heureDepart, livreur);
    }

    /**
     * Renvoie un threadTSP pour calculer des tournees
     * @param livraisons la liste des livraisons
     * @param nbrLivreur le nombre de livreurs
     * @param entrepot l'entrepot
     * @param heureDepart l'heure de depart
     * @return un threadTSP pret a etre lance.
     */
    public static ThreadTSP getTSPThread(ArrayList<Livraison> livraisons, int nbrLivreur, Livraison entrepot, Date heureDepart)
    {
        return new ThreadTSPTournees(livraisons,nbrLivreur,entrepot,heureDepart);
    }

}