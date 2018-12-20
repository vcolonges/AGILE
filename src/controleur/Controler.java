package controleur;


import algorithmes.AlgoLivraisonUrgente;
import algorithmes.AlgoParcour;
import algorithmes.TSP;
import controleur.etat.*;
import controleur.gestionCommande.*;
import exceptions.XMLException;
import modele.*;
import thread.threadtsp.*;
import utils.ListeLivreurs;
import utils.Paire;
import utils.XMLParser;
import vue.MainVue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Classe gerant l'ensemble des entites de l'application
 */
public class Controler {

    private Plan plan;
    private MainVue mainvue;
    private Etat etat;
    private Point lastDragMousePosition;
    private EcouteurDeTacheTSP ecouteurDeTacheTSP;

    private CommandeManager ctrlZ;
    /**
     * Cree le controleur de l'application
     */
    public Controler(MainVue vue) {
        this.mainvue = vue;
        etat = new EtatDebut(this);
        mainvue.setEtat(etat);
        ecouteurDeTacheTSP = new EcouteurDeTacheTSP(this);
        this.ctrlZ = new CommandeManager();
    }

    /**
     * Chargement d'un plan a partir d'un fichier xml passe en parametre
     *
     * @param lienPlan chemin du fichier xml associe au plan
     */
    public void chargerPlan(String lienPlan){
        try {
            plan = XMLParser.parsePlan(lienPlan);
            mainvue.getMapPanel().loadPlan(plan);
            etat = new EtatPlanCharge(this);
            mainvue.setEtat(etat);
        } catch (XMLException e) {
            mainvue.getMapPanel().loadPlan(plan);
            mainvue.errorMessage(e.getMessage());
        }
    }

    /**
     * Chargement des livraisons a partir d'un fichier xml
     *
     * @param lienLivraisons ficheir xml des livraisons
     */
    public void chargerLivraison(String lienLivraisons){
        if(plan == null)
            mainvue.errorMessage("Veuillez charger un plan avant de charger des livraisons.");
        else{
            try {
                plan.getLivraisons().clear();
                plan = XMLParser.parseLivraisons(lienLivraisons, plan);
                mainvue.getMapPanel().loadPlan(plan);
                etat = new EtatLivraisonsCharges(this);
                mainvue.setEtat(etat);
                mainvue.setLabelHeureDepart(plan.getHeureDepart());
            } catch (XMLException e) {
                e.printStackTrace();
                mainvue.errorMessage(e.getMessage());
            }
        }
    }

    public void mouseMoved(Point point) {
        mainvue.updateMousePosition(point);
    }

    public void onPressNode(Noeud n, MouseEvent e) {
        mainvue.displayMenuNode(n,e,etat.getPopUpMenu(plan,n));
    }

    public void resizeMap() {
        mainvue.resizeMap();
    }

    /**
     * Renvoie l'instance du plan utilisee
     *
     * @return plan
     */
    public Plan getPlan() {
        return plan;
    }

    public void mousePressed(Point point, MouseEvent e) {
        mainvue.mousePressed(point,e);
    }

    /**
     * Genere l'ensemble des tournees en utilisant le TSP
     */
    public void genererTournees() {
        etat = new EtatTournesGeneres(this);
        mainvue.setEtat(etat);
        ArrayList<Livraison> livraisons = new ArrayList<>(plan.getLivraisons().values());
        ThreadTSP tsp = ThreadTSPFactory.getTSPThread(livraisons,plan.getNbLivreurs(),plan.getEntrepot(),plan.getHeureDepart());
        tsp.addThreadListener(ecouteurDeTacheTSP);
        tsp.start();
    }

    /**
     * Rend une livraison "inactive" la livraison a ete supprimee mais la tournee n'est pas regeneree, le livreur passera donc tout de meme par le point de livraison.
     * La lviraison est maintenue a l'ecran afin de garder une vision d'ensemble des evenements passes.
     *
     * @param n noeud a rendre "inactif"
     */
    public void supprimerLivraison(Noeud n){

        mainvue.supprimerLivraison(n);
        ctrlZ.add(new SupprimerCommande(new Livraison(plan.getLivraisons().get(n.getId())),this));
        this.plan.getLivraisons().remove(n.getId());
    }

    /**
     * Permet d'annuler l'ajout d'un point de livraison
     *
     * @param l livraison a retirer
     */
    public void revertAjouterLivraison(Livraison l){
        this.plan.getLivraisons().put(l.getNoeud().getId(),l);
        mainvue.revertAjouterLivraison(l);
    }

    /**
     * Passage dans l'etat ou les clients sont avertis et depart des livreurs
     */
    public void demarrerTournees() {
        etat = new EtatClientsAvertis(this);
        mainvue.setEtat(etat);
    }

    /**
     * Dépile une commande après l'avoir annuler via un undo
     */
    public void ctrlZ(){
        if(!ctrlZ.getCommandes().isEmpty()) {
            ctrlZ.undo();
        }
    }

    public Point getLastDragMousePosition() {
        return lastDragMousePosition;
    }

    public void setLastDragMousePosition(Point lastDragMousePosition) {
        this.lastDragMousePosition = lastDragMousePosition;
    }

    public void wheelMovedUp(int wheelRotation) {
        mainvue.getMapPanel().wheelMovedUp(wheelRotation);
    }

    public void wheelMovedDown(int wheelRotation) {
        mainvue.getMapPanel().wheelMovedDown(wheelRotation);
    }

    public void mouseDragged(Point point) {
        mainvue.mouseDragged(point);
        lastDragMousePosition = point;
    }

