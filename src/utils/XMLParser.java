package utils;

import exceptions.XMLException;
import modele.Livraison;
import modele.Noeud;
import modele.Plan;
import modele.Troncon;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class XMLParser {

    public static Plan parsePlan(String lienFichier) throws XMLException {
        Plan plan = new Plan();

        Document doc = ouvrirDocument(lienFichier);

        doc.getDocumentElement().normalize();

        final Element racine = doc.getDocumentElement();
        if(!racine.getTagName().equals("reseau")){
            throw new XMLException("Le fichier n'est pas un fichier de plan");
        }

        NodeList listNoeuds = doc.getElementsByTagName("noeud");

        for (int i = 0; i < listNoeuds.getLength(); i++) {
            if(!plan.addNoeud(parseNoeud(listNoeuds.item(i)))){
                throw new XMLException("Présence d'un doublon de noeud.");
            }
        }

        NodeList listTroncons = doc.getElementsByTagName("troncon");
        for (int i = 0; i < listTroncons.getLength(); i++){
            Troncon troncon = parseTroncon(plan, listTroncons.item(i));
            if(!plan.addTroncon(troncon)){
                throw new XMLException("Présence d'un doublon de troncon.");
            }
            troncon.getOrigine().addTronconAdjacent(troncon);
        }
        return plan;
    }

    private static Document ouvrirDocument(String lienFichier) throws XMLException {
        File file = new File(lienFichier);
        if(!file.exists()){
            throw new XMLException("Fichier \'"+lienFichier+"\' introuvable");
        }

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        Document doc;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(file);
        } catch (Exception e) {
            throw new XMLException("Problème lors du parsing.");
        }
        return doc;
    }

    private static Noeud parseNoeud(Node node) throws XMLException {

        Element element = (Element) node;

        /*
         * Si l'attribut "id" n'existe pas OU est <= 0, throw XMLException explicite
         * /!\ Si l'attribut "id" fait plus de 19 caractères, throw NumberFormatException
         */
        long id;
        if(element.getAttribute("id").equals("")){
            throw new XMLException("Présence d'un noeud sans id.");
        }else if((id = Long.parseLong(element.getAttribute("id"))) <= 0) {
            throw new XMLException("Présence d'un id de noeud inférieur ou égal à 0 .");
        }

        /*
         * Si l'attribut "latitude" n'existe pas, throw XMLException explicite
         * /!\ Si l'attribut "latitude" n'est pas parsable en Double, throw NumberFormatException
         */
        if(element.getAttribute("latitude").equals("")){
            throw new XMLException("Présence d'un noeud sans latitude.");
        }
        double latitude = Double.parseDouble(element.getAttribute("latitude"));

        /*
         * Si l'attribut "longitude" n'existe pas, throw XMLException explicite
         * /!\ Si l'attribut "longitude" n'est pas parsable en Double, throw NumberFormatException
         */
        if(element.getAttribute("longitude").equals("")){
            throw new XMLException("Présence d'un noeud sans longitude.");
        }
        double longitude = Double.parseDouble(element.getAttribute("longitude"));

        return new Noeud(id,latitude,longitude);

    }

    private static Troncon parseTroncon(Plan plan, Node node) throws XMLException {

        Element element = (Element) node;

        /*
         * Si l'attribut "origine" n'existe pas OU est <= 0, throw XMLException explicite
         * /!\ Si l'attribut "origine" fait plus de 19 caractères, throw NumberFormatException
         */
        long idOrigine;
        if(element.getAttribute("origine").equals("") ){
            throw new XMLException("Présence d'un troncon sans origine.");
        }else if((idOrigine = Long.parseLong(element.getAttribute("origine"))) <= 0) {
            throw new XMLException("Présence d'une origine de troncon <= 0 .");
        }
        /*
         * Si l'idOrigine ne correspond pas à un Noeud trouvé précédemment, throw XMLException explicite
         */
        Noeud origine;
        if((origine = plan.getNoeuds().get(idOrigine)) == null){
            throw new XMLException("Présence d'un id de noeud d'origine invalide.");
        }

        /*
         * Si l'attribut "destination" n'existe pas OU est <= 0, throw XMLException explicite
         * /!\ Si l'attribut "destination" fait plus de 19 caractères, throw NumberFormatException
         */
        long idDestination;
        if(element.getAttribute("destination").equals("") ){
            throw new XMLException("Présence d'un troncon sans destination.");
        }else if((idDestination = Long.parseLong(element.getAttribute("destination"))) <= 0) {
            throw new XMLException("Présence d'une destination de troncon <= 0 .");
        }
        /*
         * Si l'idDestination ne correspond pas à un Noeud trouvé précédemment, throw XMLException explicite
         */
        Noeud destination;
        if((destination = plan.getNoeuds().get(idDestination)) == null){
            throw new XMLException("Présence d'un id de noeud de destination invalide.");
        }

        /*
         * Si l'attribut "longueur" n'existe pas, throw XMLException explicite
         * /!\ Si l'attribut "longueur" n'est pas parsable en Double, throw NumberFormatException
         */
        double longueur;
        if(element.getAttribute("longueur").equals("")){
            throw new XMLException("Présence d'un troncon sans longueur.");
        }else if((longueur = Double.parseDouble(element.getAttribute("longueur"))) <= 0) {
            throw new XMLException("Présence d'une longueur <= 0 .");
        }

        // Aucune vérification sur le nom de rue
        String nomRue = element.getAttribute("nomRue");

        return new Troncon(origine,destination,longueur,nomRue);
    }

    public static Plan parseTrajets(String lienFichier, Plan plan) throws XMLException {
        plan.getLivraisons().clear();
        plan.getTournees().clear();
        Document doc = ouvrirDocument(lienFichier);

        final Element racine = doc.getDocumentElement();
        if(!racine.getTagName().equals("demandeDeLivraisons")){
            throw new XMLException("Le fichier n'est pas un fichier de demande de livraisons");
        }

        // Parse Entrepot
        NodeList listEntrepots = doc.getElementsByTagName("entrepot");

        if(listEntrepots.getLength()>1){
            throw new XMLException("Présence de deux entrepôts");
        }
        Node node = listEntrepots.item(0);
        Element element = (Element) node;

        long entrepot = Long.parseLong(element.getAttribute("adresse"));
        if(plan.getNoeuds().get(entrepot) == null){
            throw new XMLException("Adresse de l'entrepot invalide");
        }
        plan.setEntrepot(new Livraison(plan.getNoeuds().get(entrepot),0));

        String stringHeureDepart = element.getAttribute("heureDepart");
        Date heureDepart;
        try {
            heureDepart = new SimpleDateFormat("hh:mm:ss").parse(stringHeureDepart);
        } catch (ParseException e) {
            throw new XMLException("Parsing de l'heure de départ impossible.");
        }
        plan.setHeureDepart(heureDepart);

        NodeList listLivraisons = doc.getElementsByTagName("livraison");

        for (int i = 0; i < listLivraisons.getLength(); i++) {
            plan.addLivraison(parseLivraison(listLivraisons.item(i), plan));
        }

        return plan;
    }

    private static Livraison parseLivraison(Node node, Plan plan) throws XMLException {
        Element element = (Element) node;

        /*
         * Si l'attribut "adresse" n'existe pas OU est <= 0, throw XMLException explicite
         * /!\ Si l'attribut "adresse" fait plus de 19 caractères, throw NumberFormatException
         */
        long idAdresse;
        if(element.getAttribute("adresse").equals("") ){
            throw new XMLException("Présence d'une livraison sans origine.");
        }else if((idAdresse = Long.parseLong(element.getAttribute("adresse"))) <= 0) {
            throw new XMLException("Présence d'une adresse de livraison <= 0 .");
        }
        /*
         * Si l'idOrigine ne correspond pas à un Noeud trouvé précédemment, throw XMLException explicite
         */
        Noeud adresse;
        if((adresse = plan.getNoeuds().get(idAdresse)) == null){
            throw new XMLException("Présence d'un id de noeud d'origine invalide.");
        }

        int duree;
        if(element.getAttribute("duree").equals("") ){
            throw new XMLException("Présence d'une livraison sans duree.");
        }else if((duree = Integer.parseInt(element.getAttribute("duree"))) < 0) {
            throw new XMLException("Présence d'une duree de livraison < 0 .");
        }

        return new Livraison(adresse, duree);

    }

}
