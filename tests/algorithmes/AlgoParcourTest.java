package algorithmes;

import modele.Chemin;
import modele.Livraison;
import modele.Noeud;
import modele.Troncon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AlgoParcourTest {

    private ArrayList<Livraison> livraisons;
    private ArrayList<Livraison> livraisonsMap;
    private Livraison result;
    private ArrayList<ArrayList<Livraison>> resultMap;

    @BeforeEach
    void AlgoParcourTest()
    {
        Noeud n1 = new Noeud(1,1,2);
        Noeud n2 = new Noeud(2,1,2);
        Noeud n3 = new Noeud(3,1,2);
        Noeud n4 = new Noeud(4,1,2);
        Noeud n5 = new Noeud(5,1,2);
        Troncon t1 = new Troncon(n1, n2, 3, "R1");
        Troncon t2 = new Troncon(n2, n4, 1, "R2");
        Troncon t3 = new Troncon(n3, n2, 1, "R3");
        Troncon t4 = new Troncon(n1, n3, 1, "R4");
        Troncon t5 = new Troncon(n3, n5, 1, "R5");
        Troncon t6 = new Troncon(n1, n5, 1, "R6");
        Troncon t7 = new Troncon(n3, n1, 1, "R4");
        Troncon t8 = new Troncon(n4, n5, 1, "R8");
        Troncon t9 = new Troncon(n5, n3, 1, "R5");

        Livraison livDepart = new Livraison(n1, 1);
        Livraison liv1 = new Livraison(n5, 1);
        Livraison liv2 = new Livraison(n4, 1);

        Chemin c1 = new Chemin(result, liv1, 1);
        Chemin c2 = new Chemin(result, liv2, 3);
        ArrayList<Troncon> al1 = new ArrayList<Troncon>();
        al1.add(t1);
        c1.setTroncons(al1);

        ArrayList<Troncon> al2 = new ArrayList<Troncon>();
        al2.add(t4);
        al2.add(t3);
        al2.add(t2);
        c2.setTroncons(al2);

        result = new Livraison(n1, 1);
        result.getChemins().add(c1);
        result.getChemins().add(c2);

        n1.addTronconAdjacent(t1);
        n1.addTronconAdjacent(t4);
        n1.addTronconAdjacent(t6);

        n2.addTronconAdjacent(t2);

        n3.addTronconAdjacent(t3);
        n3.addTronconAdjacent(t5);
        n3.addTronconAdjacent(t7);

        n4.addTronconAdjacent(t8);

        n5.addTronconAdjacent(t9);
        livraisons = new ArrayList<>();
        livraisons.add(livDepart);
        livraisons.add(liv1);
        livraisons.add(liv2);

        Noeud no1 = new Noeud(1,-2,7);
        Noeud no6 = new Noeud(6,2.3,4);
        Noeud no3 = new Noeud(3,-6,3);
        Noeud no4 = new Noeud(4,2,3);

        Noeud no2 = new Noeud(2,4,6);
        Noeud no5 = new Noeud(5,7,5);
        Noeud no7 = new Noeud(7,4,7);
        Noeud no11 = new Noeud(11,5,3);

        Noeud no8 = new Noeud(8,6,-1);
        Noeud no9 = new Noeud(9,5,-1);
        Noeud no10 = new Noeud(10,5,-2);


        Livraison l1 = new Livraison(no1, 1);
        Livraison l2 = new Livraison(no2, 1);
        Livraison l3 = new Livraison(no3, 1);
        Livraison l4 = new Livraison(no4, 1);
        Livraison l5 = new Livraison(no5, 1);
        Livraison l6 = new Livraison(no6, 1);
        Livraison l7 = new Livraison(no7, 1);
        Livraison l8 = new Livraison(no8, 1);
        Livraison l9 = new Livraison(no9, 1);
        Livraison l10 = new Livraison(no10, 1);
        Livraison l11 = new Livraison(no11, 1);

        livraisonsMap = new ArrayList<>();

        livraisonsMap.add(l1);
        livraisonsMap.add(l2);
        livraisonsMap.add(l3);
        livraisonsMap.add(l4);
        livraisonsMap.add(l5);
        livraisonsMap.add(l8);
        livraisonsMap.add(l9);
        livraisonsMap.add(l6);
        livraisonsMap.add(l7);
        livraisonsMap.add(l10);
        livraisonsMap.add(l11);

        resultMap = new ArrayList<>();
        ArrayList<Livraison> a1 = new ArrayList<>();
        ArrayList<Livraison> a2 = new ArrayList<>();
        ArrayList<Livraison> a3 = new ArrayList<>();

        a1.add(l1);
        a1.add(l3);
        a1.add(l4);
        a1.add(l6);

        a2.add(l9);
        a2.add(l8);
        a2.add(l10);

        a3.add(l5);
        a3.add(l7);
        a3.add(l11);
        a3.add(l2);

        resultMap.add(a1);
        resultMap.add(a2);
        resultMap.add(a3);
    }

    @Test
    void calculCheminTest()
    {
        Livraison livDepart = livraisons.get(0);
        Livraison liv1 = livraisons.get(1);
        Livraison liv2 = livraisons.get(2);

        livDepart.getChemins().addAll(AlgoParcour.calculChemin(livDepart, livraisons));
        assertEquals(result.getCheminVers(liv1).getLongueur(), livDepart.getCheminVers(liv1).getLongueur());
        assertEquals(result.getCheminVers(liv2).getLongueur(), livDepart.getCheminVers(liv2).getLongueur());
    }


    @Test
    void getLivraisonsTest() {
        ArrayList<ArrayList<Livraison>> test = AlgoParcour.getLivraisons(livraisonsMap, 3);
        boolean testEquality=true;
        for(int indexCircle = 0; indexCircle<test.size(); indexCircle++)
        {
            for(int indexLivraison = 0; indexLivraison<test.get(indexCircle).size(); indexLivraison++)
            {
                if (resultMap.get(indexCircle).get(indexLivraison)!=test.get(indexCircle).get(indexLivraison))
                    testEquality=false;

            }
        }
        assertTrue(testEquality);
    }
}
