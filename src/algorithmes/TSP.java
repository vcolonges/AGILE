package algorithmes;

import modele.Chemin;
import modele.Livraison;
import modele.Tournee;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

public class TSP {
    static private ArrayList<Livraison> livraisons;
    static private int nbLivraisons;
    static private int nbEnsemble;
    static private double[][] cout;
    static private double[][] memD;
    static private int[][] memNext;


    private static int arrayListToInt(ArrayList<Livraison> list){
        int sum = 0;

        for (Livraison item: list) {
            sum += Math.pow(2,livraisons.indexOf(item));
        }

        return sum;
    }

    private static void creerCout(){
        int i,j;
        for (Livraison livraison : livraisons ) {
            i = livraisons.indexOf(livraison);
            for(Chemin chemin :  livraison.getChemins()){
                j = livraisons.indexOf(chemin.getDestination());
                if(j != -1)
                {
                    cout[i][j] = chemin.getLongueur();
                }
            }

        }
    }


    private static double calculeD(int i, int s){
        // Precondition : estElementDe(i,s) = false et estElement(0,s) = false
        // Postrelation : retourne le cout du plus court chemin partant du sommet i, passant par chaque sommet de s exactement une fois, et terminant sur 0
        if (estVide(s)) return cout[i][0];
        if(memD[i][s] == -1)
        {
            double min = Double.MAX_VALUE;

            for (int j=1; j<nbLivraisons; j++){
                if (estElementDe(j,s)){
                    double d = calculeD(j, enleveElement(s,j));
                    if (cout[i][j] + d < min){
                        memNext[i][s]=j;
                        min = cout[i][j] + d;
                    }
                }
            }
            memD[i][s] = min;
            return min;
        }
        else
        {
            return memD[i][s];
        }
    }

    static private int enleveElement(int s, int e) {
        return (s ^ (1 << (e)));
    }

    static private boolean estElementDe(int e, int s) {
        return (s & (1 << (e))) != 0;
    }

    static private boolean estVide(int s) {
        return s==0;
    }

    private static void afficheOrdre()
    {
        int s= arrayListToInt(livraisons)-1;
        int i, sommet;

        sommet=0;
        System.out.print("Ordre :\n{"+ sommet);
        for (i = 1; i < nbLivraisons; ++i)
        {
            sommet = memNext[sommet][s];
            System.out.print(" ; "+ sommet);
            s = enleveElement(s,sommet);
        }
        System.out.print("}\n\n");
    }

    private static ArrayList<Chemin> creerListeChemins() {
        int s= arrayListToInt(livraisons)-1;
        int i, sommet;
        Livraison depart, arrivee = null;
        ArrayList<Chemin> chemins = new ArrayList<>();

        sommet=0;
        for (i = 1; i < nbLivraisons; ++i)
        {
            depart = livraisons.get(sommet);
            sommet = memNext[sommet][s];
            arrivee = livraisons.get(sommet);
            chemins.add(depart.getCheminVers(arrivee));
            s = enleveElement(s,sommet);
        }
        depart = arrivee;
        arrivee = livraisons.get(0);
        chemins.add(depart.getCheminVers(arrivee));
        return chemins;
    }


    private static void creerMem() {
        int i,j;

        for(i=0; i< nbLivraisons; ++i)
        {
            for(j=0; j<nbEnsemble; ++j)
            {
                memNext[i][j] = -1;
                memD[i][j] = -1;
            }
        }
    }


    public static Tournee calculerTournee(ArrayList<Livraison> livraisonCollection, Livraison entrepot, Date heureDepart){
        livraisons = new ArrayList<>(livraisonCollection);
        livraisons.add(0,entrepot);

        nbLivraisons = livraisons.size();
        nbEnsemble = (int) Math.pow(2,nbLivraisons);

        cout = new double[nbLivraisons][nbLivraisons];
        creerCout();

        memD = new double[nbLivraisons][nbEnsemble];
        memNext = new int[nbLivraisons][nbEnsemble];
        creerMem();

        int ensemble = arrayListToInt(livraisons);

        calculeD(0,ensemble-1);

        //afficheOrdre();

        ArrayList<Chemin> listeChemins = creerListeChemins();
        ArrayList<Livraison> setLivraisons = new ArrayList<>(livraisonCollection);
        Tournee tournee = new Tournee(setLivraisons,listeChemins, heureDepart);
        tournee.calculeHoraire();

        return tournee;
    }

