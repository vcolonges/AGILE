package controleur;

import modele.Livreur;
import thread.threadsimulation.ThreadSimulationAdapter;
import utils.Paire;

import java.util.HashMap;

public class EcouteurDeTacheSimulation extends ThreadSimulationAdapter {

    private Controler controler;

    public EcouteurDeTacheSimulation(Controler controler) {
        this.controler = controler;
    }

    @Override
    public void onUpdate(HashMap<Livreur, Paire<Double, Double>> update) {
        controler.updatePositionLivreurs(update);
    }
}
