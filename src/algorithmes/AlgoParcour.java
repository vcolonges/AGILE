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
    public ArrayList<Chemin> calculChemin(Livraison departLiv, ArrayList<Livraison> livraisons) {
        //initialisation
        ArrayList<Chemin> result = new ArrayList<>();
        Noeud depart = departLiv.getNoeud();

        // ensemble des troncons adjacents à un noeud
        HashSet<Troncon> curNoeudTroncons = null;
        // Noeud dont tous les succeeceurs sont grisés
        ArrayList<Noeud> blackNoeud = new ArrayList<>();
        //Ensemble de noeuds decouverts
        ArrayList<Noeud> greyNoeuds = new ArrayList<>();
        greyNoeuds.add(depart);
        // Collection des noeuds, leur prédécesseur et la distance jusqu'à ce noeud depuis le noeud départ
        HashMap<Long, Paire<Troncon, Double>> successorDistance = new HashMap<Long, Paire<Troncon, Double>>();
        //initialisation de collection avec le noeud départ
        successorDistance.put(depart.getId(), new Paire(null,0.0));
        //on parcourt tous les noeuds gris
        for(int curNoeudIndex=0; curNoeudIndex<greyNoeuds.size(); curNoeudIndex++)
        {   //si le noeud gris est celui de la fin, on ne calcule pas ses successeurs car c'est la fin de chemin
            Noeud curNoeud = greyNoeuds.get(curNoeudIndex);
            Noeud tmpGreyNoeud = null;
            curNoeudTroncons=curNoeud.getTronconsAdjacents();
            //On récupère la distance du depart jusquà noeud prédécesseur forcement définie dans la collection
            double curTravelDistance = successorDistance.get(curNoeud.getId()).getSecond();
            //on parcourt les Noeud adjacents via les troncons
            for(Troncon tmpTroncon : curNoeudTroncons)
            {
                //on calcule la distance au noeud seulement s'il n'est pas dans blackNoeud
                if(!blackNoeud.contains(tmpTroncon.getDestination())) //****Complexité O(n), peut être amélioré
                {
                    tmpGreyNoeud=tmpTroncon.getDestination();
                    //On ajoute les noeuds découverts dans greyNoeud seulement s'il n'est pasdéjà present (complexité O(1))
                    if(!greyNoeuds.contains(tmpGreyNoeud))
                    {
                        greyNoeuds.add(tmpGreyNoeud);
                    }
                    curTravelDistance+=tmpTroncon.getLongueur();
                    //Si le noeud gris courant (tmpGreyNoeud) possède une distance plus courte à atteindre dépuis son nouveau predecesseur, on met à jour
                    // la collection
                    if(successorDistance.get(tmpGreyNoeud.getId())==null || successorDistance.get(tmpGreyNoeud.getId()).getSecond()>curTravelDistance)
                    {
                        successorDistance.put(tmpGreyNoeud.getId(), new Paire(tmpTroncon,curTravelDistance));
                    }
                }
            }
            //une fois tous les noeud adjacents du curNoeud parcouru, on l'ajoute dans blackNoeud
            blackNoeud.add(curNoeud);
            //greyNoeuds.remove(curNoeud); //probablément juste une perte de temps
        }

        for(Livraison livraison : livraisons) {
            if(livraison!=departLiv) {
                Troncon sortTroncon = successorDistance.get(livraison.getNoeud().getId()).getPremier();
                Chemin tmpChemin = new Chemin(departLiv, livraison, successorDistance.get(livraison.getNoeud().getId()).getSecond());
                tmpChemin.getTroncons().add(sortTroncon);
                while (sortTroncon.getOrigine() != depart) {
                    sortTroncon = successorDistance.get(sortTroncon.getOrigine().getId()).getPremier();
                    tmpChemin.getTroncons().add(0, sortTroncon);
                }
                result.add(tmpChemin);
            }
        }

        return result;
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Calculer la distance entre 2 points.
    private Double PointsDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow((x2 - x1), 2.0) + Math.pow((y2 - y1),2.0 ));
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //calculer les données d'un cercle
    private ArrayList<Double> initCircleData(ArrayList<Livraison> circle) {

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

    private ArrayList<Double> initCircleData(ArrayList<Livraison> circle, ArrayList<Livraison> circle2) {

       ArrayList<Livraison> allCircle = new ArrayList<>(circle);
       allCircle.addAll(circle2);
       return initCircleData(allCircle);
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Mettre à jour les données d'un cercle
    private ArrayList<Double> MAJCircleData(Noeud curNoeud, ArrayList<Double> circleData, double newTaille) {
        double xG=circleData.get(0);
        double yG=circleData.get(1);

        circleData.set(0, (xG*(newTaille-1)+curNoeud.getLatitude())/newTaille);
        circleData.set(1, (yG*(newTaille-1)+curNoeud.getLongitude())/newTaille);
        return circleData;
    }



    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
//optimiser le contenu d'un cercle
    private ArrayList<ArrayList<Livraison>> circleOpt(ArrayList<ArrayList<Livraison>> result, ArrayList<ArrayList<Double>> circlesData, int nbrLivreur, int nLim)
    {
        ArrayList<ArrayList<Livraison>> tmpResult = new ArrayList<ArrayList<Livraison>>();
        for (int i = 0; i < nbrLivreur; i++)
        {
            ArrayList<Livraison> tmpResultList = new ArrayList<Livraison>();
            tmpResult.add(tmpResultList);
        }

            for (int iterationTime = 0; iterationTime < nbrLivreur; iterationTime++) {
                boolean testNoChange;
                int somme = 1;
                int indexCircle = 0;
                while (somme != 0) {
                    somme = 0;

                    testNoChange = true;
                    int lastCircleIndex = 0;
                    if (result.get(indexCircle).size() != 0) {
                        //pour chaque cercle on prend chaque noeud
                        for (int indexNoeud = 0; indexNoeud < result.get(indexCircle).size(); indexNoeud++) {
                            //pour chaque noeud on récupère sa distance au centre de son cercle
                            Noeud curNoeud = result.get(indexCircle).get(indexNoeud).getNoeud();
                            double distanceToCenter = PointsDistance(curNoeud.getLatitude(), curNoeud.getLongitude(), circlesData.get(indexCircle).get(0), circlesData.get(indexCircle).get(1));
                            //on compare la distance entre noeud et son cercle à la distance du noeud et autres cercles
                            lastCircleIndex = indexCircle;
                            double tmpDistance = 0;
                            for (int tmpIndexCircle = 0; tmpIndexCircle < nbrLivreur; tmpIndexCircle++) {
                                tmpDistance = PointsDistance(curNoeud.getLatitude(), curNoeud.getLongitude(), circlesData.get(tmpIndexCircle).get(0), circlesData.get(tmpIndexCircle).get(1));
                                if (tmpDistance < distanceToCenter && result.get(tmpIndexCircle).size() != 0) {
                                    distanceToCenter = tmpDistance;
                                    lastCircleIndex = tmpIndexCircle;
                                }
                            }
                            //si le noeud est plus pres du centre d'un autre cercle que du sien, on le transmet à resultat final du cercle ciblé et on redemarre l'algorithme
                            //pour le cercle cible
                            //possible d'optimiser
                            if (lastCircleIndex != indexCircle) {
                                result.get(lastCircleIndex).add(result.get(indexCircle).remove(indexNoeud));
                                circlesData.set(lastCircleIndex, MAJCircleData(curNoeud, circlesData.get(lastCircleIndex), result.get(lastCircleIndex).size()));
                                circlesData.set(indexCircle, initCircleData(result.get(indexCircle), tmpResult.get(indexCircle)));

                                indexCircle = lastCircleIndex;
                                testNoChange = false;
                                break;
                            }
                        }


                    }
                    if (testNoChange) {
                        if (result.get(indexCircle).size() <= nLim) {
                            tmpResult.get(indexCircle).addAll(result.get(indexCircle));
                            result.get(indexCircle).clear();
                            indexCircle = (indexCircle + 1) % nbrLivreur;
                        } else{
                            Livraison curLiv = null;
                            int indexCircleToMove = -1;
                            double distanceToCenter = 10000000;
                            for (int indexOkNoeud = 0; indexOkNoeud != result.get(indexCircle).size(); indexOkNoeud++) {
                                Livraison tmpCurLiv = result.get(indexCircle).get(indexOkNoeud);
                                for (int indexTargetCircle = 0; indexTargetCircle < nbrLivreur; indexTargetCircle++) {
                                    double tmpDistanceToCenter = PointsDistance(tmpCurLiv.getNoeud().getLatitude(), tmpCurLiv.getNoeud().getLongitude(), circlesData.get(indexTargetCircle).get(0), circlesData.get(indexTargetCircle).get(1));
                                    if (tmpDistanceToCenter < distanceToCenter && indexTargetCircle != indexCircle && result.get(indexTargetCircle).size() != 0) {
                                        distanceToCenter = tmpDistanceToCenter;
                                        curLiv = tmpCurLiv;
                                        indexCircleToMove = indexTargetCircle;
                                    }

                                }

                            }
                            result.get(indexCircleToMove).add(curLiv);
                            result.get(indexCircle).remove(curLiv);
                            tmpResult.get(indexCircle).addAll(result.get(indexCircle));
                            result.get(indexCircle).clear();
                            indexCircle = indexCircleToMove;
                        }


                    }

                    for (int i = 0; i < nbrLivreur; i++) {

                        somme += result.get(i).size();
                    }
                }
                for(int i=0; i<result.size(); i++)
                {
                    result.get(i).addAll(tmpResult.get(i));
                    tmpResult.get(i).clear();
                }


            }

        return result;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //separer l'ensemble de livraisons en n listes de taille k correspondant au nombre de livraisons
    //chaque liste represente un cercle avec la plus grande densité des k livraison adjacent
    private ArrayList<ArrayList<Livraison>> toCircle(ArrayList<Livraison> livraisons, int nbrLivreur, int nLim) {
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
            circlesData.add(initCircleData(result.get(indexCircle)));
            //initialiser les données d'un cercle
        }

        result = circleOpt( result, circlesData, nbrLivreur, nLim);

        //remise des noeuds équitablement
        /*if(livraisons.size() % nbrLivreur !=0 ) {
            for (int indexCircle = 0; indexCircle < nbrLivreur; indexCircle++) {
                Livraison tmpLiv = null;
                int tmpTargetIndexCircle = -1;
                if (result.get(indexCircle).size() < nLim - 1) {
                    while (result.get(indexCircle).size() < nLim - 1) {
                        double tmpDistance = 1000000;
                        for (int indexTargetCircle = 0; indexTargetCircle < nbrLivreur; indexTargetCircle++) {
                            if (indexTargetCircle == indexCircle || result.get(indexTargetCircle).size() < nLim - 1) {
                                continue;
                            }
                            Livraison livTarget = null;
                            for (int indexTargetNoeud = 0; indexTargetNoeud < result.get(indexTargetCircle).size(); indexTargetNoeud++) {
                                livTarget = result.get(indexTargetCircle).get(indexTargetNoeud);
                                double tmpDistance2 = PointsDistance(livTarget.getNoeud().getLatitude(), livTarget.getNoeud().getLongitude(), circlesData.get(indexTargetCircle).get(0), circlesData.get(indexTargetCircle).get(1));
                                if (tmpDistance > tmpDistance2) {
                                    tmpDistance = tmpDistance2;
                                    tmpLiv = livTarget;
                                    tmpTargetIndexCircle = indexTargetCircle;
                                }
                            }
                        }
                        if (tmpTargetIndexCircle != -1) {
                            result.get(indexCircle).add(tmpLiv);
                            result.get(tmpTargetIndexCircle).remove(tmpLiv);
                            circlesData.set(indexCircle, initCircleData(result.get(indexCircle)));
                            circlesData.set(tmpTargetIndexCircle, initCircleData(result.get(tmpTargetIndexCircle)));
                        }
                    }
                }
            }
        }*/

        return result;

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public ArrayList<ArrayList<Livraison>> getLivraisons(ArrayList<Livraison> livraisons, int nbrLivreur) {
        int nLim = livraisons.size()/nbrLivreur;
        if(livraisons.size()%nbrLivreur!=0)
        {
            nLim++;
        }
        return toCircle(livraisons, nbrLivreur, nLim);
    }


}

