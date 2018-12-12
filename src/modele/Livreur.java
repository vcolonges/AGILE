package modele;

import java.awt.*;

public class Livreur {

    private String prenom;
    private Color couleur;

    public Livreur(String prenom, Color couleur) {
        this.prenom = prenom;
        this.couleur = couleur;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Color getCouleur() {
        return couleur;
    }

    public void setCouleur(Color couleur) {
        this.couleur = couleur;
    }
}