    public static ArrayList<Tournee> calculerLesTournees(ArrayList<Livraison> livraisons, int nbrLivreur, Livraison entrepot, Date heureDepart){
        AlgoParcour algoParcour = new AlgoParcour();
        ArrayList<Livraison> livraisonsEntrepot = new ArrayList<>(livraisons);
        livraisonsEntrepot.add(entrepot);

        for (Livraison depart: livraisons) {
            ArrayList<Chemin> chemins = algoParcour.calculChemin(depart, livraisonsEntrepot);
            depart.getChemins().addAll(chemins);

        }

        ArrayList<Chemin> chemins = algoParcour.calculChemin(entrepot, livraisons);
        entrepot.getChemins().addAll(chemins);

        ArrayList<ArrayList<Livraison>> listeGroupeLivraisons = algoParcour.getLivraisons(livraisons, nbrLivreur);

        ArrayList<Tournee> listeTournee = new ArrayList<>();

        for (ArrayList<Livraison> livraisonTournee: listeGroupeLivraisons){
            Tournee tournee = TSP.calculerTournee(livraisonTournee, entrepot, heureDepart);
            listeTournee.add(tournee);
        }

        return listeTournee;
    }

    /*public static void main(String[] args){
        Noeud noeudl1 = new Noeud(1,1,1);
        Noeud noeudl2 = new Noeud(2,1,1);
        Noeud noeudl3 = new Noeud(3,1,1);
        Noeud noeudEntrepot = new Noeud(0,1,1);

        Livraison l1 = new Livraison(noeudl1, 1);
        Livraison l2 = new Livraison(noeudl2, 2);
        Livraison l3 = new Livraison(noeudl3, 3);
        Livraison entrepot = new Livraison(noeudEntrepot, 0);

        Chemin cheminEntrepotL1 = new Chemin(entrepot,l1,4);
        Chemin cheminEntrepotL2 = new Chemin(entrepot,l2,3);
        Chemin cheminEntrepotL3 = new Chemin(entrepot,l3,2);

        Chemin cheminL1L2 = new Chemin(l1,l2,2);
        Chemin cheminL1L3 = new Chemin(l1,l3,1);
        Chemin cheminL1Entrepot = new Chemin(l1,entrepot,4);

        Chemin cheminL2Entrepot = new Chemin(l2,entrepot,3);
        Chemin cheminL2L1 = new Chemin(l2,l1,1);
        Chemin cheminL2L3 = new Chemin(l2,l3,5);


        Chemin cheminL3Entrepot = new Chemin(l3,entrepot,1);
        Chemin cheminL3L1 = new Chemin(l3,l1,2);
        Chemin cheminL3L2 = new Chemin(l3,l2,5);


        entrepot.getChemins().add(cheminEntrepotL1);
        entrepot.getChemins().add(cheminEntrepotL2);
        entrepot.getChemins().add(cheminEntrepotL3);
        l1.getChemins().add(cheminL1Entrepot);
        l1.getChemins().add(cheminL1L2);
        l1.getChemins().add(cheminL1L3);
        l2.getChemins().add(cheminL2L1);
        l2.getChemins().add(cheminL2L3);
        l2.getChemins().add(cheminL2Entrepot);
        l3.getChemins().add(cheminL3Entrepot);
        l3.getChemins().add(cheminL3L2);
        l3.getChemins().add(cheminL3L1);

        ArrayList<Livraison> livraisons = new ArrayList<>();
        livraisons.add(l1);
        livraisons.add(l2);
        livraisons.add(l3);

        Tournee tournee = calculerTournee(livraisons,entrepot);
    }*/

}
