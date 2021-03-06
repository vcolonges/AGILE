package utils;

import modele.Livreur;

import java.awt.*;
import java.util.ArrayList;

/**
 * Classe statique contenant une liste de livreurs factices pour la simulation
 */
public final class ListeLivreurs {

    public static final Livreur[] livreurs = {
            new Livreur("Robert",new Color(230,25,75)),
            new Livreur("Jeanne",new Color(60,180,75)),
            new Livreur("Alfred",new Color(0,130,200)),
            new Livreur("Antoine",new Color(255,225,25)),
            new Livreur("Richard",new Color(70,240,240)),
            new Livreur("Monique",new Color(240,50,230)),
            new Livreur("Charles",new Color(128,0,0)),
            new Livreur("Juliette",new Color(128,128,0)),
            new Livreur("Manon",new Color(0,128,0)),
            new Livreur("Camille",new Color(145,30,180)),
            new Livreur("Mathieu",new Color(0,128,128)),
            new Livreur("Emma",new Color(0,0,128)),
            new Livreur("Julien",new Color(0,204,102)),
            new Livreur("Matheo",new Color(245,130,48)),
            new Livreur("Eloïse",new Color(210,245,60))
    };

    /**
     * Renvoie la liste des prenoms des livreurs
     *
     * @return liste des prenoms
     */
    public static ArrayList<String> getListePrenomsLivreurs(){
        ArrayList<String> prenomsLivreurs = new ArrayList<>();
        for (Livreur livreur : ListeLivreurs.livreurs){
            prenomsLivreurs.add(livreur.getPrenom());
        }
        return prenomsLivreurs;
    }

    /**
     * Renvoie le livreur dont le prenom est nom
     *
     * @param nom prenom du livreur que l'on cherche
     * @return livreur ou null
     */
    public static Livreur getLivreurParPrenom(String nom){
        for (Livreur livreur : ListeLivreurs.livreurs){
            if(livreur.getPrenom().equals(nom))
                return livreur;
        }
        return null;
    }
}