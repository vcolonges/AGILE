package controleur;

import modele.Tournee;
import thread.ThreadTSPAdapter;

import java.util.ArrayList;

public class EcouteurDeTache extends ThreadTSPAdapter {
    private Controler controler;

    public EcouteurDeTache(Controler controler) {
        this.controler = controler;
    }

    @Override
    public void threadComplete(Tournee tournees) {
        // TODO
    }

    @Override
    public void threadComplete(ArrayList<Tournee> tournees) {
        controler.tourneesGenerees(tournees);
    }
}
