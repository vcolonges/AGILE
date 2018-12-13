package controleur;

import modele.Tournee;
import thread.threadtsp.ThreadTSPAdapter;

import java.util.ArrayList;

public class EcouteurDeTacheTSP extends ThreadTSPAdapter {
    private Controler controler;

    public EcouteurDeTacheTSP(Controler controler) {
        this.controler = controler;
    }

    @Override
    public void threadComplete(Tournee tournee) {
        controler.tourneeGeneree(tournee);
    }

    @Override
    public void threadComplete(ArrayList<Tournee> tournees) {
        controler.tourneesGenerees(tournees);
    }
}
