package modele;

import java.awt.*;

/**
 * Classe définissant l'entite livreur
 */
public class Livreur {

    private String prenom;
    private Color couleur;

    /**
     * Cree l'objet livreur
     *
     * @param prenom prenom du livreur
     * @param couleur couleur associee au livreur (sera affichee sur le plan)
     */
    public Livreur(String prenom, Color couleur) {
        this.prenom = prenom;
        this.couleur = couleur;
    }

    /**
     * Renvoie le prenom du livreur
     *
     * @return prenom
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * Definit prenom comme nouveau prenom pour le livreur
     *
     * @param prenom nouveau prenom du livreur
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * Renvoie la couleur associee au livreur
     *
     * @return couleur
     */
    public Color getCouleur() {
        return couleur;
    }

    /**
     * Definit couleur comme nouvelle couleur du livreur
     *
     * @param couleur Nouvelle couleur du livreur
     */
    public void setCouleur(Color couleur) {
        this.couleur = couleur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Livreur livreur = (Livreur) o;

        if (!prenom.equals(livreur.prenom)) return false;
        return couleur.equals(livreur.couleur);
    }

    @Override
    public int hashCode() {
        int result = prenom.hashCode();
        result = 31 * result + couleur.hashCode();
        return result;
    }
}
