package controleur.gestionCommande;



import java.util.ArrayList;

public class CommandeManager {

    private ArrayList<Commande> commandes;
    private int i;


    public void add(Commande c){
        commandes.add(c);
    }

    public ArrayList<Commande> getCommandes(){
        return commandes;
    }

    public void undo(){
        if(i>= 0){
            commandes.get(i--).undo();
        }
    }
}
