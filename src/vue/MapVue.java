package vue;

import controleur.Controler;
import controleur.etat.EtatClientsAvertis;
import modele.*;
import utils.ListeLivreurs;
import utils.Paire;
import utils.Star;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Queue;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Représente la carte de l'application, pour l'affichage
 */
public class MapVue extends JPanel {


    private Controler controler;
    private Plan resizePlan;
    private Queue<Noeud> hoveredNodes;
    private ArrayList<Noeud> deletedNodes;
    private final static int WIDTH_DOT = 13;
    private final static int PADDING = 10;

    private double zoom;
    private static final double ZOOM_MAX = 2;
    private static final double ZOOM_MIN = 1;
    private Rectangle zoomArea;
    private HashMap<Livreur, Point> positionLivreursOnMap;
    private double ratioPlanResizedPlan;
    private HashMap<Livreur, Paire<Double, Double>> positionLivreursReal;

    /**
     * Creer un Jpanel permettant d'afficher une map
     */
    public MapVue(){
        hoveredNodes = new LinkedBlockingDeque<>();
        deletedNodes = new ArrayList<>();
        zoom = ZOOM_MIN;
        zoomArea = new Rectangle(0,0,getWidth(),getHeight());
    }

    @Override
    protected void paintComponent(Graphics g) {
       // boolean flag=false;
        super.paintComponent(g);

        g.setColor(Color.BLACK);

        if(resizePlan != null) {
            /*for (Noeud n : resizePlan.getNoeuds().values()) {
                drawNode(new Point((int)n.getLongitude(),(int)n.getLatitude()),g);
            }*/
            for (Troncon t : resizePlan.getTroncons()) {
                Point start = new Point((int)t.getOrigine().getLongitude(),(int)t.getOrigine().getLatitude());
                Point stop = new Point((int)t.getDestination().getLongitude(),(int)t.getDestination().getLatitude());
                drawLine(start,stop,g);
            }

            if(!resizePlan.getTournees().isEmpty()){

                for(Tournee tournee : resizePlan.getTournees()) {
                    g.setColor(tournee.getLivreur().getCouleur());
                    for (Chemin chemin : tournee.getChemins()) {
                        for (Troncon troncon : chemin.getTroncons()) {
                            Noeud start_tournee = troncon.getOrigine();
                            Noeud end_tournee = troncon.getDestination();
                            drawLine(new Point((int)start_tournee.getLongitude(),(int)start_tournee.getLatitude()),new Point((int)end_tournee.getLongitude(),(int)end_tournee.getLatitude()),g,3);
                            if(tournee.getLivraisons().contains(resizePlan.getLivraisons().get(start_tournee.getId()))){
                                drawNode(new Point((int)start_tournee.getLongitude(),(int)start_tournee.getLatitude()),g);
                            }else if(tournee.getLivraisons().contains(controler.getPlan().getLivraisonsUrgentes().get(start_tournee.getId()))){
                                drawNode(new Point((int)start_tournee.getLongitude(),(int)start_tournee.getLatitude()),g);
                            }
                        }
                    }
                }
            }else{
                for(Livraison l : resizePlan.getLivraisons().values()){
                    g.setColor(Color.GREEN);
                    Point p = new Point((int)l.getNoeud().getLongitude(),(int)l.getNoeud().getLatitude());
                    drawNode(p,g);
                }
            }

            if(resizePlan.getEntrepot()!=null){
                g.setColor(Color.RED);
                Point p = new Point((int)resizePlan.getEntrepot().getNoeud().getLongitude(),(int)resizePlan.getEntrepot().getNoeud().getLatitude());
                //drawNode(p,g);
                p = resizedNodeToZoom(p);
                Star star = new Star(p.x,p.y,15,6,5);
                Graphics2D g2 = (Graphics2D)g;
                g2.fill(star);
            }
            if(deletedNodes!= null){
                for(Noeud n : deletedNodes){
                    g.setColor(Color.gray);
                    drawNode(new Point((int)n.getLongitude(),(int)n.getLatitude()),g);
                }
            }
            if(controler.getEtat() instanceof EtatClientsAvertis && positionLivreursOnMap != null)
            {
                g.setColor(Color.BLACK);
                for(Point p : positionLivreursOnMap.values())
                {
                    drawNode(p,g);
                }
            }

            g.setColor(Color.YELLOW);
            while(!hoveredNodes.isEmpty())
            {
                Noeud hoveredNode = hoveredNodes.poll();
                Point p = new Point((int)hoveredNode.getLongitude(),(int)hoveredNode.getLatitude());
                drawNode(p,g);
            }


        }
    }

