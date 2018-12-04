package modele;

public class Livreur {
    private String prenom;
    private String couleur;

    public Livreur(String prenom, String couleur) {
        this.prenom = prenom;
        this.couleur = couleur;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getCouleur() {
        return couleur;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }
}
