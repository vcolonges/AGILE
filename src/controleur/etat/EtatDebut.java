package controleur.etat;

import controleur.Controler;
import modele.Noeud;
import modele.Plan;
import vue.PopUpMenu;

public class EtatDebut extends Etat {

    public EtatDebut(Controler c) {
        super(c);
        label = "Début";
    }

    @Override
    public PopUpMenu getPopUpMenu(Plan plan, Noeud n) {
        PopUpMenu popUpMenu = super.getPopUpMenu(plan,n);
        return popUpMenu;
    }
}
