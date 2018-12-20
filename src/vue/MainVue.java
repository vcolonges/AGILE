package vue;


import controleur.*;
import controleur.etat.*;
import modele.*;
import utils.Paire;
import controleur.*;
import controleur.etat.*;
import modele.Noeud;
import modele.Livraison;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;

/**
 * Classe representation l'interface de notre application
 */
public class MainVue extends JFrame {

    // Intitules des boutons de la fenetre
    public final static String CHARGER_PLAN = "Charger un plan";
    public static final String CHARGER_LIVRAISON = "Charger les livraisons";
    public static final String GENERER_TOURNEES = "Générer les Tournées";
    public static final String DEMARRER_TOURNEES = "Démarrer les Tournées";

    public static final int HEURE_DEBUT = 28800;
    public static final int HEURE_FIN = 64800;
    public static final int DIFF_HEURE = 1800;


    private MapVue mapPanel;
    private JSpinner spinnerLivreur;
    private JPanel panelHeureDebut;
    private JLabel labelHeureDepart;
    private final JButton genererTournees;
    private final JButton demarrerTournees;
    private final JMenuItem chargerLivraisonXML;

    private JSlider sliderHeure;
    private JLabel labelSliderHeure;
    private JLabel zoomLabel;
    private  JPanel livreursInnerPanel;
    private GridBagConstraints constraints;

    private Controler controler;

