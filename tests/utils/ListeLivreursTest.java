package utils;

import modele.Livraison;
import modele.Livreur;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ListeLivreursTest {

    @Test
    void getListePrenomsLivreurs() {
        ArrayList<String> prenomsLivreurs = new ArrayList<>();
        prenomsLivreurs.add("Robert");
        prenomsLivreurs.add("Jeanne");
        prenomsLivreurs.add("Alfred");
        prenomsLivreurs.add("Antoine");
        prenomsLivreurs.add("Richard");
        prenomsLivreurs.add("Monique");
        prenomsLivreurs.add("Charles");
        prenomsLivreurs.add("Juliette");
        prenomsLivreurs.add("Manon");
        prenomsLivreurs.add("Camille");
        prenomsLivreurs.add("Mathieu");
        prenomsLivreurs.add("Emma");
        prenomsLivreurs.add("Julien");
        prenomsLivreurs.add("Matheo");
        prenomsLivreurs.add("Eloïse");

        assertEquals(ListeLivreurs.getListePrenomsLivreurs(),prenomsLivreurs);
    }

    @Test
    void getLivreurParPrenom() {
        Livreur l = new Livreur("Robert",new Color(230,25,75));
        assertEquals(ListeLivreurs.getLivreurParPrenom("Robert"),l);
    }
}