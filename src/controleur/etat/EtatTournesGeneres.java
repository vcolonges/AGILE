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
            popUpMenu.add(menuItem);
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

                    if(name != null && name.length() > 0) {
                        Livraison livraison = plan.getLivraisons().get(n.getId());
                        for(Tournee tournee : plan.getTournees()){
                            if(tournee.getLivraisons().get(0) == livraison){
                                plan.removeTournee(tournee);
                                tournee.removeLivraison(livraison);

                                ThreadTSP t = ThreadTSPFactory.getTSPThread(tournee.getLivraisons(),plan.getEntrepot(),plan.getHeureDepart(), tournee.getLivreur());
                                t.addThreadListener(controler.getEcouteurDeTacheTSP());
                                t.start();
                                break;
                            }
                        }
                        Livreur nouveauLivreur = ListeLivreurs.getLivreurParPrenom(name);
                        Tournee tournee = plan.getTourneeParLivreur(nouveauLivreur);
                        if(tournee != null){
                            plan.removeTournee(tournee);
                            tournee.addLivraison(livraison);

                            ThreadTSP t = ThreadTSPFactory.getTSPThread(tournee.getLivraisons(),plan.getEntrepot(),plan.getHeureDepart(), nouveauLivreur);
                            t.addThreadListener(controler.getEcouteurDeTacheTSP());
                            t.start();
                        }
                    }
                }
            });
        }
        return popUpMenu;
    }
}
