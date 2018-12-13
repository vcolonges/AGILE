package controleur;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class EcouteurDeSlider implements ChangeListener {

    private Controler controleur;

    public EcouteurDeSlider(Controler controleur) {
        this.controleur = controleur;
    }

    @Override
    public void stateChanged(ChangeEvent e) {

        JSlider jSlider = (JSlider)e.getSource();
        controleur.updateLabelSliderHeure(jSlider.getValue());
        controleur.updateMapVueAvecPositionAt(jSlider.getValue());


    }
}
