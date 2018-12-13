package controleur;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class EcouteurDeSpinner implements ChangeListener {

    private Controler controleur;

    public EcouteurDeSpinner(Controler controleur) {
        this.controleur = controleur;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if(e.getSource() instanceof JSpinner){
            this.controleur.updateNbLivreur((int)(((JSpinner)e.getSource()).getValue()));
        }
    }
}
