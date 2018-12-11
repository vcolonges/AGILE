package controleur;


import algorithmes.AlgoParcour;
import controleur.etat.*;
import exceptions.XMLException;
import modele.*;
import thread.threadtsp.*;
import utils.Paire;
import utils.XMLParser;
import vue.MainVue;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Controler {

    private Plan plan;
    private MainVue mainvue;
    private Etat etat;
    private AlgoParcour algo;
    private Point lastDragMousePosition;
    private EcouteurDeTacheTSP ecouteurDeTacheTSP;

    /**
     * Cree le controleur de l'application
     */
    public Controler(MainVue vue) {
        this.mainvue = vue;
        etat = new EtatDebut(this);
        mainvue.setEtat(etat);
        algo = new AlgoParcour();
        ecouteurDeTacheTSP = new EcouteurDeTacheTSP(this);
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
                plan = XMLParser.parseTrajets(lienLivraisons, plan);
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

    public void onHoverNode(Noeud n)
    {
        mainvue.setSelectedNode(n);
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
        ArrayList<Livraison> livraisons = new ArrayList<>();
        livraisons.addAll(plan.getLivraisons().values());
        ThreadTSP tsp = ThreadTSPFactory.getTSPThread(livraisons,plan.getNbLivreurs(),plan.getEntrepot(),plan.getHeureDepart());
        tsp.addThreadListener(ecouteurDeTacheTSP);
        tsp.start();
    }

    public void supprimerLivraison(Noeud n){

        mainvue.deletePoint(n);
    }
    public void demarrerTournees() {
        etat = new EtatClientsAvertis(this);
        mainvue.setEtat(etat);
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

    public void updatePositionLivreurs(HashMap<Livreur, Paire<Double, Double>> update) {
        mainvue.updatePositionLivreurs(update);
    }

    public void updateLabelSliderHeure(int secondes){
        mainvue.updateLabelSliderHeure(secondes);
    }

    public void updateMapVueAvecPositionAt(int secondes){
        HashMap positionLivreur = new HashMap();
        for(Tournee t : plan.getTournees()){
            // -3600*1000 car la date commence à 1h
            Paire<Double,Double> p = t.getPositionAt(new Date((secondes*1000) - (3600*1000)));
            positionLivreur.put(t.getLivreur(),p);
        }
        updatePositionLivreurs(positionLivreur);
    }

    public Etat getEtat() {
        return etat;
    }

    public void drawLegende(){
        mainvue.drawLegend(plan);
    }

    public void updateNbLivreur(int value) {
        plan.setNbLivreurs(value);
    }
}
