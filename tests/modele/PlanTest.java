package modele;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.ListeLivreurs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class PlanTest {

    Plan p;
    HashMap<Long, Noeud> noeuds;
    HashSet<Troncon> troncons;
    HashMap<Long, Livraison> livraisons;
    ArrayList<Livraison> listelivraisons;
    HashMap<Long, Livraison> livraisonsUrgentes;
    ArrayList<Chemin> chemins;
    Livraison entrepot;
    Date heureDepart;
    int nbLivreurs;
    ArrayList<Tournee> tournees;
    Date date;

    @BeforeEach
    void setUp() {
        p = new Plan();
        noeuds = new HashMap<>();
        troncons = new HashSet<>();
        livraisons = new HashMap<>();
        tournees = new ArrayList<>();
        livraisonsUrgentes = new HashMap<>();

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
        listelivraisons = new ArrayList<>();
        listelivraisons.add(l1);
        listelivraisons.add(l2);
        p.addLivraison(l1);
        p.addLivraison(l2);

        Livraison entrepot = new Livraison(new Noeud(1486,0.2158,2.368),154);
        p.setEntrepot(entrepot);
        this.entrepot = entrepot;
        date = new Date();
        try {
            String stringDate = "31/12/1998 15:21:54";
            date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(stringDate);
            p.setHeureDepart(date);
            heureDepart = date;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        nbLivreurs = 15;
        p.setNbLivreurs(nbLivreurs);

        chemins = new ArrayList<>();
        chemins.add(new Chemin(l1,l2,150));

        Tournee tournee1 = new Tournee(listelivraisons,chemins,date, ListeLivreurs.livreurs[0]);
        tournees.add(tournee1);
        p.addTournee(tournee1);
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
        assertEquals(p.getTournees(),tournees);
    }

    @Test
    void setTournees() {
        Tournee tournee1 = new Tournee(listelivraisons,chemins,date, ListeLivreurs.livreurs[1]);
        ArrayList<Tournee> listeTournees = new ArrayList<>();
        listeTournees.add(tournee1);
        tournees = listeTournees;
        p.setTournees(listeTournees);
        assertEquals(p.getTournees(),tournees);
    }

    @Test
    void addTournee() {
        Tournee tournee1 = new Tournee(listelivraisons,chemins,date, ListeLivreurs.livreurs[1]);
        tournees.add(tournee1);
        p.addTournee(tournee1);
        assertEquals(p.getTournees(),tournees);
    }

    @Test
    void removeTournee() {
        Tournee tournee1 = new Tournee(listelivraisons,chemins,date, ListeLivreurs.livreurs[0]);
        tournees.remove(tournee1);
        p.removeTournee(tournee1);
        assertEquals(p.getTournees(),tournees);
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
        Tournee tournee1 = new Tournee(listelivraisons,chemins,date, ListeLivreurs.livreurs[1]);
        p.addTournee(tournee1);
        assertEquals(p.getTourneeParLivreur(ListeLivreurs.livreurs[1]),tournee1);
    }

    @Test
    void getLivreursEnCours() {
        Tournee tournee1 = new Tournee(listelivraisons,chemins,date, ListeLivreurs.livreurs[1]);
        Tournee tournee2 = new Tournee(listelivraisons,chemins,date, ListeLivreurs.livreurs[2]);
        ArrayList<Tournee> listeTournees = new ArrayList<>();
        listeTournees.add(tournee1);
        listeTournees.add(tournee2);
        tournees = listeTournees;
        p.setTournees(listeTournees);
        ArrayList<Livreur> livreursCourants = new ArrayList<>();
        livreursCourants.add(ListeLivreurs.livreurs[1]);
        livreursCourants.add(ListeLivreurs.livreurs[2]);
        assertEquals(p.getLivreursEnCours(),livreursCourants);
    }

    @Test
    void getTourneeParLivraison() {
        Tournee tournee1 = new Tournee(listelivraisons,chemins,date, ListeLivreurs.livreurs[2]);
        ArrayList<Tournee> listeTournees = new ArrayList<>();
        listeTournees.add(tournee1);
        tournees = listeTournees;
        p.setTournees(listeTournees);
        assertEquals(p.getTourneeParLivraison(listelivraisons.get(0)),tournee1);
    }

    @Test
    void getLivraisonsUrgentes() {
        this.addLivraisonUrgente();
        assertEquals(p.getLivraisonsUrgentes(),livraisonsUrgentes);
    }

    @Test
    void addLivraisonUrgente() {
        Noeud n1 = new Noeud(205,1.214,2.02);
        Livraison l1 = new Livraison(n1,154);
        livraisonsUrgentes.put(l1.getNoeud().getId(),l1);
        p.addLivraisonUrgente(l1);

        assertEquals(p.getLivraisonsUrgentes(),livraisonsUrgentes);
    }

    @Test
    void setLivraisons() {
        Noeud n1 = new Noeud(249,1.46,1.15);
        Noeud n2 = new Noeud(105,2.46,2.461);

        Livraison l1 = new Livraison(n1,201);
        Livraison l2 = new Livraison(n2,311);
        livraisons.clear();
        livraisons.put(l1.getNoeud().getId(), l1);
        livraisons.put(l2.getNoeud().getId(), l2);

        p.setLivraisons(livraisons);
        assertEquals(p.getLivraisons(), livraisons);
    }

}