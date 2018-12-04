package thread;

import modele.Tournee;

import java.util.ArrayList;

public interface ThreadListener {
    void threadComplete(ArrayList<Tournee> tournees);
    void threadComplete(Tournee tournees);
}