    /**
     * Creation de l'ensemble de l'interface
     */
    public MainVue(){

        super("Optimod'Lyon");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        ImageIcon imageIcon = new ImageIcon("src\\bike_icon.png");
        setIconImage(imageIcon.getImage());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // Init map
        mapPanel = new MapVue();

        // Création de la menubar
        JMenuBar menuBar = new JMenuBar();


        // Layout de la fenetre
        BorderLayout mainLayout = new BorderLayout();
        this.setLayout(mainLayout);

        //mapPanel.setBackground(Color.BLUE);

        // Ajout panel legende livreurs
        JPanel livreursPanel = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(livreursPanel);
        scrollPane.setBorder(new EmptyBorder(0, 10, 0, 10));
        livreursInnerPanel = new JPanel();
        //livreursInnerPanel.setBorder(new EmptyBorder(0, 10, 0, 10));
        livreursInnerPanel.setLayout(new GridBagLayout());
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.anchor = GridBagConstraints.WEST;
        livreursPanel.add(livreursInnerPanel,BorderLayout.NORTH);

        // Création Debug Panel
        JPanel debugPanel = new JPanel(new BorderLayout());
        sliderHeure = new JSlider(JSlider.HORIZONTAL,HEURE_DEBUT, HEURE_FIN, HEURE_DEBUT);

        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        for(int i = HEURE_DEBUT ; i<=HEURE_FIN ; i++){
            if(i%DIFF_HEURE==0){
                labelTable.put(i,new JLabel(i/3600+":"+String.format("%02d", i%3600/60)));
            }

        }
        sliderHeure.setLabelTable( labelTable );
        sliderHeure.setMajorTickSpacing(3600);
        sliderHeure.setEnabled(false);
        sliderHeure.setPaintLabels(true);
        labelSliderHeure = new JLabel(28800/3600+":"+String.format("%02d", 28800%3600/60),SwingConstants.CENTER);
        labelSliderHeure.setFont(new Font(Font.DIALOG,  Font.BOLD, 20));
        labelSliderHeure.setEnabled(false);
        debugPanel.add(labelSliderHeure, BorderLayout.CENTER);
        debugPanel.add(sliderHeure, BorderLayout.SOUTH);

        //init controleur
        controler = new Controler(this);
        mapPanel.setControler(controler);

        // Crétion des listener
        EcouteurDeSlider ecouteurDeSlider = new EcouteurDeSlider(controler);
        EcouteurDeBoutons ecouteurDeBoutons = new EcouteurDeBoutons(controler);
        EcouteurDeSouris ecouteurDeSouris = new EcouteurDeSouris(controler);
        EcouteurDeComposant ecouteurDeComposant = new EcouteurDeComposant(controler);
        EcouteurDeSpinner ecouteurDeSpinner = new EcouteurDeSpinner(controler);
        mapPanel.addMouseListener(ecouteurDeSouris);
        mapPanel.addMouseMotionListener(ecouteurDeSouris);
        mapPanel.addComponentListener(ecouteurDeComposant);
        mapPanel.addMouseWheelListener(ecouteurDeSouris);
        sliderHeure.addChangeListener(new EcouteurDeSlider(controler));


        EcouteurDeClavier ecouteurDeClavier = new EcouteurDeClavier(controler);

        addKeyListener(ecouteurDeClavier);

        sliderHeure.addKeyListener(ecouteurDeClavier);

        this.requestFocus();
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        // Crétion toolPanel
        JPanel toolPanel = new JPanel();
        toolPanel.setLayout(new GridLayout(1,4));

        JPanel nbPersonPanel = new JPanel();
        nbPersonPanel.setLayout(new FlowLayout());
        nbPersonPanel.add(new JLabel("Nombre de livreurs"));



        SpinnerModel model = new SpinnerNumberModel(1, 1,15,1);
        spinnerLivreur = new JSpinner(model);
        spinnerLivreur.addChangeListener(ecouteurDeSpinner);
        spinnerLivreur.setEnabled(false);
        nbPersonPanel.add(spinnerLivreur);


        panelHeureDebut = new JPanel();
        panelHeureDebut.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        panelHeureDebut.add(new JLabel("Heure de début : "),gbc);
        labelHeureDepart = new JLabel();
        panelHeureDebut.add(labelHeureDepart,gbc);
        panelHeureDebut.setVisible(false);

        genererTournees = new JButton(GENERER_TOURNEES);
        genererTournees.setEnabled(false);
        genererTournees.addActionListener(ecouteurDeBoutons);
        genererTournees.addKeyListener(ecouteurDeClavier);

        JPanel genererTourneesPanel = new JPanel();
        genererTourneesPanel.setLayout(new FlowLayout());
        genererTourneesPanel.add(genererTournees);

        demarrerTournees = new JButton(DEMARRER_TOURNEES);
        demarrerTournees.setEnabled(false);
        demarrerTournees.addActionListener(ecouteurDeBoutons);
        demarrerTournees.addKeyListener(ecouteurDeClavier);

        JPanel demarrerTourneesPanel = new JPanel();
        demarrerTourneesPanel.setLayout(new FlowLayout());
        demarrerTourneesPanel.add(demarrerTournees);

        // Placement des panels sur la fenetre
        toolPanel.add(nbPersonPanel);
        toolPanel.add(panelHeureDebut);
        toolPanel.add(genererTourneesPanel);
        toolPanel.add(demarrerTourneesPanel);
        this.add(debugPanel,BorderLayout.SOUTH);
        this.add(mapPanel,BorderLayout.CENTER);
        this.add(scrollPane,BorderLayout.EAST);
        this.add(toolPanel,BorderLayout.NORTH);


        //Poptlation de la menubar
        JMenuItem chargerPlanXML = new JMenuItem(CHARGER_PLAN);
        chargerLivraisonXML = new JMenuItem(CHARGER_LIVRAISON);
        chargerLivraisonXML.setEnabled(false);
        chargerPlanXML.addActionListener(ecouteurDeBoutons);
        chargerLivraisonXML.addActionListener(ecouteurDeBoutons);

        JMenu fileMenu = new JMenu("Fichier");
        fileMenu.add(chargerPlanXML);
        fileMenu.add(chargerLivraisonXML);
        menuBar.add(fileMenu);

        this.setJMenuBar(menuBar);

        this.setSize(1200,900);
        this.setVisible(true);

    }

    /**
     * Renvoie l'element graphique contenant la carte
     *
     * @return mappanel
     */
    public MapVue getMapPanel() {
        return mapPanel;
    }

    public void updateMousePosition(Point point) {
        mapPanel.onMouseMove(point);
    }

