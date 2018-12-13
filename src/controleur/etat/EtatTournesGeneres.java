package controleur.etat;

import algorithmes.TSP;
import controleur.Controler;
import modele.*;
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

                if(name != null && name.length() > 0) {
                    Livraison livraison = plan.getLivraisons().get(n.getId());

                    Tournee tournee = plan.getTourneeParLivraison(livraison);
                    if(tournee.getLivreur().getPrenom().equals(name)) {
                        if (tournee != null) {
                            plan.removeTournee(tournee);
                            tournee.removeLivraison(livraison);

                            Tournee t1 = TSP.calculerTournee(tournee.getLivraisons(),plan.getEntrepot(),plan.getHeureDepart(),tournee.getLivreur());
                            controler.tourneeGeneree(t1);
                            /*ThreadTSP t = ThreadTSPFactory.getTSPThread(tournee.getLivraisons(), plan.getEntrepot(), plan.getHeureDepart(), tournee.getLivreur());
                            t.addThreadListener(controler.getEcouteurDeTache());
                            t.start();*/
                        }

                        Livreur nouveauLivreur = ListeLivreurs.getLivreurParPrenom(name);
                        tournee = plan.getTourneeParLivreur(nouveauLivreur);
                        if (tournee != null) {
                            plan.removeTournee(tournee);
                            tournee.addLivraison(livraison);

                            Tournee t1 = TSP.calculerTournee(tournee.getLivraisons(),plan.getEntrepot(),plan.getHeureDepart(),tournee.getLivreur());
                            controler.tourneeGeneree(t1);
                            /*ThreadTSP t = ThreadTSPFactory.getTSPThread(tournee.getLivraisons(), plan.getEntrepot(), plan.getHeureDepart(), nouveauLivreur);
                            t.addThreadListener(controler.getEcouteurDeTache());
                            t.start();*/
                        }
                    }
                }
            });
        }
        return popUpMenu;
    }
}
