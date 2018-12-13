package utils;

import exceptions.XMLException;
import modele.Livraison;
import modele.Noeud;
import modele.Plan;
import modele.Troncon;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class XMLParserTest {

    @Test
    void testParsePlanNoeud() throws XMLException {
        Plan plan = new Plan();
        Noeud noeud = new Noeud(123,1.23,4.56);
        plan.addNoeud(noeud);

        Plan planParse = XMLParser.parsePlan("tests/test_files/plan_noeud.xml");

        assertEquals(plan,planParse);
    }

    @Test
    void testParsePlanTroncon() throws XMLException {
        Plan plan = new Plan();
        Noeud origine = new Noeud(123,1.23,4.56);
        Noeud destination = new Noeud(456,4.56,1.23);
        plan.addNoeud(origine);
        plan.addNoeud(destination);
        Troncon troncon = new Troncon(origine,destination,123,"Rue");
        plan.addTroncon(troncon);

        Plan planParse = XMLParser.parsePlan("tests/test_files/plan_troncon.xml");

        assertEquals(plan,planParse);
    }

    @Test
    void testParsePlanDoubleNoeud() {
        Throwable thrown = assertThrows(XMLException.class, () -> XMLParser.parsePlan("tests/test_files/plan_double_noeud.xml"));
        assertEquals("Présence d'un doublon de noeud.", thrown.getMessage());
    }

    @Test
    void testParsePlanDoubleTroncon() {
        assertThrows(XMLException.class, () -> XMLParser.parsePlan("tests/test_files/plan_double_troncon.xml"));
    }

    @Test
    void testParsePlanNoeudSansID() {
        assertThrows(XMLException.class, () -> XMLParser.parsePlan("tests/test_files/plan_noeud_sans_id.xml"));
    }

    @Test
    void testParsePlanNoeudSansLatitude() {
        assertThrows(XMLException.class, () -> XMLParser.parsePlan("tests/test_files/plan_noeud_sans_latitude.xml"));
    }

    @Test
    void testParsePlanNoeudSansLongitude() {
        assertThrows(XMLException.class, () -> XMLParser.parsePlan("tests/test_files/plan_noeud_sans_longitude.xml"));
    }

    @Test
    void testParseFichierInexistant() {
        assertThrows(XMLException.class, () -> XMLParser.parsePlan("pas_un_fichier"));
    }

    @Test
    void testParsePlanLivraison() throws XMLException, ParseException {
        Plan plan = new Plan();
        Noeud origine = new Noeud(123,1.23,4.56);
        Noeud destination = new Noeud(456,4.56,1.23);
        plan.addNoeud(origine);
        plan.addNoeud(destination);
        Troncon troncon = new Troncon(origine,destination,123,"Rue");
        plan.addTroncon(troncon);
        Livraison entrepot = new Livraison(origine,0);
        Livraison livraison = new Livraison(destination,30);
        Date heureDepart = new SimpleDateFormat("hh:mm:ss").parse("8:0:0");
        plan.setEntrepot(entrepot);
        plan.setHeureDepart(heureDepart);
        plan.addLivraison(livraison);

        Plan planParse = XMLParser.parsePlan("tests/test_files/plan_troncon.xml");
        planParse = XMLParser.parseLivraisons("tests/test_files/livraison.xml",planParse);

        assertEquals(plan,planParse);
    }
}