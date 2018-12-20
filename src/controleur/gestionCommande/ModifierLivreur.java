package controleur.gestionCommande;
import modele.Plan;
import controleur.Controler;

public class ModifierLivreur implements Commande {

    private Plan plan;
    private Controler controler;
    public ModifierLivreur(Plan p, Controler c){
        this.plan = p;
        this.controler = c;
    }

    public void undo(){
        //controler.setPlan(this.plan);
        controler.tourneesGenerees(this.plan.getTournees());
    }
}
