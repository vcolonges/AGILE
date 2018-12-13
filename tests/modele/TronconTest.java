package modele;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TronconTest {

    Troncon t;
    Noeud origine;
    Noeud destination;

    @BeforeEach
    void setUp() {
        origine = new Noeud(48,2.54,1.98);
        destination = new Noeud(29,1.59,5.21);
        t = new Troncon(origine,destination,154,"albert einstein");
    }

    @Test
    void getOrigine() {
        assertEquals(t.getOrigine(),origine);
    }

    @Test
    void getDestination() {
        assertEquals(t.getDestination(),destination);
    }

    @Test
    void getLongueur() {
        assertEquals(t.getLongueur(),154);
    }

    @Test
    void getNomRue() {
        assertEquals(t.getNomRue(),"albert einstein");
    }

    @Test
    void getDuree() {
        long duree = (long) ((154.0f/15)*1000);
        assertEquals(t.getDuree(15),duree);
    }
}