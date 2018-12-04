package controleur.etat;

import controleur.Controler;
import modele.Noeud;
import modele.Plan;
import vue.PopUpMenu;

import javax.swing.*;
import java.awt.*;


public class Etat {
    protected String label;
    protected Controler  controler;

    Etat(Controler c){this.controler = c;}
    public PopUpMenu getPopUpMenu(Plan plan, Noeud n){
        PopUpMenu popUpMenu = new PopUpMenu();
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label;
        if(!plan.getLivraisons().containsKey(n.getId()))
        {
            label = new JLabel("Adresse : " + n.getLatitude() + " " + n.getLongitude(),SwingConstants.LEFT);
        }
        else
        {
            label = new JLabel("Adresse de livraison : " + n.getLatitude() + " " + n.getLongitude(),SwingConstants.LEFT);
        }
        panel.add(label);
        popUpMenu.add(panel);
        return popUpMenu;
    }

    public String getLabel() {
        return label;
    }

    public PopUpMenu ajoutInfosLivraisonsToPopUpMenu(PopUpMenu popUpMenu){
        JPanel panelArrivee = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel heureArrivee = new JLabel("Heure d'arrivee sur place : ");
        panelArrivee.add(heureArrivee);

        JPanel panelDuree = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel heureDuree = new JLabel("Duree sur place : ");
        panelDuree.add(heureDuree);

        JPanel panelDepart = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel heureDepart = new JLabel("Heure de depart : ");
        panelDepart.add(heureDepart);

        popUpMenu.add(panelArrivee);
        popUpMenu.add(panelDuree);
        popUpMenu.add(panelDepart);
        return popUpMenu;
    }
}
