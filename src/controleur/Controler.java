package controleur;


import algorithmes.AlgoParcour;
import algorithmes.TSP;
import controleur.etat.*;
import controleur.gestionCommande.*;
import exceptions.XMLException;
import modele.*;
import utils.XMLParser;
import vue.MainVue;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;


public class Controler {

    private Plan plan;
    private MainVue mainvue;
    private Etat etat;
    private AlgoParcour algo;
    private Point lastDragMousePosition;
    private CommandeManager ctrlZ;
    /**
     * Cree le controleur de l'application
     */
    public Controler(MainVue vue) {
        this.mainvue = vue;
        etat = new EtatDebut(this);
        mainvue.setEtat(etat);
        algo = new AlgoParcour();
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
        ArrayList<Tournee> tournee = TSP.calculerLesTournees(livraisons,plan.getNbLivreurs(),plan.getEntrepot(), plan.getHeureDepart());
        for(Tournee t : tournee){
            System.out.println("\n\nTOURNEE : ");
            for(Chemin c : t.getChemins()){
                System.out.println("\n"+c);
                for(Troncon tc : c.getTroncons()){
                    System.out.println(tc);
                }
            }
        }
        plan.setTournees(tournee);
        mainvue.getMapPanel().tracerTournee(tournee);
    }

    public void supprimerLivraison(Noeud n){
        this.plan.getLivraisons().remove(n.getId()); // Suppression dans la structure de donnée.
        mainvue.supprimerLivraison(n);
        ctrlZ.add(new SupprimerCommande(plan.getLivraisons().get(n.getId()),this));
    }

    public void ajouterLivraison(Livraison l){
        this.plan.getLivraisons().put(l.getNoeud().getId(),l);
        mainvue.ajouterLivraison(l);
    }

    public void ctrlZ(){
        ctrlZ.getCommandes().get(0).undo();
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

    public void setZoom(double zoom) {
        mainvue.setZoom((int)(zoom*100.0));
    }

    public void wheelMovedDown(int wheelRotation) {
        mainvue.getMapPanel().wheelMovedDown(wheelRotation);
    }

    public void mouseDragged(Point point) {
        mainvue.mouseDragged(point);
        lastDragMousePosition = point;
    }
}
