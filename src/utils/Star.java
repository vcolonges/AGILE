package utils;

import java.awt.*;

public class Star extends Polygon {
    public Star(int x, int y, int rayonExterieur, int rayonInterieur, int nbBranches) {
        this(x, y, rayonExterieur, rayonInterieur, nbBranches, 0);
    }
    public Star(int x, int y, int rayonExterieur, int rayonInterieur, int nbBranches, double angleDepart) {
        super(getXCoordinates(x, y, rayonExterieur, rayonInterieur,  nbBranches, angleDepart)
                ,getYCoordinates(x, y, rayonExterieur, rayonInterieur, nbBranches, angleDepart)
                ,nbBranches*2);
    }

    protected static int[] getXCoordinates(int x, int y, int rayonExterieur, int rayonInterieur, int nbBranches, double angleDepart) {
        int res[]=new int[nbBranches*2];
        double addAngle=2*Math.PI/nbBranches;
        double angle=angleDepart;
        double innerAngle=angleDepart+Math.PI/nbBranches;
        for (int i=0; i<nbBranches; i++) {
            res[i*2]=(int)Math.round(rayonExterieur*Math.cos(angle))+x;
            angle+=addAngle;
            res[i*2+1]=(int)Math.round(rayonInterieur*Math.cos(innerAngle))+x;
            innerAngle+=addAngle;
        }
        return res;
    }

    protected static int[] getYCoordinates(int x, int y, int rayonExterieur, int rayonInterieur, int nbBranches, double angleDepart) {
        int res[]=new int[nbBranches*2];
        double addAngle=2*Math.PI/nbBranches;
        double angle=angleDepart;
        double innerAngle=angleDepart+Math.PI/nbBranches;
        for (int i=0; i<nbBranches; i++) {
            res[i*2]=(int)Math.round(rayonExterieur*Math.sin(angle))+y;
            angle+=addAngle;
            res[i*2+1]=(int)Math.round(rayonInterieur*Math.sin(innerAngle))+y;
            innerAngle+=addAngle;
        }
        return res;
    }
}
