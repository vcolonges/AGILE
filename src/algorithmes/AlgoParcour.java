package algorithmes;

import modele.Chemin;
import modele.Livraison;
import modele.Noeud;
import modele.Troncon;
import utils.Paire;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


public class AlgoParcour {
    //calculer 1e plus court chemin entre 2 livraisons
    // à recoder avec une somme pour eviter des boucles
    public static ArrayList<Chemin> calculChemin(Livraison departLiv, ArrayList<Livraison> livraisons) {
        //initialisation
        ArrayList<Chemin> result = new ArrayList<>();
        Noeud depart = departLiv.getNoeud();

        // ensemble des troncons adjacents à un noeud
        HashSet<Troncon> curNoeudTroncons;
        // Noeud dont tous les succeeceurs sont grisés
        ArrayList<Noeud> blackNoeud = new ArrayList<>();
        //Ensemble de noeuds decouverts
        ArrayList<Noeud> greyNoeuds = new ArrayList<>();
        greyNoeuds.add(depart);
        // Collection des noeuds, leur prédécesseur et la distance jusqu'à ce noeud depuis le noeud départ
        HashMap<Long, Paire<Troncon, Double>> successorDistance = new HashMap<Long, Paire<Troncon, Double>>();
        //initialisation de collection avec le noeud départ
        successorDistance.put(depart.getId(), new Paire<>(null,0.0));
        //on parcourt tous les noeuds gris
        for(int curNoeudIndex=0; curNoeudIndex<greyNoeuds.size(); curNoeudIndex++)
        {   //si le noeud gris est celui de la fin, on ne calcule pas ses successeurs car c'est la fin de chemin
            Noeud curNoeud = greyNoeuds.get(curNoeudIndex);

            Noeud tmpGreyNoeud = null;
            if(blackNoeud.contains(curNoeud)) //****Complexité O(n), peut être amélioré
            {
                continue;
            }

            curNoeudTroncons=curNoeud.getTronconsAdjacents();
            //On récupère la distance du depart jusquà noeud prédécesseur forcement définie dans la collection
            double travelDistance = successorDistance.get(curNoeud.getId()).getSecond();
            //on parcourt les Noeud adjacents via les troncons
            for(Troncon tmpTroncon : curNoeudTroncons)
            {

                tmpGreyNoeud=tmpTroncon.getDestination();
                //On ajoute les noeuds découverts dans greyNoeud seulement s'il n'est pasdéjà present (complexité O(1))
                if(!greyNoeuds.contains(tmpGreyNoeud))
                {
                    greyNoeuds.add(tmpGreyNoeud);
                }
                double curTravelDistance = travelDistance + tmpTroncon.getLongueur();
                //Si le noeud gris courant (tmpGreyNoeud) possède une distance plus courte à atteindre dépuis son nouveau predecesseur, on met à jour
                // la collection
                if(successorDistance.get(tmpGreyNoeud.getId())==null || successorDistance.get(tmpGreyNoeud.getId()).getSecond()>curTravelDistance)
                {
                    successorDistance.put(tmpGreyNoeud.getId(), new Paire(tmpTroncon,curTravelDistance));
                }
            }
            //une fois tous les noeud adjacents du curNoeud parcouru, on l'ajoute dans blackNoeud
            blackNoeud.add(curNoeud);
        }

        for(Livraison livraison : livraisons) {
            if(livraison!=departLiv) {
                Troncon sortTroncon = successorDistance.get(livraison.getNoeud().getId()).getPremier();
                Chemin tmpChemin = new Chemin(departLiv, livraison, successorDistance.get(livraison.getNoeud().getId()).getSecond());
                tmpChemin.getTroncons().add(sortTroncon);
                double longueur = sortTroncon.getLongueur();
                while (sortTroncon.getOrigine() != depart) {
                    sortTroncon = successorDistance.get(sortTroncon.getOrigine().getId()).getPremier();
                    longueur+=sortTroncon.getLongueur();
                    tmpChemin.getTroncons().add(0, sortTroncon);
                }
                result.add(tmpChemin);
                tmpChemin.setLongueur(longueur);
            }
        }

        return result;
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Calculer la distance entre 2 points.
    private static Double pointsDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow((x2 - x1), 2.0) + Math.pow((y2 - y1),2.0 ));
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //calculer les données d'un cercle
    private static ArrayList<Double> initCircleData(ArrayList<Livraison> circle) {

        ArrayList<Double> circleData = new ArrayList<>();

        double xG=0;
        double yG=0;
        for(int indexNoeud=0; indexNoeud<circle.size(); indexNoeud++)
        {
            xG+=circle.get(indexNoeud).getNoeud().getLatitude();
            yG+=circle.get(indexNoeud).getNoeud().getLongitude();
        }
        circleData.add(xG/circle.size());
        circleData.add(yG/circle.size());

        return circleData;
    }



