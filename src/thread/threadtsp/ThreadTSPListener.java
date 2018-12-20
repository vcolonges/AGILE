package thread.threadtsp;

import modele.Tournee;

import java.util.ArrayList;

/**
 * interface permettant de gerer les evenement de fin de thread
 */
public interface ThreadTSPListener {
    /**
     * Thread complete pour des tournees
     * @param tournees les tournees calculees
     */
    void threadComplete(ArrayList<Tournee> tournees);

    /**
     * Thread complete pour une tournee
     * @param tournee la tournee calcule
     */
    void threadComplete(Tournee tournee);
}