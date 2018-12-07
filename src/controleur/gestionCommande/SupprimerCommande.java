package controleur.gestionCommande;

import modele.Livraison;
import controleur.Controler;

public class SupprimerCommande implements Commande {

    private Livraison livraison;
    private Controler controler;

    public SupprimerCommande(Livraison l,Controler c){
        this.livraison = l;
        this.controler = c;
    }
    public void undo(){
        controler.getPlan().getLivraisons().put(livraison.getNoeud().getId(),livraison);
        controler.ajouterLivraison(livraison);
    }


}
