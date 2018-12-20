package controleur.gestionCommande;

import modele.Livraison;
import controleur.Controler;


/**
 * Classe permettant de représenter l'action de suppresion de commande
 */
public class SupprimerCommande implements Commande {

    private Livraison livraison;
    private Controler controler;

    /**
     * Constructeur, initialisation avec une instance de plan et de controleur
     *
     * @param p plan
     * @param c controler
     * */
    public SupprimerCommande(Livraison l,Controler c){
        this.livraison = l;
        this.controler = c;
    }

    /**
     * Réalise l'action inverse de la commande réalisé
     *
     * */
    public void undo(){
        controler.getPlan().getLivraisons().put(livraison.getNoeud().getId(),livraison);
        controler.revertAjouterLivraison(livraison);
    }


}
