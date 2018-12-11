package controleur.etat;

import controleur.Controler;
import modele.Livraison;
import modele.Noeud;
import modele.Plan;
import modele.Tournee;
import vue.PopUpMenu;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Etat {
    protected String label;
    protected Controler  controler;

    Etat(Controler c){this.controler = c;}

    /**
     * Cette fonction renvoie une popUpMenu avec l'adresse de noeud appelant
     *
     * @param plan plan de l'application
     * @param n noeud appelant (celui sur lequel on veut afficher la popUpMenu)
     * @return la popUpMenu cree
     */
    public PopUpMenu getPopUpMenu(Plan plan, Noeud n){
        PopUpMenu popUpMenu = new PopUpMenu();
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label;
        if(!plan.getLivraisons().containsKey(n.getId()))
        {
            label = new JLabel("Adresse : lat." + n.getLatitude() + " long." + n.getLongitude(),SwingConstants.LEFT);
        }
        else
        {
            label = new JLabel("Adresse de livraison : lat." + n.getLatitude() + " long." + n.getLongitude(),SwingConstants.LEFT);
        }
        panel.add(label);
        popUpMenu.add(panel);
        return popUpMenu;
    }

    public String getLabel() {
        return label;
    }

    /**
     * Cette fonction ajoute la popUpMenu les differentes informations sur la livraison,
     * le livreur, l'heure d'arrivee de celui-ci, sa duree sur place et son heure de depart
     *
     * @param popUpMenu popUpMenu precedemment creee
     * @param p Plan de l'application
     * @param n  Noeud courant (sur lequel la popupmenu est affichee)
     * @return la popUpMenu modifiee
     */
    public PopUpMenu ajoutInfosLivraisonsToPopUpMenu(PopUpMenu popUpMenu, Plan p, Noeud n){
        Livraison livraison = p.getLivraisons().get(n.getId());
        Tournee tournee = p.getTourneeParLivraison(livraison);
        if(tournee != null){
            JPanel panelLivreur = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel livreur = new JLabel("Livreur : " + tournee.getLivreur().getPrenom());
            panelLivreur.add(livreur);
            popUpMenu.add(panelLivreur);
        }


        JPanel panelArrivee = new JPanel(new FlowLayout(FlowLayout.LEFT));
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        JLabel heureArrivee = new JLabel("Heure d'arrivee sur place : " + format.format(livraison.getHeureArrivee()));
        panelArrivee.add(heureArrivee);

        JPanel panelDuree = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel heureDuree = new JLabel("Duree sur place : " + livraison.getDuree()/60 + " min");
        panelDuree.add(heureDuree);

        JPanel panelDepart = new JPanel(new FlowLayout(FlowLayout.LEFT));
        Date valHeureDepart = new Date(livraison.getHeureArrivee().getTime() + livraison.getDuree()*1000);
        JLabel heureDepart = new JLabel("Heure de depart : " + format.format(valHeureDepart));
        panelDepart.add(heureDepart);

        popUpMenu.add(panelArrivee);
        popUpMenu.add(panelDuree);
        popUpMenu.add(panelDepart);
        return popUpMenu;
    }
}
