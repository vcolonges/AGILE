package controleur;

import vue.MainVue;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class EcouteurDeBoutons implements ActionListener{

    private Controler controler;

    public EcouteurDeBoutons(Controler controleur) {
        this.controler = controleur;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // Methode appelee par l'ecouteur de boutons a chaque fois qu'un bouton est clique
        // Envoi au controleur du message correspondant au bouton clique
        switch (e.getActionCommand()) {
            case MainVue.CHARGER_PLAN:
            case MainVue.CHARGER_LIVRAISON:
                JFileChooser choix = new JFileChooser();
                choix.setCurrentDirectory(new File("/"));
                choix.changeToParentDirectory();
                int retour = choix.showOpenDialog(null);
                if (retour == JFileChooser.APPROVE_OPTION) {
                    // un fichier a été choisi (sortie par OK)
                    // chemin absolu du fichier choisi
                    String lien = choix.getSelectedFile().getAbsolutePath();
                    try {
                        if (e.getActionCommand().equals(MainVue.CHARGER_PLAN))
                            controler.chargerPlan(lien);
                        if (e.getActionCommand().equals(MainVue.CHARGER_LIVRAISON)) {
                            controler.chargerLivraison(lien);
                        }
                    }catch(Exception e1){
                        e1.printStackTrace();
                    }
                }
                break;

            case MainVue.DEMARRER_TOURNEES:
                controler.demarrerTournees();
                break;

            case MainVue.GENERER_TOURNEES:
                //System.out.println("test");
                controler.genererTournees();
                break;

            case "comboBoxChanged":
                if(controler.getPlan() != null) {
                    JComboBox nbLivreurs = (JComboBox) e.getSource();
                    controler.getPlan().setNbLivreurs(Integer.parseInt(nbLivreurs.getSelectedItem().toString()));
                }
                break;
        }
    }
}
