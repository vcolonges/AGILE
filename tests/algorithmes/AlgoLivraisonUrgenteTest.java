package algorithmes;

import modele.*;
import org.junit.jupiter.api.Test;
import utils.ListeLivreurs;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class AlgoLivraisonUrgenteTest {

    @Test
    void modifieTourneeTest_premiereLivraison(){
        AlgoLivraisonUrgente algoLivraisonUrgente = new AlgoLivraisonUrgente();

        Tournee t1 = new Tournee(null,null,new Date(0),ListeLivreurs.livreurs[0]);
        t1.setRetourEntrepot(new Date(2000));
        Tournee t2 = new Tournee(null,null,new Date(0),ListeLivreurs.livreurs[1]);
        t2.setRetourEntrepot(new Date(1000));
        Tournee t3 = new Tournee(null,null,new Date(0),ListeLivreurs.livreurs[2]);
        t3.setRetourEntrepot(new Date(3000));

        ArrayList<Tournee> tournees = new ArrayList<>();
        tournees.add(t1);
        tournees.add(t2);
        tournees.add(t3);

        Noeud noeudEntrepot = new Noeud(0,0,0);
        Noeud noeudUrgente = new Noeud(1,1,1);

        Troncon tronconEntrepotUrgent = new Troncon(noeudEntrepot,noeudUrgente,0,"");
        Troncon tronconUrgentEntrepot = new Troncon(noeudUrgente,noeudEntrepot,0,"");

        noeudEntrepot.addTronconAdjacent(tronconEntrepotUrgent);
        noeudUrgente.addTronconAdjacent(tronconUrgentEntrepot);

        Livraison entrepot = new Livraison(noeudEntrepot,0);
        Livraison urgente = new Livraison(noeudUrgente,1);


        ArrayList<Livraison> listeLivraison = new ArrayList<>();
        listeLivraison.add(urgente);

        Tournee test = algoLivraisonUrgente.modifiTournee(urgente,listeLivraison,entrepot,tournees,new Date(0), 3);

        assertEquals(2000, test.getRetourEntrepot().getTime());

        //Tournee test = algoLivraisonUrgente.modifiTournee()

    }

    @Test
    void modifieTourneeTest_secondeLivraisonPourMemeLivreur(){
        AlgoLivraisonUrgente algoLivraisonUrgente = new AlgoLivraisonUrgente();

        Tournee t1 = new Tournee(null,null,new Date(0),ListeLivreurs.livreurs[0]);
        t1.setRetourEntrepot(new Date(3000));
        Tournee t2 = new Tournee(null,null,new Date(0),ListeLivreurs.livreurs[1]);
        t2.setRetourEntrepot(new Date(1000));
        Tournee t3 = new Tournee(null,null,new Date(0),ListeLivreurs.livreurs[2]);
        t3.setRetourEntrepot(new Date(4000));

        ArrayList<Tournee> tournees = new ArrayList<>();
        tournees.add(t1);
        tournees.add(t2);
        tournees.add(t3);

        Noeud noeudEntrepot = new Noeud(0,0,0);
        Noeud noeudUrgente = new Noeud(1,1,1);
        Noeud noeudUrgente2 = new Noeud(2,2,2);

        Troncon tronconEntrepotUrgent = new Troncon(noeudEntrepot,noeudUrgente,0,"");
        Troncon tronconUrgentEntrepot = new Troncon(noeudUrgente,noeudEntrepot,0,"");
        Troncon tronconUrgent2Entrepot = new Troncon(noeudUrgente2,noeudEntrepot,0,"");
        Troncon tronconUrgent2Urgent = new Troncon(noeudUrgente2,noeudUrgente,0,"");
        Troncon tronconEntrepotUrgent2 = new Troncon(noeudEntrepot,noeudUrgente2,0,"");
        Troncon tronconUrgentUrgent2 = new Troncon(noeudUrgente,noeudUrgente2,0,"");

        noeudEntrepot.addTronconAdjacent(tronconEntrepotUrgent);
        noeudEntrepot.addTronconAdjacent(tronconEntrepotUrgent2);

        noeudUrgente.addTronconAdjacent(tronconUrgentEntrepot);
        noeudUrgente.addTronconAdjacent(tronconUrgentUrgent2);

        noeudUrgente2.addTronconAdjacent(tronconUrgent2Entrepot);
        noeudUrgente2.addTronconAdjacent(tronconUrgent2Urgent);

        Livraison entrepot = new Livraison(noeudEntrepot,0);
        Livraison urgente = new Livraison(noeudUrgente,1);
        Livraison urgente2 = new Livraison(noeudUrgente2,1);


        ArrayList<Livraison> listeLivraison = new ArrayList<>();
        listeLivraison.add(urgente);
        listeLivraison.add(urgente2);

        ArrayList<Livraison> listeLivraisonPasse = new ArrayList<>();
        listeLivraisonPasse.add(urgente);

        ArrayList<Chemin> listeCheminPasse = new ArrayList<>();
        listeCheminPasse.add(new Chemin(entrepot,urgente,0));
        listeCheminPasse.add(new Chemin(urgente,entrepot,0));

        Tournee urgentePasse = new Tournee(listeLivraisonPasse,listeCheminPasse,new Date(1000),ListeLivreurs.livreurs[1]);
        urgentePasse.setRetourEntrepot(new Date(2000));
        tournees.add(urgentePasse);

        Tournee test = algoLivraisonUrgente.modifiTournee(urgente2,listeLivraison,entrepot,tournees,new Date(0), 3);

        assertEquals(3000, test.getRetourEntrepot().getTime());

        //Tournee test = algoLivraisonUrgente.modifiTournee()

    }

}