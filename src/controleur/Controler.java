package controleur;


import algorithmes.AlgoLivraisonUrgente;
import algorithmes.AlgoParcour;
import controleur.etat.*;
import controleur.gestionCommande.*;
import exceptions.XMLException;
import modele.*;
import thread.threadtsp.*;
import utils.Paire;
import utils.XMLParser;
import vue.MainVue;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

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

    public Plan getPlan() {
        return plan;
    }

    public void mousePressed(Point point, MouseEvent e) {
        mainvue.mousePressed(point,e);
    }

    public void genererTournees() {
        etat = new EtatTournesGeneres(this);
        mainvue.setEtat(etat);
        ArrayList<Livraison> livraisons = new ArrayList<>(plan.getLivraisons().values());
        ThreadTSP tsp = ThreadTSPFactory.getTSPThread(livraisons,plan.getNbLivreurs(),plan.getEntrepot(),plan.getHeureDepart());
        tsp.addThreadListener(ecouteurDeTacheTSP);
        tsp.start();
    }

    public void supprimerLivraison(Noeud n){

        mainvue.supprimerLivraison(n);
        System.out.println(plan.getLivraisons().get(n.getId()));
        ctrlZ.add(new SupprimerCommande(plan.getLivraisons().get(n.getId()),this));
        this.plan.getLivraisons().remove(n.getId());
    }

    public void revertAjouterLivraison(Livraison l){
        this.plan.getLivraisons().put(l.getNoeud().getId(),l);
        mainvue.revertAjouterLivraison(l);
    }

    public void demarrerTournees() {
        etat = new EtatClientsAvertis(this);
        mainvue.setEtat(etat);
    }

    public void ctrlZ(){

        ctrlZ.undo();
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

    public void tourneesGenerees(ArrayList<Tournee> tournees) {
        plan.setTournees(tournees);
        mainvue.getMapPanel().tracerTournee(tournees);
    }

    public void tourneeGeneree(Tournee tournee) {
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

    public void ajouterLivraisonUrgente(Noeud n, int duree) {
        AlgoLivraisonUrgente algo = new AlgoLivraisonUrgente();
        AlgoParcour algoParcour = new AlgoParcour();
        Livraison livraison = new Livraison(n,duree);
        plan.addLivraisonUrgente(livraison);
        Tournee t = algo.modifiTournee(livraison,plan.getLivraisonsUrgentes().values(),plan.getEntrepot(),plan.getTournees(),mainvue.getHeureSlider(),plan.getNbLivreurs());
        if(!plan.getTournees().contains(t))
            plan.addTournee(t);
        mainvue.getMapPanel().tracerTournee(plan.getTournees());
    }
}
