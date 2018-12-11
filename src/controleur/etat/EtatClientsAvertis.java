package controleur.etat;

import controleur.Controler;
import modele.Noeud;
import modele.Plan;
import vue.PopUpMenu;

import javax.swing.*;

public class EtatClientsAvertis extends Etat {
    public EtatClientsAvertis(Controler c) {
        super(c);
        label = "Clients avertis";
    }

    @Override
    public PopUpMenu getPopUpMenu(Plan plan, Noeud n) {
        PopUpMenu popUpMenu = super.getPopUpMenu(plan,n);
        if(!plan.getLivraisons().containsKey(n.getId()))
        {
            JMenuItem menuItem = new JMenuItem("Ajouter une livraison");
            popUpMenu.add(menuItem);
            menuItem.addActionListener(e -> ajouterLivraisonApresLancement(n));
        }
        else
        {
            super.ajoutInfosLivraisonsToPopUpMenu(popUpMenu, plan, n);
            JMenuItem menuItem = new JMenuItem("Supprimer une livraison");
            popUpMenu.add(menuItem);
            menuItem.addActionListener(e -> supprimerLivraisonApresLancement(n));
        }
        return popUpMenu;
    }

    private void supprimerLivraisonApresLancement(Noeud n) {
        controler.supprimerLivraison(n);
    }

    public void ajouterLivraisonApresLancement(Noeud n){
        boolean good = false;
        int duree = 0;
        do {
            try {
                String ret = JOptionPane.showInputDialog("Entrez la durée de livraison", 60);
                if(ret == null)
                    return;
                duree = Integer.parseInt(ret);
                good=true;
            }catch(NumberFormatException e)
            {
                good=false;
            }
        }while(!good);
        controler.ajouterLivraisonUrgente(n,duree);
    }
}
