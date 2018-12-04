package controleur.etat;

import controleur.Controler;
import modele.Noeud;
import modele.Plan;
import vue.PopUpMenu;

import javax.swing.*;
import javax.swing.text.Position;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EtatTournesGeneres extends Etat{
    public EtatTournesGeneres(Controler c) {
        super(c);
        label = "Tournés générés";
    }

    @Override
    public PopUpMenu getPopUpMenu(Plan plan, Noeud n) {

        PopUpMenu popUpMenu = super.getPopUpMenu(plan,n);

        if(plan.getLivraisons().containsKey(n.getId()))
        {
            super.ajoutInfosLivraisonsToPopUpMenu(popUpMenu, plan, n);
            JMenuItem menuItem = new JMenuItem("Changer de livreur");
            popUpMenu.add(menuItem);
            menuItem.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                }
            });
        }
        return popUpMenu;
    }
}
