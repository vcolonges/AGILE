package controleur;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;




public class EcouteurDeClavier implements KeyListener {

    Controler controler;
    public EcouteurDeClavier(Controler c) {
        this.controler = c;
    }

    public void keyPressed(KeyEvent e) {
        if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Z){
            controler.ctrlZ();
            System.out.println("here");
        }

    }

    public void keyReleased(KeyEvent e) {

    }

    public void keyTyped(KeyEvent e) {
        // on ne fait rien
    }
}