    /**
     * charge un plan a afficher
     * @param p le plan a charger
     */
    public void loadPlan(Plan p)
    {
        updateZoomArea();
        if(p == null) return;
        resizePlan = new Plan();
        controler.getPlan().getMaxLat();
        controler.getPlan().getMaxLong();

        int heightMap = this.getSize().height;
        int widthMap = this.getSize().width;

        double minLatPlan = controler.getPlan().getMinLat();
        double minLongPlan = controler.getPlan().getMinLong();
        double maxLatPlan = controler.getPlan().getMaxLat();
        double maxLongPlan = controler.getPlan().getMaxLong();

        double ratioLong = (widthMap-2*PADDING)/(maxLongPlan-minLongPlan);
        double ratioLat = (heightMap-2*PADDING)/(maxLatPlan-minLatPlan);

        ratioPlanResizedPlan = ratioLong < ratioLat ? ratioLong : ratioLat;


        for (Noeud n : controler.getPlan().getNoeuds().values()){
            double newlatitude = (maxLatPlan-minLatPlan)*ratioPlanResizedPlan-((n.getLatitude()-minLatPlan)* ratioPlanResizedPlan) + PADDING;
            double newLongitude = (n.getLongitude()-minLongPlan)* ratioPlanResizedPlan + PADDING;
            this.resizePlan.addNoeud(new Noeud(n.getId(),newlatitude,newLongitude));
        }

        long originID;
        long destinationID;

        Noeud newOriginTroncon;
        Noeud newDestinationTroncon;

        for(Troncon t : controler.getPlan().getTroncons()){
            originID = t.getOrigine().getId();
            destinationID = t.getDestination().getId();

            newOriginTroncon = this.resizePlan.getNoeuds().get(originID);
            newDestinationTroncon = this.resizePlan.getNoeuds().get(destinationID);

            this.resizePlan.addTroncon(new Troncon(newOriginTroncon,newDestinationTroncon,t.getLongueur(),t.getNomRue()));

        }
        for(Livraison l : controler.getPlan().getLivraisons().values()){
            this.resizePlan.addLivraison(new Livraison(this.resizePlan.getNoeuds().get(l.getNoeud().getId()),l.getDuree()));
        }
        if(controler.getPlan().getEntrepot()!=null)
            this.resizePlan.setEntrepot(new Livraison(this.resizePlan.getNoeuds().get(controler.getPlan().getEntrepot().getNoeud().getId()),0));

        tracerTournee(p.getTournees());

        if(deletedNodes != null) {
            ArrayList<Noeud> newDeletedNodes = new ArrayList<>();
            for (Noeud n : deletedNodes) {
                newDeletedNodes.add(resizePlan.getNoeuds().get(n.getId()));
            }
            deletedNodes = newDeletedNodes;
        }

        if(positionLivreursReal != null)
        {
            HashMap<Livreur,Point> newPositionLivreurOnMap = new HashMap<>();
            for(Map.Entry<Livreur,Paire<Double,Double>> e : positionLivreursReal.entrySet())
            {
                double newLatitude = (e.getValue().getSecond()-minLatPlan)* ratioPlanResizedPlan + PADDING;
                double newLongitude = (e.getValue().getPremier()-minLongPlan)* ratioPlanResizedPlan + PADDING;
                positionLivreursOnMap.put(e.getKey(),new Point((int)newLongitude,(int)newLatitude));
            }
            positionLivreursOnMap = newPositionLivreurOnMap;
        }

        repaint();

    }

    /**
     * Defini le controleur de la carte
     *
     * @param controler controleur
     */
    public void setControler(Controler controler) {
        this.controler = controler;
    }

    /**
     * Methode a appeler au mouvement de la souris
     * @param point la position de la souris
     */
    public void onMouseMove(Point point) {
        if(resizePlan == null) return;


        Noeud n = getNearestResizedNode(point);
        if(n != null)
        {
            Iterator itr = resizePlan.getTournees().listIterator();
            while(itr.hasNext()) {
                Tournee t = (Tournee)itr.next();
                for(Chemin c : t.getChemins()){
                    for(Troncon tr : c.getTroncons()){
                        if(n.equals(tr.getOrigine())||n.equals(tr.getDestination())){
                            Collections.swap(resizePlan.getTournees(),resizePlan.getTournees().indexOf(t),resizePlan.getTournees().size()-1);
                        }
                    }

                }
            }

            hoveredNodes.add(n);
        }

        repaint();

    }

