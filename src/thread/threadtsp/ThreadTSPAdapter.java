package thread.threadtsp;

import modele.Tournee;

import java.util.ArrayList;

/**
 * Classe recevant des evenements de fin de thread
 */
public class ThreadTSPAdapter implements ThreadTSPListener {
    @Override
    public void threadComplete(ArrayList<Tournee> tournees) {

    }

    @Override
    public void threadComplete(Tournee tournee) {

    }
}