package controleur.gestionCommande;

import modele.Plan;
import controleur.Controler;

/**
 * Classe permettant de représenter l'action de modification d'une livraison
 */
public class ModifierLivraison implements Commande {

    private Plan plan;
    private Controler controler;

    /**
     * Constructeur, initialisation avec une instance de plan et de controleur
     *
     * @param p plan
     * @param c controler
     * */
    public ModifierLivraison(Plan p,Controler c){
        try{
            this.plan = (Plan)p.clone();
            this.controler = c;
        }catch (CloneNotSupportedException e){
            e.printStackTrace();
        }

    }
    /**
     * Réalise l'action inverse de la commande réalisé
     *
     * */
    public void undo(){
        controler.getPlan().setTournees(plan.getTournees());
        controler.genererTournees();
    }


}
