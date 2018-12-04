package controleur;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class EcouteurDeSouris extends MouseAdapter {

    private Controler controler;

    public EcouteurDeSouris(Controler controler) {
        this.controler = controler;
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        super.mouseClicked(e);
        controler.mousePressed(e.getPoint(),e);
    }


    @Override
    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e);
        controler.mouseMoved(e.getPoint());
    }
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if(e.getWheelRotation()<0)
        {
            controler.wheelMovedUp(e.getWheelRotation());
        }
        else
        {
            controler.wheelMovedDown(e.getWheelRotation());
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        controler.setLastDragMousePosition(e.getPoint());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        controler.mouseDragged(e.getPoint());
    }
}