    /**
     * Methode a appeler au clic sur la plan
     * @param e l' evenement du clic
     */
    public void selectNode(MouseEvent e){
        if(resizePlan == null) return;

        Noeud n = getNearestResizedNode(e.getPoint());
        if(n != null)
        {
            controler.onPressNode(controler.getPlan().getNoeuds().get(n.getId()),e);
        }

        repaint();
    }

    /**
     * Met les tournees en parametre a l'echelle du plan de l'ihm et les trace
     * @param tournees tournees a tracer
     */
    public void tracerTournee(ArrayList<Tournee> tournees) {
        long originID;
        long destinationID;
        Noeud newOriginTroncon;
        Noeud newDestinationTroncon;
        ArrayList<Tournee> newTournees = new ArrayList<>();
        for(Tournee tournee : tournees) {
            ArrayList<Chemin> chemins = new ArrayList<>();
            for (Chemin chemin : tournee.getChemins()) {
                Chemin newChemin = new Chemin(chemin.getOrigine(),chemin.getDestination(),chemin.getLongueur());
                ArrayList<Troncon> troncons = new ArrayList<>();
                for (Troncon troncon : chemin.getTroncons()) {
                        originID = troncon.getOrigine().getId();
                        destinationID = troncon.getDestination().getId();

                        newOriginTroncon = this.resizePlan.getNoeuds().get(originID);
                        newDestinationTroncon = this.resizePlan.getNoeuds().get(destinationID);

                        Troncon newTroncon = new Troncon(newOriginTroncon,newDestinationTroncon,troncon.getLongueur(),troncon.getNomRue());
                        troncons.add(newTroncon);
                }
                newChemin.setTroncons(troncons);
                chemins.add(newChemin);
            }
            Tournee newtournee = new Tournee(tournee.getLivraisons(),chemins,tournee.getHeureDepart(),tournee.getLivreur());
            newTournees.add(newtournee);
        }

        resizePlan.setTournees(newTournees);

        controler.drawLegende();
        repaint();
    }

    private Noeud getNearestResizedNode(Point point)
    {
        for(Noeud n : resizePlan.getNoeuds().values())
        {
            Point pointResized = zoomToResizedPoint(new Point(point.x,point.y));
            if(pointResized.x <= n.getLongitude()+WIDTH_DOT/2 && pointResized.x >= n.getLongitude()-WIDTH_DOT/2)
            {
                if(pointResized.y <= n.getLatitude()+WIDTH_DOT/2 && pointResized.y >= n.getLatitude()-WIDTH_DOT/2)
                {
                    return n;
                }
            }
        }
        return null;
    }

    /**
     * Supprime une livraison sur la carte
     * @param n le noeud de la livraison
     */
    public void supprimerLivraison(Noeud n){
        resizePlan.getLivraisons().remove(n.getId()); //Suppression de la livraison dans le resize.

        deletedNodes.add(this.resizePlan.getNoeuds().get(n.getId()));
        repaint();
    }

    /**
     * A appeler au scroll vers le haut
     * @param wheelRotation nombre de tics
     */
    public void wheelMovedUp(int wheelRotation) {
        zoom+=0.1;
        if(zoom>ZOOM_MAX) zoom = ZOOM_MAX;
        updateZoomArea();
    }

    private void updateZoomAreaLocation(int x, int y)
    {
        x = x < 0 ? 0 : x;
        x = x+zoomArea.getWidth() > getWidth() ? getWidth()-(int)zoomArea.getWidth() : x;
        y = y < 0 ? 0 : y;
        y = y+zoomArea.getHeight() > getHeight() ? getHeight()-(int)zoomArea.getHeight() : y;
        zoomArea.setLocation(x,y);
    }

    private void updateZoomArea()
    {
        int widthZoomArea = (int)(getWidth()*(ZOOM_MAX-zoom));
        int heightZoomArea = (int)(getHeight()*(ZOOM_MAX-zoom));
        zoomArea.setSize(new Dimension(widthZoomArea,heightZoomArea));
        updateZoomAreaLocation(zoomArea.x,zoomArea.y);
        repaint();
    }

    /**
     * A appeler au scroll vers le bas
     * @param wheelRotation nombre de tics
     */
    public void wheelMovedDown(int wheelRotation) {
        zoom-=0.1;
        if(zoom<ZOOM_MIN) zoom = ZOOM_MIN;
        updateZoomArea();
    }

