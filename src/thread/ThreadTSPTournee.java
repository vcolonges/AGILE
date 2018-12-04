package thread;

import algorithmes.TSP;
import modele.Livraison;
import modele.Tournee;

import java.util.ArrayList;
import java.util.Date;

public class ThreadTSPTournee extends ThreadTSP {

    public ThreadTSPTournee(ArrayList<Livraison> livraisonCollection, Livraison entrepot, Date heureDepart) {
        super(livraisonCollection,entrepot,heureDepart);
    }

    @Override
    public void run()
    {
        Tournee tournee = TSP.calculerTournee(livraisons,entrepot,heureDepart);
        listeners.forEach(listener -> {listener.threadComplete(tournee);});
    }
}
