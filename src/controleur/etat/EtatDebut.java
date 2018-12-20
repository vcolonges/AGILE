package controleur.etat;

import controleur.Controler;
import modele.Noeud;
import modele.Plan;
import vue.PopUpMenu;

/**
 * Classe definissant le premier etat de l'application, etat de demarrage ou rien n'a ete fait
 */
public class EtatDebut extends Etat {

    /**
     * construction de l'etat a partir du controleur
     *
     * @param c controleur
     */
    public EtatDebut(Controler c) {
        super(c);
        label = "Début";
    }

    @Override
    public PopUpMenu getPopUpMenu(Plan plan, Noeud n) {
        return super.getPopUpMenu(plan,n);
    }
}
