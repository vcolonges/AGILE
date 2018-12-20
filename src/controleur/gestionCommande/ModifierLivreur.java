package controleur.gestionCommande;
import modele.Plan;
import controleur.Controler;


/**
 * Classe permettant de représenter l'action de modification d'un livreur
 */
public class ModifierLivreur implements Commande {

    private Plan plan;
    private Controler controler;

    /**
     * Constructeur, initialisation avec une instance de plan et de controleur
     *
     * @param p plan
     * @param c controler
     * */
    public ModifierLivreur(Plan p, Controler c){
        this.plan = p;
        this.controler = c;
    }

    /**
     * Réalise l'action inverse de la commande réalisé
     *
     * */
    public void undo(){
        controler.tourneesGenerees(this.plan.getTournees());
    }
}
