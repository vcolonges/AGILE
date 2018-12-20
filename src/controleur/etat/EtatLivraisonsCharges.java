package controleur.etat;

import controleur.Controler;
import modele.Noeud;
import modele.Plan;
import vue.PopUpMenu;

/**
 * Classe definissant l'etat ou les livraison ont ete chargees dans l'application
 */
public class EtatLivraisonsCharges extends Etat {

    /**
     * construction de l'etat a partir du controleur
     *
     * @param c controleur
     */
    public EtatLivraisonsCharges(Controler c) {
        super(c);
        label = "Livraisons chargées";
    }

    @Override
    public PopUpMenu getPopUpMenu(Plan plan, Noeud n) {
        return super.getPopUpMenu(plan,n);
    }
}
