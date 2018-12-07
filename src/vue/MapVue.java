package vue;

import controleur.Controler;
import modele.*;
import utils.ListeLivreurs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.LinkedBlockingDeque;
import java.awt.BasicStroke;

public class MapVue extends JPanel {


    private Controler controler;
    private Plan resizePlan;
    private Queue<Noeud> hoveredNodes;
    private ArrayList<Noeud> deletedNodes;
    private final static int WIDTH_DOT = 10;
    private final static int PADDING = 10;

    private final Color[] colors = {Color.GREEN,Color.ORANGE,Color.RED,Color.YELLOW,Color.WHITE,Color.PINK,Color.CYAN,Color.BLUE};
    private double zoom;
    private static final double ZOOM_MAX = 2;
    private static final double ZOOM_MIN = 1;
    private Rectangle zoomArea;

    public MapVue(){
        hoveredNodes = new LinkedBlockingDeque<>();
        deletedNodes = new ArrayList<>();
        zoom = ZOOM_MIN;
        zoomArea = new Rectangle(0,0,getWidth(),getHeight());
    }

    double phi = Math.toRadians(40);
    int barb = 10;

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
                            }else if(tournee.getLivraisons().contains(resizePlan.getLivraisons().get(end_tournee.getId()))){
                                drawNode(new Point((int)end_tournee.getLongitude(),(int)end_tournee.getLatitude()),g);
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
                g.setColor(Color.BLACK);
                Point p = new Point((int)resizePlan.getEntrepot().getNoeud().getLongitude(),(int)resizePlan.getEntrepot().getNoeud().getLatitude());
                drawNode(p,g);
            }
            if(deletedNodes!= null){
                for(Noeud n : deletedNodes){
                    g.setColor(Color.gray);
                    drawNode(new Point((int)n.getLongitude(),(int)n.getLatitude()),g);
                }
            }
            g.setColor(Color.yellow);
            while(!hoveredNodes.isEmpty())
            {
                Noeud hoveredNode = hoveredNodes.poll();
                Point p = new Point((int)hoveredNode.getLongitude(),(int)hoveredNode.getLatitude());
                drawNode(p,g);
            }


        }
    }

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

        double ratioMin = ratioLong < ratioLat ? ratioLong : ratioLat;


        for (Noeud n : controler.getPlan().getNoeuds().values()){
            double newlatitude = (n.getLatitude()-minLatPlan)*ratioMin + PADDING;
            double newLongitude = (n.getLongitude()-minLongPlan)*ratioMin + PADDING;
            this.resizePlan.addNoeud(new Noeud(n.getId(),newlatitude,newLongitude));
        }

        long originID;
        long destinationID;

        Noeud newOriginTroncon = null;
        Noeud newDestinationTroncon = null;

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
        repaint();

    }

    public void setControler(Controler controler) {
        this.controler = controler;
    }

    public void onMouseMove(Point point) {
        if(resizePlan == null) return;


        Noeud n = getNearestResizedNode(point);
        if(n != null)
        {
            hoveredNodes.add(n);
            controler.onHoverNode(n);
        }

        repaint();

    }

    public void selectNode(Point point, MouseEvent e){
        if(resizePlan == null) return;

        Noeud n = getNearestResizedNode(e.getPoint());
        if(n != null)
        {
            controler.onPressNode(controler.getPlan().getNoeuds().get(n.getId()),e);
        }

        repaint();
    }

    public void tracerTournee(ArrayList<Tournee> tournees) {
        long originID;
        long destinationID;
        Noeud newOriginTroncon = null;
        Noeud newDestinationTroncon = null;
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

    public void deletePoint(Noeud n){
        deletedNodes.add(this.resizePlan.getNoeuds().get(n.getId()));
    }

    public void wheelMovedUp(int wheelRotation) {
        zoom+=0.1;
        if(zoom>ZOOM_MAX) zoom = ZOOM_MAX;
        updateZoomArea();
        controler.setZoom(zoom);
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

    public void wheelMovedDown(int wheelRotation) {
        zoom-=0.1;
        if(zoom<ZOOM_MIN) zoom = ZOOM_MIN;
        updateZoomArea();
        controler.setZoom(zoom);
    }

    public void mouseDragged(Point point) {
        Point oldPosition = controler.getLastDragMousePosition();
        Point newPosition = point;
        Point vectorInZoom = new Point(newPosition.x-oldPosition.x,newPosition.y-oldPosition.y);
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

    private void drawNode(Point p, Graphics g)
    {
        Point pointInZoom = resizedNodeToZoom(p);
        g.fillOval( pointInZoom.x - WIDTH_DOT / 2,  pointInZoom.y - WIDTH_DOT / 2, WIDTH_DOT, WIDTH_DOT);
    }

    private void drawLine(Point p1, Point p2, Graphics g)
    {
        Point start = resizedNodeToZoom(p1);
        Point end = resizedNodeToZoom(p2);
        g.drawLine(start.x, start.y, end.x, end.y);
    }

    private void drawLine(Point p1, Point p2, Graphics g, float epaisseur)
    {
        Point start = resizedNodeToZoom(p1);
        Point end = resizedNodeToZoom(p2);
        ((Graphics2D)g).setStroke(new BasicStroke(epaisseur));
        g.drawLine(start.x, start.y, end.x, end.y);
    }
}
