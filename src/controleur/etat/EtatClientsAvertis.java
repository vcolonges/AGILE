package controleur.etat;
import java.awt.event.KeyEvent;
import controleur.Controler;
import modele.Noeud;
import modele.Plan;
import vue.PopUpMenu;

import javax.swing.*;

/**
 * Etat ou les clients ont ete averti de leur livraison et les livreurs sont partis
 */
public class EtatClientsAvertis extends Etat {

    /**
     * construction de l'etat a partir du controleur
     *
     * @param c controleur
     */
    public EtatClientsAvertis(Controler c) {
        super(c);
        label = "Clients avertis";
        c.cleanCtrlz();
    }

    @Override
    public PopUpMenu getPopUpMenu(Plan plan, Noeud n) {
        PopUpMenu popUpMenu = super.getPopUpMenu(plan,n);
        if(!plan.getLivraisons().containsKey(n.getId()) && !plan.getLivraisonsUrgentes().containsKey(n.getId()))
        {
            JMenuItem menuItem = new JMenuItem("Ajouter une livraison");
            //JMenuItem ctrlz = new JMenuItem("Annuler");

            //ctrlz.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,KeyEvent.CTRL_MASK));

            popUpMenu.add(menuItem);
            //popUpMenu.add(ctrlz);
            menuItem.addActionListener(e -> ajouterLivraisonApresLancement(n));
            //ctrlz.addActionListener(e-> ctrlz());
        }
        else
        {
            super.ajoutInfosLivraisonsToPopUpMenu(popUpMenu, plan, n);
            JMenuItem supprimerLivraison = new JMenuItem("Supprimer une livraison");
            //JMenuItem ctrlz = new JMenuItem("Annuler");


            //ctrlz.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,KeyEvent.CTRL_MASK));
            popUpMenu.add(supprimerLivraison);

            //popUpMenu.add(ctrlz);
            supprimerLivraison.addActionListener(e -> supprimerLivraisonApresLancement(n));
            //ctrlz.addActionListener(e-> ctrlz());
        }
        return popUpMenu;
    }

    /**
     * Permet de supprimer une livraison une fois celles-ci demarees
     *
     * @param n livraison a supprimer
     */
    private void supprimerLivraisonApresLancement(Noeud n) {
        controler.supprimerLivraison(n);
    }

    /**
     * Permet d'ajoute une livraison une fois que celles-ci sont demarrees
     *
     * @param n livraison a ajouter
     */
    public void ajouterLivraisonApresLancement(Noeud n){
        boolean good;
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