    /////////////////////////////////////////////////////////////////////////////

    private static ArrayList<Double> initCircleData(ArrayList<Livraison> circle, ArrayList<Livraison> circle2) {

       ArrayList<Livraison> allCircle = new ArrayList<>(circle);
       allCircle.addAll(circle2);
       return initCircleData(allCircle);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //test validité d'échange de livraisons entre cercles
    private static boolean exchangeTest(ArrayList<Double> origin, Livraison l1, ArrayList<Double> target, Livraison l2)
    {
        double distanceL1ToOrigin = pointsDistance(l1.getNoeud().getLatitude(), l1.getNoeud().getLongitude(), origin.get(0), origin.get(1));
        double distanceL1ToTarget = pointsDistance(l1.getNoeud().getLatitude(), l1.getNoeud().getLongitude(), target.get(0), target.get(1));
        double distanceL2ToOrigin = pointsDistance(l2.getNoeud().getLatitude(), l2.getNoeud().getLongitude(), origin.get(0), origin.get(1));
        double distanceL2ToTarget = pointsDistance(l2.getNoeud().getLatitude(), l2.getNoeud().getLongitude(), target.get(0), target.get(1));

        if(distanceL1ToOrigin - distanceL2ToOrigin> distanceL1ToTarget-distanceL2ToTarget)
        {
            return true;
        }
        else
        {
            return false;
        }
    }



    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //optimiser le contenu d'un cercle
    private static ArrayList<ArrayList<Livraison>> circleOpt(ArrayList<ArrayList<Livraison>> result, ArrayList<ArrayList<Double>> circlesData, int nbrLivreur, int nLimMin, int nLimMax)
    {
        ArrayList<ArrayList<Livraison>> tmpResult = new ArrayList<ArrayList<Livraison>>();
        for (int i = 0; i < nbrLivreur; i++)
        {
            ArrayList<Livraison> tmpResultList = new ArrayList<Livraison>();
            tmpResult.add(tmpResultList);
        }

        for (int iterationTime = 0; iterationTime < nbrLivreur; iterationTime++) {
            int indexCircle = 0;
            int loopCounter = nbrLivreur;
            while (loopCounter != 0) {
                boolean testNoChange = true;
                int lastCircleIndex = indexCircle;
                if (result.get(indexCircle).size() != 0) {
                    //pour chaque cercle on prend chaque noeud
                    for (int indexNoeud = 0; indexNoeud < result.get(indexCircle).size(); indexNoeud++) {
                        //pour chaque noeud on récupère sa distance au centre de son cercle
                        Noeud curNoeud = result.get(indexCircle).get(indexNoeud).getNoeud();
                        double distanceToCenter = pointsDistance(curNoeud.getLatitude(), curNoeud.getLongitude(), circlesData.get(indexCircle).get(0), circlesData.get(indexCircle).get(1));
                        //on compare la distance entre noeud et son cercle à la distance du noeud et autres cercles
                        double tmpDistance;
                        for (int tmpIndexCircle = 0; tmpIndexCircle < nbrLivreur; tmpIndexCircle++) {
                            tmpDistance = pointsDistance(curNoeud.getLatitude(), curNoeud.getLongitude(), circlesData.get(tmpIndexCircle).get(0), circlesData.get(tmpIndexCircle).get(1));
                            if (tmpDistance < distanceToCenter) {
                                distanceToCenter = tmpDistance;
                                lastCircleIndex = tmpIndexCircle;
                            }
                        }
                        //si le noeud est plus pres du centre d'un autre cercle que du sien, on le transmet à resultat final du cercle ciblé et on redemarre l'algorithme
                        //pour le cercle cible
                        //possible d'optimiser
                        if (lastCircleIndex != indexCircle) {
                            tmpResult.get(lastCircleIndex).add(result.get(indexCircle).remove(indexNoeud));
                            circlesData.set(lastCircleIndex, initCircleData(result.get(lastCircleIndex), tmpResult.get(lastCircleIndex)));
                            circlesData.set(indexCircle, initCircleData(result.get(indexCircle), tmpResult.get(indexCircle)));

                            indexCircle = lastCircleIndex;
                            testNoChange = false;
                            loopCounter = nbrLivreur;
                            break;
                        } else {
                            tmpResult.get(indexCircle).add(result.get(indexCircle).remove(indexNoeud));
                        }
                    }
                    if (testNoChange) {
                        indexCircle = (indexCircle + 1) % nbrLivreur;
                    }
                } else {
                    loopCounter--;
                    indexCircle = (indexCircle + 1) % nbrLivreur;
                }
            }

            for (int i = tmpResult.size() - 1; i >= 0; --i) {
                result.get(i).addAll(tmpResult.get(i));
                tmpResult.get(i).clear();
            }
        }

        for (int indexCurCircle = 0; indexCurCircle < result.size(); indexCurCircle++) {
            if (result.get(indexCurCircle).size() < nLimMin && tmpResult.get(indexCurCircle).size() == 0) {
                while (result.get(indexCurCircle).size() < nLimMin) {
                    int indexTarget = -1;
                    int indexMoveFrom = -1;
                    double distanceToCenter = 1000000;

                    for (int indexTargetCircle = 0; indexTargetCircle < nbrLivreur; indexTargetCircle++) {
                        if (indexTargetCircle == indexCurCircle) {
                            continue;
                        }
                        for (int indexTargetNoeud = 0; indexTargetNoeud < result.get(indexTargetCircle).size(); indexTargetNoeud++) {
                            Livraison tmpLiv = result.get(indexTargetCircle).get(indexTargetNoeud);
                            double tmpDistance = pointsDistance(tmpLiv.getNoeud().getLatitude(), tmpLiv.getNoeud().getLongitude(), circlesData.get(indexCurCircle).get(0), circlesData.get(indexCurCircle).get(1));
                            if (distanceToCenter > tmpDistance) {
                                distanceToCenter = tmpDistance;
                                indexTarget = indexTargetNoeud;
                                indexMoveFrom = indexTargetCircle;
                            }

                        }
                    }
                    result.get(indexCurCircle).add(result.get(indexMoveFrom).remove(indexTarget));
                    circlesData.set(indexCurCircle, initCircleData(result.get(indexCurCircle)));
                    if (result.get(indexMoveFrom).size() != 0) {
                        circlesData.set(indexMoveFrom, initCircleData(result.get(indexMoveFrom)));
                    }
                }
                tmpResult.get(indexCurCircle).addAll(result.get(indexCurCircle));
                result.get(indexCurCircle).clear();
                indexCurCircle = -1;
            }
        }

        for (int i = tmpResult.size() - 1; i >= 0; --i) {
            result.get(i).addAll(tmpResult.get(i));
            tmpResult.get(i).clear();
        }

        for (int indexCurCircle = 0; indexCurCircle < result.size(); indexCurCircle++) {
            if (result.get(indexCurCircle).size() > nLimMax ) {
                while (result.get(indexCurCircle).size() > nLimMax) {
                    int indexTarget = -1;
                    int indexMoveTo = -1;
                    double distanceToCenter = 1000000;

                    for (int indexTargetNoeud = 0; indexTargetNoeud < result.get(indexCurCircle).size(); indexTargetNoeud++) {
                        Livraison tmpLiv = result.get(indexCurCircle).get(indexTargetNoeud);

                        for (int indexTargetCircle = 0; indexTargetCircle < nbrLivreur; indexTargetCircle++) {
                            if (indexTargetCircle == indexCurCircle || result.get(indexTargetCircle).size() == 0) {
                                continue;
                            }
                            double tmpDistance = pointsDistance(tmpLiv.getNoeud().getLatitude(), tmpLiv.getNoeud().getLongitude(), circlesData.get(indexTargetCircle).get(0), circlesData.get(indexTargetCircle).get(1));

                            if (distanceToCenter > tmpDistance) {
                                distanceToCenter = tmpDistance;
                                indexTarget = indexTargetNoeud;
                                indexMoveTo = indexTargetCircle;
                            }
                        }
                    }
                    result.get(indexMoveTo).add(result.get(indexCurCircle).remove(indexTarget));
                    circlesData.set(indexCurCircle, initCircleData(result.get(indexCurCircle)));
                    circlesData.set(indexMoveTo, initCircleData(result.get(indexMoveTo)));
                }
                tmpResult.get(indexCurCircle).addAll(result.get(indexCurCircle));
                result.get(indexCurCircle).clear();
                indexCurCircle = -1;
            }
        }

        for (int i = tmpResult.size() - 1; i >= 0; --i) {
            result.get(i).addAll(tmpResult.get(i));
            tmpResult.get(i).clear();
        }

        tmpResult.clear();
        int indexCircle = 0;
        int loopCounter=nbrLivreur;
        while (loopCounter!=0/*result.size()!=0*/) {
            int indexTargetCircleToMove = -1;
            int indexNoeudToMove = -1;
            boolean testNoChange = true;
            for (int indexNoeud = 0; indexNoeud < result.get(indexCircle).size(); indexNoeud++) {
                Livraison curLiv = result.get(indexCircle).get(indexNoeud);
                double distanceToCenter = pointsDistance(curLiv.getNoeud().getLatitude(), curLiv.getNoeud().getLongitude(), circlesData.get(indexCircle).get(0), circlesData.get(indexCircle).get(1));
                for (int indexTargetCircle = 0; indexTargetCircle < result.size(); indexTargetCircle++) {
                    double distanceToTargetCenter = pointsDistance(curLiv.getNoeud().getLatitude(), curLiv.getNoeud().getLongitude(), circlesData.get(indexTargetCircle).get(0), circlesData.get(indexTargetCircle).get(1));
                    if(distanceToTargetCenter<distanceToCenter)
                    {
                        distanceToCenter=distanceToTargetCenter;
                        indexTargetCircleToMove = indexTargetCircle;
                        indexNoeudToMove = indexNoeud;
                    }
                }

                if(indexTargetCircleToMove != -1)
                {
                    double distanceToOriginCenter = 1000000;
                    int indexNoeudToReturn = -1;
                    for(int indexTargetNoeud = 0; indexTargetNoeud<result.get(indexTargetCircleToMove).size(); indexTargetNoeud++)
                    {
                        double distanceCircleNoeudToCenter = pointsDistance(curLiv.getNoeud().getLatitude(), curLiv.getNoeud().getLongitude(), circlesData.get(indexCircle).get(0), circlesData.get(indexCircle).get(1));
                        Livraison targetCurLiv = result.get(indexTargetCircleToMove).get(indexTargetNoeud);
                        double tmpDistanceToOriginCenter = pointsDistance(targetCurLiv.getNoeud().getLatitude(), targetCurLiv.getNoeud().getLongitude(), circlesData.get(indexCircle).get(0), circlesData.get(indexCircle).get(1));
                        //double distanceToCurCenter = PointsDistance(targetCurLiv.getNoeud().getLatitude(), targetCurLiv.getNoeud().getLongitude(), circlesData.get(indexTargetCircleToMove).get(0), circlesData.get(indexTargetCircleToMove).get(1));
                        if(distanceToOriginCenter>tmpDistanceToOriginCenter && exchangeTest(circlesData.get(indexCircle), result.get(indexCircle).get(indexNoeudToMove), circlesData.get(indexTargetCircleToMove), result.get(indexTargetCircleToMove).get(indexTargetNoeud)))
                        {
                            distanceToOriginCenter=tmpDistanceToOriginCenter;
                            indexNoeudToReturn = indexTargetNoeud;
                        }
                    }

                    if(indexNoeudToReturn!= -1)
                    {
                        result.get(indexCircle).add(result.get(indexTargetCircleToMove).remove(indexNoeudToReturn));
                        result.get(indexTargetCircleToMove).add(result.get(indexCircle).remove(indexNoeudToMove));
                        circlesData.set(indexCircle, initCircleData(result.get(indexCircle)));
                        circlesData.set(indexTargetCircleToMove, initCircleData(result.get(indexTargetCircleToMove)));
                        indexCircle = indexTargetCircleToMove;
                        testNoChange=false;
                        loopCounter=nbrLivreur;
                        break;
                    }
                }
            }
            if(testNoChange)
            {
                indexCircle = (indexCircle+1) % result.size();
                loopCounter--;
            }
        }

        return result;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //separer l'ensemble de livraisons en n listes de taille k correspondant au nombre de livraisons
    //chaque liste represente un cercle avec la plus grande densité des k livraison adjacent
    private static ArrayList<ArrayList<Livraison>> toCircle(ArrayList<Livraison> livraisons, int nbrLivreur, int nLimMin, int nLimMax) {
        //stocker aleatoirement k livraisons en nbrLivreur listes
        ArrayList<ArrayList<Livraison>> result = new ArrayList<ArrayList<Livraison>>();
        int indexLivraison = 0;
        for (int i = 0; i < nbrLivreur; i++)
        {
            ArrayList<Livraison> tmpList = new ArrayList<Livraison>();
            for (int j = 0; j < livraisons.size() / nbrLivreur; j++) {
                tmpList.add(livraisons.get(indexLivraison));
                indexLivraison++;
            }
            result.add(tmpList);
        }

        //si on a juste 1 livreur, pas besoin de faire le clustering
        if(nbrLivreur==1)
        {
            return result;
        }
        //si le nombre de livraisons n'est pas divisible par nbrLivreur, repartir le reste
        // entre les listes
        for (int p = indexLivraison; p < livraisons.size(); p++) {
            result.get(livraisons.size() - p).add(livraisons.get(p));
        }

        //à chaque cercle on attribue un centre se composant de deux coordonnées et deux coordonées de deux noeuds les plus eloignés
        // pour chaque element de la liste on a:
        // index 0 : Latitude Noeud 1, index 1 : Longitude Noeud 1
        // index 2 : Latitude Noeud 2, index 3 : Longitude Noeud 2
        // index 4 : Latitude centre, index 5 : Longitude centre
        ArrayList<ArrayList<Double>> circlesData = new ArrayList<>();
        for (int indexCircle = 0; indexCircle < nbrLivreur; indexCircle++) {
            //initialiser les données d'un cercle
            circlesData.add(initCircleData(result.get(indexCircle)));
        }
        result = circleOpt( result, circlesData, nbrLivreur, nLimMin, nLimMax);
        return result;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static ArrayList<ArrayList<Livraison>> getLivraisons(ArrayList<Livraison> livraisons, int nbrLivreur) {
        int nLimMin = livraisons.size()/nbrLivreur;
        int nLimMax = nLimMin;

        if(livraisons.size()%nbrLivreur!=0)
        {
            nLimMax++;
        }
        return toCircle(livraisons, nbrLivreur, nLimMin, nLimMax);
    }
}

