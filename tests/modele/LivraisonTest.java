package modele;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.ListResourceBundle;

import static org.junit.jupiter.api.Assertions.*;

class LivraisonTest {

    Livraison l;
    Livraison l2;
    Noeud n;
    HashSet<Chemin> chemins;

    @BeforeEach
    void setUp() {
        n = new Noeud(123,1.23,4.56);
        l = new Livraison(n,200);
        try {
            String stringDate = "31/12/1998 15:21:54";
            Date date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(stringDate);
            l.setHeureArrivee(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Livraison origine = new Livraison(new Noeud(158,1.26,5.3),154);
        Livraison destination = new Livraison(new Noeud(175,1.156,5.215),451);
        double longueur = 1500;
        Chemin c = new Chemin(origine,destination,longueur);
        chemins = new HashSet<>();
        chemins.add(c);
        l.addChemin(c);

        l2 = new Livraison(l);
    }

    @Test
    void getNoeud() {
        assertEquals(l.getNoeud(),n);
    }

    @Test
    void setNoeud() {
        Noeud newN = new Noeud(21,2.24,5.20);
        l.setNoeud(newN);
        assertEquals(l.getNoeud(),newN);
    }

    @Test
    void getDuree() {
        assertEquals(l.getDuree(),200);
    }

    @Test
    void setDuree() {
        l.setDuree(100);
        assertEquals(l.getDuree(),100);
    }

    @Test
    void getChemins() {
        assertEquals(l.getChemins(),chemins);
    }

    @Test
    void addChemin() {
        Livraison origine = new Livraison(new Noeud(454,0.454,1.287),254);
        Livraison destination = new Livraison(new Noeud(2542,0.5434,51.5727),456);
        double longueur = 1246;
        Chemin c = new Chemin(origine,destination,longueur);

        chemins.add(c);
        l.addChemin(c);

        assertEquals(l.getChemins(),chemins);
    }

    @Test
    void getCheminVers() {
        //TODO Victor | Anatolii
    }

    @Test
    void getHeureArrivee() {
        try {
            String stringDate = "31/12/1998 15:21:54";
            Date date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(stringDate);
            assertEquals(l.getHeureArrivee(),date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    void setHeureArrivee() {
        try {
            String stringDate = "29/10/2015 08:10:14";
            Date date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(stringDate);
            l.setHeureArrivee(date);
            assertEquals(l.getHeureArrivee(),date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}