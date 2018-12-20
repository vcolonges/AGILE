package thread.threadtsp;

import algorithmes.TSP;
import modele.Livraison;
import modele.Livreur;
import modele.Tournee;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;

public class ThreadTSPTournee extends ThreadTSP {

    /**
     * le livreur
     */
    private Livreur livreur;

    /**
     * Creer un thread permettant de calculer une tournee
     * @param livraisonCollection les livraisons
     * @param entrepot l'entrepot
     * @param heureDepart l'heure de depart
     * @param livreur le livreur
     */
    protected ThreadTSPTournee(ArrayList<Livraison> livraisonCollection, Livraison entrepot, Date heureDepart, Livreur livreur) {
        super(livraisonCollection,entrepot,heureDepart);
        this.livreur = livreur;
    }

    @Override
    public void run()
    {
        Tournee tournee = TSP.calculerTournee(livraisons,entrepot,heureDepart, livreur);
        listeners.forEach(listener -> {listener.threadComplete(tournee);});
    }
}