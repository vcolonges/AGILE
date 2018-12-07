package vue;


import controleur.*;
import controleur.etat.*;
import modele.Livreur;
import modele.Noeud;
import modele.Plan;
import utils.ListeLivreurs;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Calendar;
import java.util.Date;

public class MainVue extends JFrame {

    // Intitules des boutons de la fenetre
    public final static String CHARGER_PLAN = "Charger un plan";
    public static final String CHARGER_LIVRAISON = "Charger les livraisons";
    public static final String GENERER_TOURNEES = "Générer les Tournées";
    public static final String DEMARRER_TOURNEES = "Démarrer les Tournées";


    private EcouteurDeBoutons ecouteurDeBoutons;
    private EcouteurDeSouris ecouteurDeSouris;
    private EcouteurDeComposant ecouteurDeComposant;
    private EcouteurDeSpinner ecouteurDeSpinner;
    private JMenuBar menuBar;
    private MapVue mapPanel;
    private JLabel XPosition;
    private JLabel YPosition;
    private JLabel selectedNode;
    private JSpinner spinnerLivreur;
    private JPanel panelHeureDebut;
    private JLabel labelHeureDepart;
    private final JButton genererTournees;
    private final JLabel etatLabel;
    private final JButton demarrerTournees;
    private final JMenuItem chargerPlanXML;
    private final JMenuItem chargerLivraisonXML;
    private JLabel zoomLabel;
    private JPanel livreursPanel;
    private GridBagConstraints constraints;

    private Controler controler;

    public MainVue(){

        super("Application");

        // Init map
        mapPanel = new MapVue();

        // Création de la menubar
        menuBar = new JMenuBar();


        // Layout de la fenetre
        BorderLayout mainLayout = new BorderLayout();
        this.setLayout(mainLayout);

        //mapPanel.setBackground(Color.BLUE);

        // Ajout panel legende livreurs
        livreursPanel = new JPanel();
        livreursPanel.setBorder(new EmptyBorder(0, 10, 0, 10));
        livreursPanel.setLayout(new GridBagLayout());
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.anchor = GridBagConstraints.WEST;

        // Création Debug Panel
        JPanel debugPanel = new JPanel(new FlowLayout());
        zoomLabel = new JLabel();
        debugPanel.add(zoomLabel);
        debugPanel.add(new JLabel("X:"));
        XPosition = new JLabel();
        debugPanel.add(XPosition);
        debugPanel.add(new JLabel("Y:"));
        YPosition = new JLabel();
        debugPanel.add(YPosition);
        debugPanel.add(new JLabel("Selected node : "));
        selectedNode = new JLabel();
        debugPanel.add(selectedNode);
        debugPanel.add(new JLabel("Etat : "));
        etatLabel = new JLabel();
        debugPanel.add(etatLabel);

        //init controleur
        controler = new Controler(this);
        mapPanel.setControler(controler);

        // Crétion des listener
        ecouteurDeBoutons = new EcouteurDeBoutons(controler);
        ecouteurDeSouris = new EcouteurDeSouris(controler);
        ecouteurDeComposant = new EcouteurDeComposant(controler);
        ecouteurDeSpinner = new EcouteurDeSpinner(controler);
        mapPanel.addMouseListener(ecouteurDeSouris);
        mapPanel.addMouseMotionListener(ecouteurDeSouris);
        mapPanel.addComponentListener(ecouteurDeComposant);
        mapPanel.addMouseWheelListener(ecouteurDeSouris);

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
        this.add(livreursPanel,BorderLayout.EAST);
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
        XPosition.setText(""+point.x);
        YPosition.setText(""+point.y);
        mapPanel.onMouseMove(point);
    }

    public void setSelectedNode(Noeud n)
    {
        selectedNode.setText(n.toString());
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
        etatLabel.setText(etat.getLabel());
        if(etat instanceof EtatPlanCharge) {
            genererTournees.setEnabled(false);
            demarrerTournees.setEnabled(false);
            chargerLivraisonXML.setEnabled(true);
        }else if(etat instanceof EtatLivraisonsCharges) {
            genererTournees.setEnabled(true);
            demarrerTournees.setEnabled(false);
            panelHeureDebut.setVisible(true);
            spinnerLivreur.setEnabled(true);
        }else if(etat instanceof EtatTournesGeneres){
            demarrerTournees.setEnabled(true);
        }else if(etat instanceof EtatClientsAvertis){
            genererTournees.setEnabled(false);
            demarrerTournees.setEnabled(false);
            spinnerLivreur.setEnabled(false);
        }
    }

    public void deletePoint(Noeud n){

        mapPanel.deletePoint(n);
    }

    public void setZoom(int zoom) {
        zoomLabel.setText(zoom+"%");
    }

    public void mouseDragged(Point point) {
        mapPanel.mouseDragged(point);
    }

    public void drawLegend(Plan plan) {
        int i = 0;
        if(plan!=null)
        {
            for (Livreur livreur : plan.getLivreursEnCours()){
                JPanel livreurPan = new JPanel();
                constraints.gridy = i++;
                JLabel nomLivreur = new JLabel(livreur.getPrenom());
                nomLivreur.setBorder(new EmptyBorder(0, 20, 0, 0));
                BufferedImage bImg = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = bImg.createGraphics();

                graphics.setPaint(livreur.getCouleur());
                graphics.fillRect(0, 0, bImg.getWidth(), bImg.getHeight());

                ImageIcon imageIcon = new ImageIcon(bImg);


                livreurPan.add(new JLabel(imageIcon));
                livreurPan.add(nomLivreur);
                livreursPanel.add(livreurPan,constraints);
            }
        }
        validate();
    }
}



