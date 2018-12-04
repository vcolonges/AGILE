package modele;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

public class Tournee {
    private ArrayList<Livraison> livraisons;
    private ArrayList<Chemin> chemins;
    private Date heureDepart;
    private Date retourEntrepot;
    private static final double VITESSE = 4.17;

    public Tournee(ArrayList<Livraison> livraisons, ArrayList<Chemin> chemins, Date heureDepart)
    {
        this.livraisons = livraisons;
        this.chemins = chemins;
        this.heureDepart = heureDepart;
    }

    public ArrayList<Chemin> getChemins(){
        return this.chemins;
    }

    public void calculeHoraire(){
        Date heure = new Date(heureDepart.getTime());

        for (int i = 0; i < livraisons.size(); i++) {
            double dureeChemin = chemins.get(i).getLongueur()/VITESSE;
            heure.setTime((long) (heure.getTime()+dureeChemin*1000));
            livraisons.get(i).setHeureArrivee(new Date(heure.getTime()));
            heure.setTime(heure.getTime()+livraisons.get(i).getDuree()*1000);
        }

        double dureeChemin = chemins.get(chemins.size()-1).getLongueur()/VITESSE;
        heure.setTime((long) (heure.getTime()+dureeChemin*1000));
        retourEntrepot = new Date(heure.getTime());
    }

    public ArrayList<Livraison> getLivraisons() {
        return livraisons;
    }

    public Date getHeureDepart() {
        return heureDepart;
    }

    @Override
    public String toString() {
        return "Tournee{" +
                "chemins=" + chemins +
                '}';
    }

    public Date getRetourEntrepot() {
        return retourEntrepot;
    }
}
