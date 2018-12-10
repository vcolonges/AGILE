package thread.threadsimulation;

import modele.Livreur;
import modele.Tournee;
import utils.Paire;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class ThreadSimulation extends Thread {

    private final ArrayList<Tournee> tournees;
    private final Date startTime;
    private ArrayList<ThreadSimulationListener> listeners;

    public ThreadSimulation(ArrayList<Tournee> tournees, Date startTime) {
        this.tournees = tournees;
        this.startTime = startTime;
        listeners = new ArrayList<>();
    }

    public void addThreadListener(ThreadSimulationListener l)
    {
        listeners.add(l);
    }

    @Override
    public void run() {
        Date currentTime = startTime;
        while(true) {
            HashMap<Livreur, Paire<Double,Double>> updateInfo = new HashMap<>();
            for (Tournee t : tournees) {
                System.out.println(currentTime);
                updateInfo.put(t.getLivreur(),t.getPositionAt(currentTime));
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Calendar c = Calendar.getInstance();
                c.setTime(currentTime);
                c.add(Calendar.SECOND,60);
                currentTime = c.getTime();
            }
            listeners.forEach(l->l.onUpdate(updateInfo));
        }
    }
}
