package controleur.etat;

import controleur.Controler;
import modele.*;
import thread.threadtsp.ThreadTSP;
import thread.threadtsp.ThreadTSPFactory;
import utils.ListeLivreurs;
import vue.PopUpMenu;

import javax.swing.*;
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
            JMenuItem menuItemAnnuler = new JMenuItem("Annuler");
            popUpMenu.add(menuItem);
            popUpMenu.add(menuItemAnnuler);
            menuItemAnnuler.addActionListener(e-> ctrlz());
            menuItem.addActionListener(new java.awt.event.ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
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
                    controler.updateDeliverer(name,n,plan);

                }
            });
        }
        return popUpMenu;
    }

    public void ctrlz(){
        controler.ctrlZ();
    }
}
