package controleur.etat;

import controleur.Controler;
import modele.*;
import vue.PopUpMenu;
import javax.swing.*;
import java.util.ArrayList;

/**
 * Classe definissant l'etat ou les tournees ont ete generees par l"application
 */
public class EtatTournesGeneres extends Etat{

    /**
     * construction de l'etat a partir du controleur
     *
     * @param c controleur
     */
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
        return popUpMenu;
    }

}
