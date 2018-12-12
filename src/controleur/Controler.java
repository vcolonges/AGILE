package controleur;


import algorithmes.AlgoParcour;
import com.sun.tools.javac.Main;
import controleur.etat.*;
import controleur.gestionCommande.*;
import exceptions.XMLException;
import modele.*;
import thread.threadsimulation.ThreadSimulation;
import thread.threadtsp.ThreadTSP;
import thread.threadtsp.ThreadTSPFactory;
import utils.ListeLivreurs;
import utils.Paire;
import utils.XMLParser;
import vue.MainVue;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

public class Controler {

    private Plan plan;
    private MainVue mainvue;
    private Etat etat;
    private AlgoParcour algo;
    private Point lastDragMousePosition;
    private EcouteurDeTacheTSP ecouteurDeTacheTSP;
    private EcouteurDeTacheSimulation ecouteurDeTacheSimulation;


    private CommandeManager ctrlZ;
    /**
     * Cree le controleur de l'application
     */
    public Controler(MainVue vue) {
        this.mainvue = vue;
        etat = new EtatDebut(this);
        mainvue.setEtat(etat);
        algo = new AlgoParcour();
        ecouteurDeTacheTSP = new EcouteurDeTacheTSP(this);
        this.ecouteurDeTacheSimulation = new EcouteurDeTacheSimulation(this);
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
                plan = XMLParser.parseTrajets(lienLivraisons, plan);
                mainvue.getMapPanel().loadPlan(plan);
                etat = new EtatLivraisonsCharges(this);
                mainvue.setEtat(etat);
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
        mainvue.supprimerLivraison(n);
        System.out.println(plan.getLivraisons().get(n.getId()));
        ctrlZ.add(new SupprimerCommande(plan.getLivraisons().get(n.getId()),this));
        this.plan.getLivraisons().remove(n.getId()); // Suppression dans la structure de donnée.
    }

    public void revertAjouterLivraison(Livraison l){
        this.plan.getLivraisons().put(l.getNoeud().getId(),l);
        mainvue.revertAjouterLivraison(l);
    }


    public void ctrlZ() {
        ctrlZ.undo();
    }
    public void demarrerTournees() {
        etat = new EtatClientsAvertis(this);
        mainvue.setEtat(etat);

        ThreadSimulation t = new ThreadSimulation(plan.getTournees(),plan.getHeureDepart());
        t.addThreadListener(ecouteurDeTacheSimulation);
        t.start();
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

    public void setZoom(double zoom) {
        mainvue.setZoom((int)(zoom*100.0));
    }

    public void setPlan(Plan p){ this.plan = p;}

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

    public Etat getEtat() {
        return etat;
    }


    public void setMainvue(MainVue v){
        this.mainvue = v;
    }

    public MainVue getMainVue(){
        return this.mainvue;
    }
    public void updateDeliverer(String name,Noeud n,Plan p){
        if(name != null && name.length() > 0) {
            ctrlZ.add(new ModifierLivreur(p,this));
            Livraison livraison = p.getLivraisons().get(n.getId());
            for(Tournee tournee : p.getTournees()){
                if(tournee.getLivraisons().get(0) == livraison){
                    p.removeTournee(tournee);
                    tournee.removeLivraison(livraison);

                    ThreadTSP t = ThreadTSPFactory.getTSPThread(tournee.getLivraisons(),p.getEntrepot(),p.getHeureDepart(), tournee.getLivreur());
                    t.addThreadListener(this.getEcouteurDeTacheTSP());
                    t.start();
                    break;
                }
            }
            Livreur nouveauLivreur = ListeLivreurs.getLivreurParPrenom(name);
            Tournee tournee = p.getTourneeParLivreur(nouveauLivreur);
            if(tournee != null){
                p.removeTournee(tournee);
                tournee.addLivraison(livraison);

                ThreadTSP t = ThreadTSPFactory.getTSPThread(tournee.getLivraisons(),p.getEntrepot(),p.getHeureDepart(), nouveauLivreur);
                t.addThreadListener(this.getEcouteurDeTacheTSP());
                t.start();
            }
        }
    }
}
