package vue;


import controleur.Controler;
import controleur.EcouteurDeBoutons;
import controleur.EcouteurDeComposant;
import controleur.EcouteurDeSouris;
import controleur.etat.Etat;
import controleur.etat.EtatLivraisonsCharges;
import controleur.etat.EtatPlanCharge;
import controleur.etat.EtatTournesGeneres;
import modele.Noeud;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class MainVue extends JFrame {

    // Intitules des boutons de la fenetre
    public final static String CHARGER_PLAN = "Charger un plan";
    public static final String CHARGER_LIVRAISON = "Charger les livraisons";
    public static final String GENERER_TOURNEES = "Générer les Tournées";
    public static final String DEMARRER_TOURNEES = "Démarrer les Tournées";


    private EcouteurDeBoutons ecouteurDeBoutons;
    private EcouteurDeSouris ecouteurDeSouris;
    private EcouteurDeComposant ecouteurDeComposant;
    private JMenuBar menuBar;
    private MapVue mapPanel;
    private JLabel XPosition;
    private JLabel YPosition;
    private JLabel selectedNode;
    private final JButton genererTournees;
    private final JLabel etatLabel;
    private final JButton demarrerTournees;
    private final JMenuItem chargerPlanXML;
    private final JMenuItem chargerLivraisonXML;
    private JLabel zoomLabel;

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
        mapPanel.addMouseListener(ecouteurDeSouris);
        mapPanel.addMouseMotionListener(ecouteurDeSouris);
        mapPanel.addComponentListener(ecouteurDeComposant);
        mapPanel.addMouseWheelListener(ecouteurDeSouris);

        // Crétion toolPanel
        JPanel toolPanel = new JPanel();
        toolPanel.setLayout(new GridLayout(4,1));
        JPanel nbPersonPanel = new JPanel();
        nbPersonPanel.setLayout(new FlowLayout());
        nbPersonPanel.add(new JLabel("Nombre de livreurs"));

        JComboBox nbLivreurs = new JComboBox();
        nbLivreurs.addActionListener(ecouteurDeBoutons);
        for(int i = 1; i < 16; i++)
            nbLivreurs.addItem(""+i);
        nbLivreurs.setMaximumSize( nbLivreurs.getPreferredSize() );
        nbPersonPanel.add(nbLivreurs);

        JPanel startStimePanel = new JPanel();
        startStimePanel.setLayout(new FlowLayout());

        JComboBox heureDebut = new JComboBox();
        for(int i = 1; i < 25; i++)
            heureDebut.addItem(""+i);
        heureDebut.setMaximumSize( heureDebut.getPreferredSize() );
        startStimePanel.add(new JLabel("Heure de début"));
        startStimePanel.add(heureDebut);

        JComboBox minuteDebut = new JComboBox();
        for(int i = 1; i < 61; i++)
            minuteDebut.addItem(""+i);
        minuteDebut.setMaximumSize( minuteDebut.getPreferredSize() );
        startStimePanel.add(new JLabel("h"));
        startStimePanel.add(minuteDebut);
        startStimePanel.add(new JLabel("min"));

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
        toolPanel.add(startStimePanel);
        toolPanel.add(genererTourneesPanel);
        toolPanel.add(demarrerTourneesPanel);
        this.add(debugPanel,BorderLayout.SOUTH);
        this.add(mapPanel,BorderLayout.CENTER);
        this.add(toolPanel,BorderLayout.EAST);


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

    public void setEtat(Etat etat) {
        etatLabel.setText(etat.getLabel());
        if(etat.getClass() == EtatPlanCharge.class) {
            genererTournees.setEnabled(false);
            demarrerTournees.setEnabled(false);
            chargerLivraisonXML.setEnabled(true);
        }
        else if(etat.getClass() == EtatLivraisonsCharges.class) {
            genererTournees.setEnabled(true);
            demarrerTournees.setEnabled(false);
        }
        else if(etat.getClass() == EtatTournesGeneres.class){
            demarrerTournees.setEnabled(true);
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
}



