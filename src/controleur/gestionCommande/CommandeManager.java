package controleur.gestionCommande;



import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

public class CommandeManager {

    private Deque<Commande> commandes;

    public  CommandeManager(){
        commandes = new ArrayDeque<>();
    }
    public void add(Commande c){
        commandes.push(c);
    }

    public Deque<Commande> getCommandes(){
        return commandes;
    }

    public void undo(){
        if(!commandes.isEmpty()){
            commandes.pop().undo();
        }
    }
}
