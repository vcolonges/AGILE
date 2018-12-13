package modele;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.ListeLivreurs;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TourneeTest {

    Tournee t;
    ArrayList<Livraison> livraisons;
    ArrayList<Chemin> chemins;
    Livraison livraison1;
    Livraison livraison2;

    @BeforeEach
    void setUp() {
        livraisons = new ArrayList<>();
        livraison1 = new Livraison(new Noeud(154,1.547,3.25),159);
        livraison2 = new Livraison(new Noeud(259,2.366,5.21),21);
        livraisons.add(livraison1);
        livraisons.add(livraison2);
        chemins = new ArrayList<>();
        chemins.add(new Chemin(livraison1,livraison2,26.5));
        try {
            String stringDate = "31/12/1998 15:21:54";
            Date date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(stringDate);
            t = new Tournee(livraisons,chemins,date, ListeLivreurs.livreurs[0]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getChemins() {
        //TODO Victor | Anatolii
    }

    @Test
    void calculeHoraire() {
        //TODO Victor | Anatolii
    }

    @Test
    void getHeureAvecLivraisonSupplementaire() {
        //TODO Victor | Anatolii
    }

    @Test
    void getLivraisons() {
        assertEquals(t.getLivraisons(), livraisons);
    }

    @Test
    void getHeureDepart() {
        try {
            String stringDate = "31/12/1998 15:21:54";
            Date date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(stringDate);
            assertEquals(t.getHeureDepart(), date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getLivreur() {
        assertEquals(t.getLivreur(), ListeLivreurs.livreurs[0]);
    }

    @Test
    void addLivraison() {
        Livraison l = new Livraison(new Noeud(157,2.699,1.25),248);
        livraisons.add(l);
        t.addLivraison(l);
        assertEquals(t.getLivraisons(), livraisons);
    }

    @Test
    void removeLivraison() {
        Livraison l = new Livraison(new Noeud(157,2.699,1.25),248);
        t.removeLivraison(l);
        livraisons.remove(l);
        assertEquals(t.getLivraisons(), livraisons);
    }

    @Test
    void setLivreur() {
        t.setLivreur(ListeLivreurs.livreurs[1]);
        assertEquals(t.getLivreur(), ListeLivreurs.livreurs[1]);
    }

    @Test
    void isModifiable() {
        //TODO Victor | Anatolii
    }

    @Test
    void getRetourEntrepot() {
        //TODO Victor | Anatolii
    }

    @Test
    void getPositionAt() {
        //TODO Victor | Anatolii
    }

    @Test
    void ajouteLivraisonFinTournee() {
        //TODO Victor | Anatolii
    }
}