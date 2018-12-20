package controleur.gestionCommande;

import controleur.Controler;
import modele.Livraison;
import modele.Plan;

/**
 * Classe permettant de représenter l'action d'ajout d'une livraison urgente
 */
public class AjouterLivraisonUrgente implements Commande {



    private Livraison livraison;
    private Controler controler;

    /**
     * Constructeur, initialisation avec une instance de livraison et de controleur
     *
     * @param l livraison
     * @param c controler
     * */
    public AjouterLivraisonUrgente(Livraison l , Controler c){
            this.livraison = l;
            this.controler = c;
    }

    /**
     * Réalise l'action inverse de la commande réalisé
     *
     * */
    public void undo(){
        controler.revertAjouterLivraison(livraison);
    }
}


