package vue;


import controleur.*;
import controleur.etat.*;
import modele.Livreur;
import modele.Noeud;
import utils.Paire;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;

public class MainVue extends JFrame {

    // Intitules des boutons de la fenetre
    public final static String CHARGER_PLAN = "Charger un plan";
    public static final String CHARGER_LIVRAISON = "Charger les livraisons";
    public static final String GENERER_TOURNEES = "Générer les Tournées";
    public static final String DEMARRER_TOURNEES = "Démarrer les Tournées";

    public static final int HEURE_DEBUT = 28800;
    public static final int HEURE_FIN = 64800;
    public static final int DIFF_HEURE = 1800;


    private EcouteurDeBoutons ecouteurDeBoutons;
    private EcouteurDeSouris ecouteurDeSouris;
    private EcouteurDeComposant ecouteurDeComposant;
    private EcouteurDeSpinner ecouteurDeSpinner;
    private EcouteurDeSlider ecouteurDeSlider;
    private JMenuBar menuBar;
    private MapVue mapPanel;
    private JSpinner spinnerLivreur;
    private JPanel panelHeureDebut;
    private JLabel labelHeureDepart;
    private final JButton genererTournees;
    private final JButton demarrerTournees;
    private final JMenuItem chargerPlanXML;
    private final JMenuItem chargerLivraisonXML;

    private JSlider sliderHeure;
    private JLabel labelSliderHeure;

    private Controler controler;

    public MainVue(){

        super("Optimod'Lyon");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        ImageIcon imageIcon = new ImageIcon("src\\bike_icon.png");
        setIconImage(imageIcon.getImage());
        // Init map
        mapPanel = new MapVue();

        // Création de la menubar
        menuBar = new JMenuBar();


        // Layout de la fenetre
        BorderLayout mainLayout = new BorderLayout();
        this.setLayout(mainLayout);

        //mapPanel.setBackground(Color.BLUE);

        // Création Debug Panel
        JPanel debugPanel = new JPanel(new BorderLayout());
        sliderHeure = new JSlider(JSlider.HORIZONTAL,HEURE_DEBUT, HEURE_FIN, HEURE_DEBUT);
        Hashtable labelTable = new Hashtable();
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
        ecouteurDeSlider = new EcouteurDeSlider(controler);
        ecouteurDeBoutons = new EcouteurDeBoutons(controler);
        ecouteurDeSouris = new EcouteurDeSouris(controler);
        ecouteurDeComposant = new EcouteurDeComposant(controler);
        ecouteurDeSpinner = new EcouteurDeSpinner(controler);
        mapPanel.addMouseListener(ecouteurDeSouris);
        mapPanel.addMouseMotionListener(ecouteurDeSouris);
        mapPanel.addComponentListener(ecouteurDeComposant);
        mapPanel.addMouseWheelListener(ecouteurDeSouris);
        sliderHeure.addChangeListener(new EcouteurDeSlider(controler));

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

        /*JComboBox nbLivreurs = new JComboBox();
        nbLivreurs.addActionListener(ecouteurDeBoutons);
        for(int i = 1; i < 16; i++)
            nbLivreurs.addItem(""+i);
        nbLivreurs.setMaximumSize( nbLivreurs.getPreferredSize() );
        nbPersonPanel.add(nbLivreurs);*/

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
        JPanel genererTourneesPanel = new JPanel();
        genererTourneesPanel.setLayout(new FlowLayout());
        genererTourneesPanel.add(genererTournees);

        demarrerTournees = new JButton(DEMARRER_TOURNEES);
        demarrerTournees.setEnabled(false);
        demarrerTournees.addActionListener(ecouteurDeBoutons);
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
        this.add(toolPanel,BorderLayout.NORTH);


        //Poptlation de la menubar
        chargerPlanXML = new JMenuItem(CHARGER_PLAN);
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

    public MapVue getMapPanel() {
        return mapPanel;
    }

    public void updateMousePosition(Point point) {
        //XPosition.setText(""+point.x);
        //YPosition.setText(""+point.y);
        mapPanel.onMouseMove(point);
    }

    public void setSelectedNode(Noeud n)
    {
        //selectedNode.setText(n.toString());
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

    public void errorMessage(String message){
        JOptionPane.showMessageDialog(this,message);
    }


    public void mousePressed(Point point, MouseEvent e) {
        mapPanel.selectNode(point,e);
    }

    public void setLabelHeureDepart(Date heureDepart){
        Calendar cal = Calendar.getInstance();
        cal.setTime(heureDepart);
        int heure = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        String strHeureDepart = String.format("%02dh%02d", heure, minute);
        labelHeureDepart.setText(strHeureDepart);
    }

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

    public void deletePoint(Noeud n){
        mapPanel.deletePoint(n);
    }

    public void mouseDragged(Point point) {
        mapPanel.mouseDragged(point);
    }

    public void updatePositionLivreurs(HashMap<Livreur, Paire<Double, Double>> update) {
        mapPanel.updatePositionLivreurs(update);
    }

    public void updateLabelSliderHeure(int secondes){
        labelSliderHeure.setText(secondes/3600+":"+String.format("%02d", secondes%3600/60));
    }
}