    /**
     * A apeler au drag de la souris
     * @param point position actuelle de la souris
     */
    public void mouseDragged(Point point) {
        Point oldPosition = controler.getLastDragMousePosition();
        Point vectorInZoom = new Point(point.x-oldPosition.x, point.y-oldPosition.y);
        Point vectorReal = new Point(-vectorInZoom.x*(this.getWidth()/(int)zoomArea.getWidth()),-vectorInZoom.y*(this.getHeight()/(int)zoomArea.getHeight()));
        Point newOrigineZoomAreaPosition = new Point(zoomArea.getLocation().x+vectorReal.x,zoomArea.getLocation().y+vectorReal.y);
        updateZoomAreaLocation(newOrigineZoomAreaPosition.x,newOrigineZoomAreaPosition.y);
        repaint();
    }

    private Point resizedNodeToZoom(Point point)
    {
        return new Point((int)((point.x-zoomArea.x)*(getWidth()/zoomArea.getWidth())),(int)((point.y-zoomArea.y)*(getHeight()/zoomArea.getHeight())));
    }

    private Point zoomToResizedPoint(Point point)
    {
        return new Point((int)(point.x*(zoomArea.getWidth()/getWidth())+zoomArea.x),(int)(point.y*(zoomArea.getHeight()/getHeight())+zoomArea.y));
    }

    /**
     * Dessine un point avec la taille globale WIDTH_DOT
     *
     * @param p point
     * @param g graphique utilise
     */
    private void drawNode(Point p, Graphics g)
    {
        drawNode(p,g,WIDTH_DOT);
    }

    /**
     * Dessine un point en specifiant sa taille
     *
     * @param p point
     * @param g graphique utilise
     * @param size taille
     */
    private void drawNode(Point p, Graphics g, int size)
    {
        Point pointInZoom = resizedNodeToZoom(p);
        g.fillOval( pointInZoom.x - WIDTH_DOT / 2,  pointInZoom.y - WIDTH_DOT / 2, size, size);
    }

    /**
     * Dessine une ligne d'un point p1 a un point p2
     *
     * @param p1 point
     * @param p2 point
     * @param g graphique  utilise
     */
    private void drawLine(Point p1, Point p2, Graphics g)
    {
        Point start = resizedNodeToZoom(p1);
        Point end = resizedNodeToZoom(p2);
        g.drawLine(start.x, start.y, end.x, end.y);
    }

    /**
     *
     * Dessine une ligne d'un point p1 a un point p2 en specifiant l'epaisseur
     *
     * @param p1 point
     * @param p2 point
     * @param g graphique  utilise
     * @param epaisseur epaisseur
     */
    private void drawLine(Point p1, Point p2, Graphics g, float epaisseur)
    {
        Point start = resizedNodeToZoom(p1);
        Point end = resizedNodeToZoom(p2);
        ((Graphics2D)g).setStroke(new BasicStroke(epaisseur));
        g.drawLine(start.x, start.y, end.x, end.y);
    }

    /**
     * Met a jour la position des livreurs a l'ecran
     * @param update lien entre un livreur et sa postition
     */
    public void updatePositionLivreurs(HashMap<Livreur, Paire<Double, Double>> update) {
        double minLongPlan = controler.getPlan().getMinLong();
        double minLatPlan = controler.getPlan().getMinLat();
        double maxLatplan = controler.getPlan().getMaxLat();
        positionLivreursOnMap = new HashMap<>();
        this.positionLivreursReal = update;
        for(Map.Entry<Livreur,Paire<Double,Double>> e : update.entrySet())
        {
            double newLatitude = (maxLatplan-minLatPlan)*ratioPlanResizedPlan-((e.getValue().getSecond()-minLatPlan)* ratioPlanResizedPlan) + PADDING;
            double newLongitude = (e.getValue().getPremier()-minLongPlan)* ratioPlanResizedPlan + PADDING;
            positionLivreursOnMap.put(e.getKey(),new Point((int)newLongitude,(int)newLatitude));
        }
        repaint();
    }

    /**
     * Annule l'ajotu d'une livraison
     *
     * @param l livraison a supprimer
     */
    public void revertAjouterLivraison(Livraison l){
        resizePlan.getLivraisons().put(l.getNoeud().getId(),l);
        for(int index=0;index<deletedNodes.size();index++){
            if(deletedNodes.get(index).getId()==l.getNoeud().getId()){
                deletedNodes.remove(index);
            }
        }
        repaint();
    }

    public void cleanDeleteNode(){
        deletedNodes.clear();
    }
}
