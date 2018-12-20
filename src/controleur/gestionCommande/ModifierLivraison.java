package controleur.gestionCommande;

import modele.Plan;
import controleur.Controler;

public class ModifierLivraison implements Commande {

    private Plan plan;
    private Controler controler;

    public ModifierLivraison(Plan p,Controler c){
        try{
            this.plan = (Plan)p.clone();
            this.controler = c;
        }catch (CloneNotSupportedException e){
            e.printStackTrace();
        }

    }
    public void undo(){
        controler.getPlan().setTournees(plan.getTournees());
        controler.genererTournees();
    }


}
