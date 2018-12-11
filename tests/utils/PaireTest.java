package utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaireTest {

    Paire p;

    @BeforeEach
    void setUp() {
        p = new Paire<>(1,3.5);
    }

    @Test
    void testEquals(){
        Paire newP = new Paire<>(1,3.5);
        assertEquals(p,newP);
    }

    @Test
    void testPremier(){
        assertEquals(p.getPremier(),1);
        p.setPremier(3);
        assertEquals(p.getPremier(),3);
    }

    @Test
    void testSecond(){
        assertEquals(p.getSecond(),3.5);
        p.setSecond(2.75);
        assertEquals(p.getSecond(),2.75);
    }
}