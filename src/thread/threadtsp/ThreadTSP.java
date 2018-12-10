package thread.threadtsp;

import modele.Livraison;

import java.util.ArrayList;
import java.util.Date;

public abstract class ThreadTSP extends Thread{

    protected ArrayList<Livraison> livraisons;
    protected Livraison entrepot;
    protected Date heureDepart;
    protected ArrayList<ThreadTSPListener> listeners;

    protected ThreadTSP(ArrayList<Livraison> livraisons, Livraison entrepot, Date heureDepart) {
        this.livraisons = livraisons;
        this.entrepot = entrepot;
        this.heureDepart = heureDepart;
        listeners = new ArrayList<>();
    }

    public void addThreadListener(ThreadTSPListener l)
    {
        listeners.add(l);
    }
}