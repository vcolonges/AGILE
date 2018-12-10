package thread.threadtsp;

import modele.Tournee;

import java.util.ArrayList;

public interface ThreadTSPListener {
    void threadComplete(ArrayList<Tournee> tournees);
    void threadComplete(Tournee tournees);
}
