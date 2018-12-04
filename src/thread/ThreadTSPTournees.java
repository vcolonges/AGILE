package thread;

import algorithmes.TSP;
import modele.Livraison;
import modele.Tournee;

import java.util.ArrayList;
import java.util.Date;

public class ThreadTSPTournees extends ThreadTSP {

    protected int nbLivreurs;

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
