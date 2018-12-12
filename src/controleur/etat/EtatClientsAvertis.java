package controleur.etat;
import java.awt.event.KeyEvent;
import controleur.Controler;
import modele.Noeud;
import modele.Plan;
import vue.PopUpMenu;

import javax.swing.*;
import java.awt.*;

public class EtatClientsAvertis extends Etat {
    public EtatClientsAvertis(Controler c) {
        super(c);
        label = "Clients avertis";
    }

    @Override
    public PopUpMenu getPopUpMenu(Plan plan, Noeud n) { // Pourquoi passer plan ?
        PopUpMenu popUpMenu = super.getPopUpMenu(plan,n);
        if(!plan.getLivraisons().containsKey(n.getId()))
        {

            JMenu sectionL = new JMenu("Ajouter à");

            for(long j = 0;j<plan.getNbLivreurs();j++) {
                JMenuItem temp = new JMenuItem("Livreur " + j);
                sectionL.add(temp);
                temp.addActionListener(e->ajouterLivraison(n));
            }

            JMenuItem ctrlz = new JMenuItem("Annuler");
            ctrlz.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,KeyEvent.CTRL_MASK));

            popUpMenu.add(ctrlz);
            popUpMenu.add(sectionL);

            ctrlz.addActionListener(e-> ctrlz());

        }
        else
        {
            super.ajoutInfosLivraisonsToPopUpMenu(popUpMenu, plan, n);
            JMenuItem supprimerLivraison = new JMenuItem("Supprimer une livraison");
            JMenuItem ctrlz = new JMenuItem("Annuler");

            JMenu sectionL = new JMenu("Ajouter à");

            for(int j = 0;j<plan.getNbLivreurs();j++) {
                JMenuItem temp = new JMenuItem("Livreur " + j);
                sectionL.add(temp);
                temp.addActionListener(e->ajouterLivraison(n));
            }
            ctrlz.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,KeyEvent.CTRL_MASK));
            popUpMenu.add(supprimerLivraison);
            popUpMenu.add(sectionL);
            popUpMenu.add(ctrlz);
            supprimerLivraison.addActionListener(e -> supprimerLivraisonApresLancement(n));
            ctrlz.addActionListener(e-> ctrlz());
        }
        return popUpMenu;
    }

    private void supprimerLivraisonApresLancement(Noeud n) {
        controler.supprimerLivraison(n);
    }
    private void ajouterLivraison(Noeud n){
        //controler.revertAjouterLivraison(n);
    }
    public void ctrlz(){
        controler.ctrlZ();
    }
}
