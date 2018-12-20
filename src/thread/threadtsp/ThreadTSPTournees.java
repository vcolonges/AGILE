package thread.threadtsp;

import algorithmes.TSP;
import modele.Livraison;
import modele.Tournee;

import java.util.ArrayList;
import java.util.Date;

public class ThreadTSPTournees extends ThreadTSP {

    /**
     * le nombre de livreur
     */
    protected int nbLivreurs;

    /**
     * Creer un thread permettant de generer des tournees
     * @param livraisons les livraisons
     * @param nbrLivreur le nombre de livreur
     * @param entrepot l'entrepot
     * @param heureDepart l'heure de depart
     */
    protected ThreadTSPTournees(ArrayList<Livraison> livraisons, int nbrLivreur, Livraison entrepot, Date heureDepart) {
        super(livraisons,entrepot,heureDepart);
        this.nbLivreurs = nbrLivreur;
    }

    @Override
    public void run() {
        ArrayList<Tournee> tournees = TSP.calculerLesTournees(livraisons,nbLivreurs,entrepot, heureDepart);
        listeners.forEach(listener -> {listener.threadComplete(tournees);});
    }
}