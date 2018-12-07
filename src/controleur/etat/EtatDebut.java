package controleur.etat;

import controleur.Controler;
import modele.Noeud;
import modele.Plan;
import vue.PopUpMenu;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EtatDebut extends Etat {

    public EtatDebut(Controler c) {
        super(c);
        label = "Début";
    }

    @Override
    public PopUpMenu getPopUpMenu(Plan plan, Noeud n) {
        PopUpMenu popUpMenu = new PopUpMenu();

        JMenuItem menuItem = new JMenuItem("Ajouter livraison");
        popUpMenu.add(menuItem);
        menuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }
        });
        return popUpMenu;
    }
}
