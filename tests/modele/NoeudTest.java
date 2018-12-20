package modele;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NoeudTest {

    Noeud n;

    @BeforeEach
    void setUp() {
        n = new Noeud(154,1.842,5.268);
    }

    @Test
    void getId() {
        assertEquals(n.getId(),154);
    }

    @Test
    void getLatitude() {
        assertEquals(n.getLatitude(),1.842);
    }

    @Test
    void getLongitude() {
        assertEquals(n.getLongitude(),5.268);
    }

    @Test
    void addTronconAdjacent() {
        //TODO Victor | Anatolii
    }

    @Test
    void getTronconsAdjacents() {
        //TODO Victor | Anatolii
    }

    @Test
    void setId() {
        n.setId(145);
        assertEquals(n.getId(),145);
    }

    @Test
    void setLatitude() {
        n.setLatitude(1.214);
        assertEquals(n.getLatitude(),1.214);
    }

    @Test
    void setLongitude() {
        n.setLongitude(4.256);
        assertEquals(n.getLongitude(),4.256);
    }
}