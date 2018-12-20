package controleur.gestionCommande;

import controleur.Controler;
import modele.Livraison;
import modele.Plan;

public class AjouterLivraisonUrgente implements Commande {



    private Livraison livraison;
    private Controler controler;
    public AjouterLivraisonUrgente(Livraison l , Controler c){
            this.livraison = l;
            this.controler = c;
    }

    public void undo(){
        controler.revertAjouterLivraison(livraison);
        //controler.getPlan().getLivraisonsUrgentes().remove(livraison.getNoeud().getId());
    }
}