    public void displayMenuNode(Noeud n, MouseEvent e, PopUpMenu popUpMenu)
    {
        if(popUpMenu != null) {
            popUpMenu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    public void resizeMap() {
        mapPanel.loadPlan(controler.getPlan());
    }

    /**
     * Ouvre une popup contenant un message d'erreur
     *
     * @param message message d'erreur affiche
     */
    public void errorMessage(String message){
        JOptionPane.showMessageDialog(this,message);
    }

    public void mousePressed(Point point, MouseEvent e) {
        mapPanel.selectNode(e);
    }

    /**
     * Assigne "heureDepart" dans le JLabel prevu a cet effet
     * @param heureDepart Heure de Depart des Tournees
     */
    public void setLabelHeureDepart(Date heureDepart){
        Calendar cal = Calendar.getInstance();
        cal.setTime(heureDepart);
        int heure = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        String strHeureDepart = String.format("%02dh%02d", heure, minute);
        labelHeureDepart.setText(strHeureDepart);
    }

    /**
     * En fonction de l'etat de l'application defini les actions possibles ou no nsur l'interface
     *
     * @param etat etat courant
     */
    public void setEtat(Etat etat) {
        if(this.genererTournees==null)return;
        genererTournees.setEnabled(false);
        demarrerTournees.setEnabled(false);
        spinnerLivreur.setEnabled(false);
        chargerLivraisonXML.setEnabled(false);
        sliderHeure.setEnabled(false);
        labelSliderHeure.setEnabled(false);

        if(etat instanceof EtatPlanCharge) {
            chargerLivraisonXML.setEnabled(true);
        }else if(etat instanceof EtatLivraisonsCharges) {
            chargerLivraisonXML.setEnabled(true);
            genererTournees.setEnabled(true);
            panelHeureDebut.setVisible(true);
            spinnerLivreur.setEnabled(true);
            spinnerLivreur.setValue(1);
        }else if(etat instanceof EtatTournesGeneres){
            chargerLivraisonXML.setEnabled(true);
            spinnerLivreur.setEnabled(true);
            genererTournees.setEnabled(true);
            demarrerTournees.setEnabled(true);
        }else if(etat instanceof EtatClientsAvertis){
            chargerLivraisonXML.setEnabled(true);
            sliderHeure.setEnabled(true);
            labelSliderHeure.setEnabled(true);
        }
    }

    /**
     * Supprime une livraison
     *
     * @param n livraison a supprimer
     */
    public void supprimerLivraison(Noeud n){
        mapPanel.supprimerLivraison(n);
    }

    /**
     * Annule l'ajout d'une livraison
     *
     * @param l livraison a supprimer
     */
    public void revertAjouterLivraison(Livraison l){
        mapPanel.revertAjouterLivraison(l);
    }

    public void mouseDragged(Point point) {
        mapPanel.mouseDragged(point);
    }

    public void updatePositionLivreurs(HashMap<Livreur, Paire<Double, Double>> update) {
        mapPanel.updatePositionLivreurs(update);
    }

    /**
     * Met à jour le Label d'affichage de la valeur de Slider
     * @param secondes Valeur du JSlider
     */
    public void updateLabelSliderHeure(int secondes){
        labelSliderHeure.setText(secondes/3600+":"+String.format("%02d", secondes%3600/60));
    }

    /**
     * Affiche la legende des livreurs
     *
     * @param plan Plan de l'application dans lequel se trouve les livreurs
     */
    public void drawLegend(Plan plan) {
        livreursInnerPanel.removeAll();
        int i = 0;
        if(plan!=null)
        {
            for (Tournee tournee : plan.getTournees()){
                Livreur livreur = tournee.getLivreur();
                constraints.gridy = i++;

                JPanel livreurPan = new JPanel();

                SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                JLabel nomLivreur = new JLabel(livreur.getPrenom() + " | Fin : "+format.format(tournee.getRetourEntrepot()));
                nomLivreur.setBorder(new EmptyBorder(0, 20, 0, 0));

                BufferedImage bImg = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = bImg.createGraphics();
                graphics.setPaint(livreur.getCouleur());
                graphics.fillRect(0, 0, bImg.getWidth(), bImg.getHeight());
                ImageIcon imageIcon = new ImageIcon(bImg);

                livreurPan.add(new JLabel(imageIcon));
                livreurPan.add(nomLivreur);
                JPanel livraisonsPanel = new JPanel(new GridLayout(tournee.getLivraisons().size(),1));
                for(Livraison l : tournee.getLivraisons()){
                    JLabel jl = new JLabel("("+(tournee.getLivraisons().indexOf(l)+1)+") - "+format.format(l.getHeureArrivee())+" -> "+format.format(new Date(l.getHeureArrivee().getTime() + l.getDuree()*1000)));
                    livraisonsPanel.add(jl);
                }
                livraisonsPanel.setBorder(new EmptyBorder(0, 10, 10, 10));
                livreursInnerPanel.add(livreurPan,constraints);
                constraints.gridy = i++;
                livreursInnerPanel.add(livraisonsPanel,constraints);
            }
        }
        validate();
    }

    /**
     *
     * @return La Date cree grace a la valeur du JSlider
     */
    public Date getHeureSlider() {
        return new Date(sliderHeure.getValue()*1000);
    }

    public void cleanDeleteNode(){
        mapPanel.cleanDeleteNode();
    }
}



