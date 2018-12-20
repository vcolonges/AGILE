package controleur.gestionCommande;



import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

/**
 * Classe permettant de gerer les differentes actions réalisés sur l'application
 */
public class CommandeManager {

    private Deque<Commande> commandes;

    /**
     * Constructeur, instancie une pile pour stocker les commandes
     */
    public  CommandeManager(){
        commandes = new ArrayDeque<>();
    }

    /**
     * Empile une commande
     *
     * @param c commande
     */
    public void add(Commande c){
        commandes.push(c);
    }

    /**
     * Récupère la pile
     */
    public Deque<Commande> getCommandes(){
        return commandes;
    }

    /**
     * Annule la dernière action ajouté et la supprime de la pile
     */
    public void undo(){
        if(!commandes.isEmpty()){
            commandes.pop().undo();
        }
    }

    /**
     * Vide la pile
     */
    public void clean(){
        commandes.clear();
    }
}
