package thread;

import modele.Livraison;
import modele.Livreur;

import java.util.ArrayList;
import java.util.Date;

public class ThreadTSPFactory {
    public static ThreadTSP getTSPThread(ArrayList<Livraison> livraisonCollection, Livraison entrepot, Date heureDepart, Livreur livreur)
    {
        return new ThreadTSPTournee(livraisonCollection, entrepot, heureDepart, livreur);
    }

    public static ThreadTSP getTSPThread(ArrayList<Livraison> livraisons, int nbrLivreur, Livraison entrepot, Date heureDepart)
    {
        return new ThreadTSPTournees(livraisons,nbrLivreur,entrepot,heureDepart);
    }

}
