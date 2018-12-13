package modele;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class LivreurTest {

    Livreur l;

    @BeforeEach
    void setUp() {
        l = new Livreur("Jerome",new Color(205,148,103));
    }

    @Test
    void getPrenom() {
        assertEquals(l.getPrenom(),"Jerome");
    }

    @Test
    void setPrenom() {
        l.setPrenom("Jeremy");
        assertEquals(l.getPrenom(),"Jeremy");
    }

    @Test
    void getCouleur() {
        assertEquals(l.getCouleur(),new Color(205,148,103));
    }

    @Test
    void setCouleur() {
        l.setCouleur(new Color(39,21,153));
        assertEquals(l.getCouleur(),new Color(39,21,153));
    }
}