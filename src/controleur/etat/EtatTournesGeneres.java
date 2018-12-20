package controleur.etat;

import algorithmes.TSP;
import controleur.Controler;
import modele.*;
import utils.ListeLivreurs;
import vue.PopUpMenu;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

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
            //JMenuItem ctrlz = new JMenuItem("Annuler");


           // ctrlz.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,KeyEvent.CTRL_MASK));
           // popUpMenu.add(ctrlz);
            popUpMenu.add(menuItem);
            //ctrlz.addActionListener(e-> ctrlz());
            menuItem.addActionListener(evt -> {
                ArrayList<String> nomLivreursEnCours = new ArrayList<>();
                for (Livreur livreur : plan.getLivreursEnCours()){
                    nomLivreursEnCours.add(livreur.getPrenom());
                }
                String name = (String) JOptionPane.showInputDialog(popUpMenu,
                        "Selectionnez le nouveau livreur :",
                        "Changer de livreur",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        nomLivreursEnCours.toArray(),
                        nomLivreursEnCours.get(0));
                this.controler.modifierLivraisonGeneree(plan,n,name);

            });
        }
        else{
            //JMenuItem ctrlz = new JMenuItem("Annuler");
            //ctrlz.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,KeyEvent.CTRL_MASK));
            //popUpMenu.add(ctrlz);
            //ctrlz.addActionListener(e-> ctrlz());
        }
        return popUpMenu;
    }

    /*public void ctrlz() {
        controler.ctrlZ();
    }*/
}
