package controleur.etat;

import controleur.Controler;
import modele.Noeud;
import modele.Plan;
import vue.PopUpMenu;

/**
 * Classe definissant l'etat ou le plan a ete charge dans l'application
 */
public class EtatPlanCharge extends Etat {

    /**
     * construction de l'etat a partir du controleur
     *
     * @param c controleur
     */
    public EtatPlanCharge(Controler c) {
        super(c);
        label = "Plan chargé";
    }

    @Override
    public PopUpMenu getPopUpMenu(Plan plan, Noeud n) {
        return super.getPopUpMenu(plan,n);
    }
}
