package algorithmes;

import modele.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TSPTest {

    Tournee resultTournee;
    ArrayList<Livraison> livraisons;
    Livraison entrepot;

    TSPTest(){

        Noeud n0 = new Noeud(0,1,1);
        Noeud n1 = new Noeud(1,2,3);
        Noeud n2 = new Noeud(2,3,4);
        Noeud n3 = new Noeud(3,5,4);
        Noeud n4 = new Noeud(4,5,6);
        Noeud n5 = new Noeud(5,6,7);
        Noeud n6 = new Noeud(6,7,8);
        Noeud n7 = new Noeud(7,8,9);
        Noeud nEntrepot = new Noeud(8,0,0);

        Troncon t0 = new Troncon(n1,n0,1, "R0");
        Troncon t1 = new Troncon(n0,n5,1, "R1");
        Troncon t2 = new Troncon(n5,n6,1, "R2");
        Troncon t3 = new Troncon(n6,n1,1, "R3");
        Troncon t4 = new Troncon(n1,n7,1, "R4");
        Troncon t5 = new Troncon(n7,n3,1, "R5");
        Troncon t6 = new Troncon(n3,n1,1, "R6");
        Troncon t7 = new Troncon(nEntrepot,n3,1, "R7");
        Troncon t8 = new Troncon(nEntrepot,n5,1, "R8");
        Troncon t9 = new Troncon(nEntrepot,n2,1, "R9");
        Troncon t10 = new Troncon(n3,n5,1, "R10");
        Troncon t11 = new Troncon(n2,n4,1, "R11");
        Troncon t12 = new Troncon(n2,n5,1, "R12");
        Troncon t13 = new Troncon(n4,n0,1, "R13");
        Troncon t14 = new Troncon(n3,nEntrepot,1, "R14");
        Troncon t15 = new Troncon(n5,nEntrepot,1, "R15");
        Troncon t16 = new Troncon(n2,nEntrepot,1, "R16");
        Troncon t17 = new Troncon(n5,n3,1, "R17");
        Troncon t18 = new Troncon(n4,n2,2, "R18");
        Troncon t19 = new Troncon(n5,n2,1, "R19");
        Troncon t20 = new Troncon(n0,n4,2, "R20");

        n0.addTronconAdjacent(t20);
        n0.addTronconAdjacent(t1);

        n1.addTronconAdjacent(t0);
        n1.addTronconAdjacent(t4);

        n2.addTronconAdjacent(t11);
        n2.addTronconAdjacent(t12);
        n2.addTronconAdjacent(t16);

        n3.addTronconAdjacent(t6);
        n3.addTronconAdjacent(t10);
        n3.addTronconAdjacent(t14);

        n4.addTronconAdjacent(t13);
        n4.addTronconAdjacent(t18);

        n5.addTronconAdjacent(t2);
        n5.addTronconAdjacent(t15);
        n5.addTronconAdjacent(t17);
        n5.addTronconAdjacent(t19);

        n6.addTronconAdjacent(t3);

        n7.addTronconAdjacent(t5);

        nEntrepot.addTronconAdjacent(t7);
        nEntrepot.addTronconAdjacent(t8);
        nEntrepot.addTronconAdjacent(t9);



        Livraison l0 = new Livraison(n0,50);
        Livraison l1 = new Livraison(n1,60);
        Livraison l2 = new Livraison(n2,40);
        Livraison l3 = new Livraison(n3,30);
        Livraison lE = new Livraison(nEntrepot,0);

        Chemin c1 = new Chemin(l0, l1, 3);
        Chemin c2 = new Chemin(l0, l2, 2);
        Chemin c3 = new Chemin(l0, l3, 2);
        Chemin c4 = new Chemin(l0, lE, 2);
        Chemin c5 = new Chemin(l1, l0, 1);
        Chemin c6 = new Chemin(l1, l2, 3);
        Chemin c7 = new Chemin(l1, l3, 1);
        Chemin c8 = new Chemin(l1, lE, 2);
        Chemin c9 = new Chemin(l2, l0,2);
        Chemin c10 = new Chemin(l2, l1,3);
        Chemin c11 = new Chemin(l2, l3,2);
        Chemin c12 = new Chemin(l2, lE,1);
        Chemin c13 = new Chemin(l3, l0, 2);
        Chemin c14 = new Chemin(l3, l1, 1);
        Chemin c15 = new Chemin(l3, l2, 2);
        Chemin c16 = new Chemin(l3, lE, 1);
        Chemin c17 = new Chemin(lE, l0, 4);
        Chemin c18 = new Chemin(lE, l1, 2);
        Chemin c19 = new Chemin(lE, l2, 1);
        Chemin c20 = new Chemin(lE, l3, 1);


        //n0-n1
        c1.getTroncons().add(t1);
        c1.getTroncons().add(t2);
        c1.getTroncons().add(t3);

        //n0-n2
        c2.getTroncons().add(t1);
        c2.getTroncons().add(t19);

        //n0-n3
        c3.getTroncons().add(t1);
        c3.getTroncons().add(t17);

        //n0-nEntrepot
        c4.getTroncons().add(t1);
        c4.getTroncons().add(t15);

        //n1-n0
        c5.getTroncons().add(t0);

        //n1-n2
        c6.getTroncons().add(t0);
        c6.getTroncons().add(t20);
        c6.getTroncons().add(t18);

        //n1-n3
        c7.getTroncons().add(t4);
        c7.getTroncons().add(t5);

        //n1-nEntrepot
        c8.getTroncons().add(t4);
        c8.getTroncons().add(t5);
        c8.getTroncons().add(t14);

        //n2-n0
        c9.getTroncons().add(t11);
        c9.getTroncons().add(t13);

        //n2-n1
        c10.getTroncons().add(t12);
        c10.getTroncons().add(t2);
        c10.getTroncons().add(t3);

        //n2-n3
        c11.getTroncons().add(t12);
        c11.getTroncons().add(t17);

        //n2-nEntrepot
        c12.getTroncons().add(t16);

        //n3-n0
        c13.getTroncons().add(t19);
        c13.getTroncons().add(t10);
        c13.getTroncons().add(t11);
        c13.getTroncons().add(t13);

        //n3-n1
        c14.getTroncons().add(t6);

        //n3-n2
        c15.getTroncons().add(t10);
        c15.getTroncons().add(t19);

        //n3-nEntrepot
        c16.getTroncons().add(t14);

        //nEntrepot - n0
        c17.getTroncons().add(t8);
        c17.getTroncons().add(t10);
        c17.getTroncons().add(t11);
        c17.getTroncons().add(t13);

        //nEntrepot - n1
        c18.getTroncons().add(t7);
        c18.getTroncons().add(t6);

        //nEntrepot-n2
        c19.getTroncons().add(t9);

        //nEntrepot-n3
        c20.getTroncons().add(t7);

        livraisons = new ArrayList<>();
        livraisons.add(l0);
        livraisons.add(l1);
        livraisons.add(l2);
        livraisons.add(l3);

        entrepot = lE;

        ArrayList<Chemin> testChemin = new ArrayList<>();
        testChemin.add(c20);
        testChemin.add(c14);
        testChemin.add(c5);
        testChemin.add(c2);
        testChemin.add(c12);
        resultTournee = new Tournee(livraisons,testChemin, new Date(), new Livreur("Toto", new Color(0,0,0)));

    }


    @Test
    void calculerLesTourneesTest() {
        Date date = new Date();
        ArrayList<Tournee> test = TSP.calculerLesTournees(livraisons,1,entrepot,date);
        ArrayList<Chemin> result = resultTournee.getChemins();
        boolean testResult = true;
        for(int indexChemin=0; indexChemin<test.get(0).getChemins().size(); indexChemin++)
        {
            Chemin curChemin = test.get(0).getChemins().get(indexChemin);
            if(result.get(indexChemin).getOrigine()!=curChemin.getOrigine() || result.get(indexChemin).getDestination()!=curChemin.getDestination() || result.get(indexChemin).getTroncons().size()!=curChemin.getTroncons().size())
            {
                testResult=false;
            }
        }
        assertTrue(testResult);
    }
}