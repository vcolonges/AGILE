package modele;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class PlanTest {

    Plan p;
    HashMap<Long, Noeud> noeuds;
    HashSet<Troncon> troncons;
    HashMap<Long, Livraison> livraisons;
    Livraison entrepot;
    Date heureDepart;
    int nbLivreurs;

    @BeforeEach
    void setUp() {
        p = new Plan();
        noeuds = new HashMap<>();
        troncons = new HashSet<>();
        livraisons = new HashMap<>();

        Noeud n1 = new Noeud(156,2.154,6.25);
        Noeud n2 = new Noeud(542,5.214,1.32);
        p.addNoeud(n1);
        p.addNoeud(n2);
        noeuds.put(n1.getId(), n1);
        noeuds.put(n2.getId(), n2);

        Troncon t1 = new Troncon(n1,n2,154,"albert einstein");
        troncons.add(t1);
        p.addTroncon(t1);

        Livraison l1 = new Livraison(n1,154);
        Livraison l2 = new Livraison(n2,466);
        livraisons.put(l1.getNoeud().getId(), l1);
        livraisons.put(l2.getNoeud().getId(), l2);
        p.addLivraison(l1);
        p.addLivraison(l2);

        Livraison entrepot = new Livraison(new Noeud(1486,0.2158,2.368),154);
        p.setEntrepot(entrepot);
        this.entrepot = entrepot;

        try {
            String stringDate = "31/12/1998 15:21:54";
            Date date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(stringDate);
            p.setHeureDepart(date);
            heureDepart = date;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        nbLivreurs = 15;
        p.setNbLivreurs(nbLivreurs);
    }

    @Test
    void getNoeuds() {
        assertEquals(p.getNoeuds(),noeuds);
    }

    @Test
    void addNoeud() {
        Noeud n1 = new Noeud(154,3.254,3.15);
        p.addNoeud(n1);
        noeuds.put(n1.getId(), n1);
        assertEquals(p.getNoeuds(),noeuds);
    }

    @Test
    void getTroncons() {
        assertEquals(p.getTroncons(),troncons);
    }

    @Test
    void addTroncon() {
        Noeud n1 = new Noeud(205,1.214,2.02);
        Noeud n2 = new Noeud(321,6.24,3.15);
        Troncon t1 = new Troncon(n1,n2,533,"avenue republique");
        troncons.add(t1);
        p.addTroncon(t1);

        assertEquals(p.getTroncons(),troncons);
    }

    @Test
    void getMaxLat() {
        assertEquals(p.getMaxLat(),5.214);
    }

    @Test
    void getMaxLong() {
        assertEquals(p.getMaxLong(),6.25);
    }

    @Test
    void getMinLat() {
        assertEquals(p.getMinLat(),2.154);
    }

    @Test
    void getMinLong() {
        assertEquals(p.getMinLong(),1.32);
    }

    @Test
    void getLivraisons() {
        assertEquals(p.getLivraisons(),livraisons);
    }

    @Test
    void addLivraison() {
        Noeud n1 = new Noeud(157,3.021,0.65);
        Livraison l1 = new Livraison(n1,2065);
        p.addLivraison(l1);
        livraisons.put(l1.getNoeud().getId(), l1);

        assertEquals(p.getLivraisons(),livraisons);
    }

    @Test
    void getEntrepot() {
        assertEquals(p.getEntrepot(),entrepot);
    }

    @Test
    void setEntrepot() {
        Livraison entrepot = new Livraison(new Noeud(4521,1.324,0.186),2032);
        p.setEntrepot(entrepot);
        this.entrepot = entrepot;
        assertEquals(p.getEntrepot(),entrepot);
    }

    @Test
    void getHeureDepart() {
        assertEquals(p.getHeureDepart(),heureDepart);
    }

    @Test
    void setHeureDepart() {
        try {
            String stringDate = "05/12/2018 10:21:13";
            Date date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(stringDate);
            p.setHeureDepart(date);
            heureDepart = date;
            assertEquals(p.getHeureDepart(),date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getTournees() {
        //TODO Victor | Anatolii
    }

    @Test
    void setTournees() {
        //TODO Victor | Anatolii
    }

    @Test
    void addTournee() {
        //TODO Victor | Anatolii
    }

    @Test
    void removeTournee() {
        //TODO Victor | Anatolii
    }

    @Test
    void getNbLivreurs() {
        assertEquals(p.getNbLivreurs(),nbLivreurs);
    }

    @Test
    void setNbLivreurs() {
        nbLivreurs = 10;
        p.setNbLivreurs(nbLivreurs);
        assertEquals(p.getNbLivreurs(),nbLivreurs);
    }

    @Test
    void getTourneeParLivreur() {
        //TODO Emilie
    }

    @Test
    void getLivreursEnCours() {
        //TODO Emilie
    }

    @Test
    void getTourneeParLivraison() {
        //TODO Emilie
    }

    @Test
    void getLivraisonsUrgentes() {
        //TODO Victor | Anatolii
    }

    @Test
    void addLivraisonUrgente() {
        //TODO Victor | Anatolii
    }
}