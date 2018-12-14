package algorithmes;

import modele.Chemin;
import modele.Livraison;
import modele.Livreur;
import modele.Tournee;
import utils.ListeLivreurs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/*
 * Cette classe sert à résoudre le problème du voyageur de commerce pour un groupe de livraison
 * Elle crée les Tournee décrivant les solutions optimales
 */


public class TSP {
    static private ArrayList<Livraison> livraisons;
    static private int nbLivraisons;
    static private int nbEnsemble;
    static private double[][] cout; // retiens la distance entre 2 Livraisons
    static private double[][] memD; // retiens la distance de la plus courte solution du TSP partant d'une livraison et passant par un ensemble de livraison
    static private int[][] memNext; // retiens la livraison suivante dans la solution optimale à partir d'une livraison et passant par un ensemble de livraison


    // Transforme une collection de Livraison en un ensemble stocké en int à partir de la liste originale de livraison
    private static int arrayListToInt(Collection<Livraison> list){
        int sum = 0;

        for (Livraison item: list) {
            // Pour chaque item de la liste on passe à 1 le bit correspondant dans l'ensemble
            sum += Math.pow(2,livraisons.indexOf(item));
        }

        return sum;
    }

    // stocke dans un tableau les distances entre 2 Livraisons
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


                    if (cout[i][j] + d < min){ // si on trouve une meilleure solution
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

    private static void creerListeChemins(ArrayList<Chemin> chemins, ArrayList<Livraison> livraisonsOutput) {
        int s= arrayListToInt(livraisons)-1;
        int i, sommet;
        Livraison depart, arrivee = null;

        sommet=0;
        for (i = 1; i < nbLivraisons; ++i)
        {
            depart = livraisons.get(sommet);
            sommet = memNext[sommet][s];
            arrivee = livraisons.get(sommet);
            chemins.add(depart.getCheminVers(arrivee));
            livraisonsOutput.add(arrivee);
            s = enleveElement(s,sommet);
        }
        depart = arrivee;
        arrivee = livraisons.get(0);
        chemins.add(depart.getCheminVers(arrivee));
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


    public static Tournee calculerTournee(ArrayList<Livraison> livraisonCollection, Livraison entrepot, Date heureDepart, Livreur livreur){
        if(livraisonCollection.isEmpty()) return null;
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

        ArrayList<Chemin> listeChemins = new ArrayList<>();
        ArrayList<Livraison> setLivraisons = new ArrayList<>();
        creerListeChemins(listeChemins, setLivraisons);

        Tournee tournee = new Tournee(setLivraisons,listeChemins, heureDepart, livreur);
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

        int i=0;
        for (ArrayList<Livraison> livraisonTournee: listeGroupeLivraisons){
            Tournee tournee = TSP.calculerTournee(livraisonTournee, entrepot, heureDepart, ListeLivreurs.livreurs[i++]);
            listeTournee.add(tournee);
        }

        return listeTournee;
    }


}