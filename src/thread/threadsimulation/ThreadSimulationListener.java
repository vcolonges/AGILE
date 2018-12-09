package thread.threadsimulation;

import modele.Livreur;
import utils.Paire;

import java.util.HashMap;

public interface ThreadSimulationListener {
    public void onUpdate(HashMap<Livreur, Paire<Double,Double>> update);
}
