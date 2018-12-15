package modele;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CheminTest {

    Chemin c;
    Chemin c2;
    Livraison origine;
    Livraison destination;
    double longueur;
    ArrayList<Troncon> troncons;

    @BeforeEach
    void setUp() {
        c2 = new Chemin();
        origine = new Livraison(new Noeud(158,1.26,5.3),154);
        destination = new Livraison(new Noeud(175,1.156,5.215),451);
        longueur = 1500;
        c = new Chemin(origine,destination,longueur);
        troncons = new ArrayList<>();
        Troncon t = new Troncon(new Noeud(158,1.26,5.3),new Noeud(168,1.316,3.26),100,"Albert einstein");
        Troncon t2 = new Troncon(new Noeud(168,1.316,3.26),new Noeud(175,1.156,5.215),500,"Albert einstein");
        troncons.add(t);
        troncons.add(t2);
        c.setTroncons(troncons);
    }

    @Test
    void setTroncons() {
        Troncon t = new Troncon(new Noeud(456,0.153,4.64),new Noeud(1567,1.6846,0.87654),546,"Albert einstein");
        troncons.clear();
        troncons.add(t);
        c.setTroncons(troncons);

        assertEquals(c.getTroncons(),troncons);
    }

    @Test
    void getOrigine() {
        assertEquals(c.getOrigine(),origine);
    }

    @Test
    void getDestination() {
        assertEquals(c.getDestination(),destination);
    }

    @Test
    void getLongueur() {
        assertEquals(c.getLongueur(),longueur);
    }

    @Test
    void getTroncons() {
        assertEquals(c.getTroncons(),troncons);
    }

}