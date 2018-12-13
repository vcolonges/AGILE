package controleur.etat;

import controleur.Controler;
import modele.Noeud;
import modele.Plan;
import vue.PopUpMenu;

public class EtatPlanCharge extends Etat {

    public EtatPlanCharge(Controler c) {
        super(c);
        label = "Plan chargé";
    }

    @Override
    public PopUpMenu getPopUpMenu(Plan plan, Noeud n) {
        return super.getPopUpMenu(plan,n);
    }
}
