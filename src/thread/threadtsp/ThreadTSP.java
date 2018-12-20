package thread.threadtsp;

import modele.Livraison;

import java.util.ArrayList;
import java.util.Date;

/**
 * Represente un thread permettant de generer des tournees
 */
public abstract class ThreadTSP extends Thread{

    /**
     * Les livraisons
     */
    protected ArrayList<Livraison> livraisons;
    /**
     * L'entrepot
     */
    protected Livraison entrepot;
    /**
     * L'heure de depart
     */
    protected Date heureDepart;
    /**
     * Les listener qui attendent la fin du thread
     */
    protected ArrayList<ThreadTSPListener> listeners;

    /**
     * Commence la creation d'un thread
     * @param livraisons les livraisons
     * @param entrepot l'entrepot
     * @param heureDepart l'heure de depart
     */
    protected ThreadTSP(ArrayList<Livraison> livraisons, Livraison entrepot, Date heureDepart) {
        this.livraisons = livraisons;
        this.entrepot = entrepot;
        this.heureDepart = heureDepart;
        listeners = new ArrayList<>();
    }

    /**
     * Ajoute un listener au thread
     * @param l le listener
     */
    public void addThreadListener(ThreadTSPListener l)
    {
        listeners.add(l);
    }
}