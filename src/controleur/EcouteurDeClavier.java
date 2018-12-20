package controleur;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;




public class EcouteurDeClavier implements KeyListener {
    /**
     * Construction de l'ecouteur grace au controleur
     *
     * @param controleur controleur
     */
    Controler controler;
    public EcouteurDeClavier(Controler c) {
        this.controler = c;
    }


    public void keyPressed(KeyEvent e) {
        if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Z){
            controler.ctrlZ();
        }

    }

    public void keyReleased(KeyEvent e) {

    }

    public void keyTyped(KeyEvent e) {

    }
}