    /**
     * Une fois les tournees generees, cette methode permet de les ajouter au plan et de les tracer a l'ecran
     *
     * @param tournees tournees generees a tracer et a ajouter au plan
     */
    public void tourneesGenerees(ArrayList<Tournee> tournees) {
        for (Tournee t : tournees)
            if(t == null){
                mainvue.errorMessage("Generation de tournees impossible pour cette configuration");
                return;
            }

        plan.setTournees(tournees);
        mainvue.getMapPanel().tracerTournee(tournees);
    }

    /**
     * Une fois une tournee generee, cette methode permet de l'jaouter au plan et de la tracer a l'ecran
     *
     * @param tournee tournee generee
     */
    public void tourneeGeneree(Tournee tournee) {
        if(tournee == null){
            return;
        }
        plan.addTournee(tournee);
        mainvue.getMapPanel().tracerTournee(plan.getTournees());
    }

    public EcouteurDeTacheTSP getEcouteurDeTacheTSP() {
        return ecouteurDeTacheTSP;
    }

    /**
     * Appelle la vue pour qu'elle mette a jour la position des Livreurs
     * @param update Contient pour chaque Livreur une Paire avec sa position
     */
    public void updatePositionLivreurs(HashMap<Livreur, Paire<Double, Double>> update) {
        mainvue.updatePositionLivreurs(update);
    }

    /**
     * Appelle la vue pour qu'elle mette a jour le label du slider
     * @param secondes Temps a mettre dans le slider en secondes
     */
    public void updateLabelSliderHeure(int secondes){
        mainvue.updateLabelSliderHeure(secondes);
    }

    /**
     * Appelle la vue pour qu'elle mettre a jour la position des livreurs a l'instant "secondes"
     * @param secondes
     */
    public void updateMapVueAvecPositionAt(int secondes){
        HashMap positionLivreur = new HashMap();
        for(Tournee t : plan.getTournees()){
            if(t.getHeureDepart().getTime() <= new Date((secondes*1000)-(3600*1000)).getTime())
            {
                // -3600*1000 car la date commence à 1h
                Paire<Double,Double> p = t.getPositionAt(new Date((secondes*1000) - (3600*1000)));
                positionLivreur.put(t.getLivreur(),p);
            }

        }
        updatePositionLivreurs(positionLivreur);
    }

    /**
     * Renvoie l'etat courant dans lequel se trouve l'application
     *
     * @return etat
     */
    public Etat getEtat() {
        return etat;
    }

    /**
     * Affiche la légende des livreurs
     */
    public void drawLegende(){
        mainvue.drawLegend(plan);
    }

    /**
     * Met a jour le nombre de Livreurs dans le Plan
     * @param value Nombre de Livreurs
     */
    public void updateNbLivreur(int value) {
        plan.setNbLivreurs(value);
    }

    /**
     * Calcul le chemin le plus court pour l'ajout d'une livraison et defini a quel livreur celle-ci s'ajoute en lui ajoutant
     *
     * @param n noeud de livraison a ajouter
     * @param duree duree sur place
     */
    public void ajouterLivraisonUrgente(Noeud n, int duree) {

        AlgoLivraisonUrgente algo = new AlgoLivraisonUrgente();
        AlgoParcour algoParcour = new AlgoParcour();
        Livraison livraison = new Livraison(n,duree);
        ctrlZ.add(new AjouterLivraisonUrgente(livraison,this));
        plan.addLivraisonUrgente(livraison);
        Tournee t = algo.modifiTournee(livraison,plan.getLivraisonsUrgentes().values(),plan.getEntrepot(),plan.getTournees(),mainvue.getHeureSlider(),plan.getNbLivreurs());
        if(!plan.getTournees().contains(t))
            plan.addTournee(t);
        mainvue.getMapPanel().tracerTournee(plan.getTournees());
    }


    /**
     * Réalise la réaffectation d'un point de livraison à un nouveau livreur
     * @param p instance actuel du plan sans la modification
     * @param n noeud de livraison a modifier
     * @param name nom du livreur
     */
    public void modifierLivraisonGeneree(Plan p ,Noeud n ,String name){

        if(name != null && name.length() > 0) {


            ctrlZ.add(new ModifierLivraison(new Plan(p),this));
            Livraison livraison = p.getLivraisons().get(n.getId());

            Tournee tournee = p.getTourneeParLivraison(livraison);
            if(!tournee.getLivreur().getPrenom().equals(name)) {
                if (tournee != null) {
                    p.removeTournee(tournee);
                    tournee.removeLivraison(livraison);
                    Tournee t1 = TSP.calculerTournee(tournee.getLivraisons(),p.getEntrepot(),p.getHeureDepart(),tournee.getLivreur());
                    tourneeGeneree(t1);
                }

                Livreur nouveauLivreur = ListeLivreurs.getLivreurParPrenom(name);
                tournee = p.getTourneeParLivreur(nouveauLivreur);
                if (tournee != null) {
                    p.removeTournee(tournee);
                    tournee.addLivraison(livraison);
                    Tournee t1 = TSP.calculerTournee(tournee.getLivraisons(),p.getEntrepot(),p.getHeureDepart(),tournee.getLivreur());
                    tourneeGeneree(t1);
                }
            }
        }
    }

    /**
     * Nettoi la pile de commande
     */
    public void cleanCtrlz(){

        ctrlZ.clean();
    }

    /**
     * Defini le plan courant de l'application
     *
     * @param p plan
     */
    public void setPlan(Plan p){
        this.plan = p;
    }

    /**
     * Nettoi les noeuds supprimer
     */
    public void cleanDeleteNode(){
        mainvue.cleanDeleteNode();
    }
}
