package test;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CentreDeTriTest {

    private CentreDeTri centre;
    private Poubelle poubelle;
    private Commerce commerce;

    @BeforeEach
    public void setUp() {
        centre = new CentreDeTri(7, "Centre Principal", "1 rue de la medaille");
        poubelle = new Poubelle(1, 100, "Zone 1", TypePoubelle.JAUNE, 1);
        commerce = new Commerce(1, "Commerce 1", centre);
    }

    @Test
    public void testAjouterPoubelle() {
        centre.ajouterPoubelle(poubelle);
        assertTrue(centre.getPoubelle().contains(poubelle));
    }

    @Test
    public void testRetirerPoubelle() {
        centre.ajouterPoubelle(poubelle);
        centre.retirerPoubelle(poubelle);
        assertFalse(centre.getPoubelle().contains(poubelle));
    }

    @Test
    public void testAjouterCommerce() {
        centre.ajouterCommerce(commerce);
        assertTrue(centre.getCommerce().contains(commerce));
    }

    @Test
    public void testRetirerCommerce() {
        centre.ajouterCommerce(commerce);
        centre.retirerCommerce(commerce);
        assertFalse(centre.getCommerce().contains(commerce));
    }

    @Test
    public void testGetQuartiersDesservis() {
        List<String> quartiers = centre.getQuartiersDesservis();
        assertNotNull(quartiers);
    }

    @Test
    public void testGetHistoriqueRejets() {
        List<Depot> rejets = centre.getHistoriqueRejets();
        assertNotNull(rejets);
    }

    @Test
    public void testGetNom() {
        assertEquals("Centre Principal", centre.getNom());
    }

    @Test
    public void testGetAdresse() {
        assertEquals("1 rue de la medaille", centre.getAdresse());
    }

    @Test
    public void testGetId() {
        assertEquals(7, centre.getId()); // ✔ ID fixé dans setUp()
    }

    @Test
    public void testCollecterDechets() {
        centre.ajouterPoubelle(poubelle);
        centre.collecterDechets(); // S'exécute sans erreur
    }

    @Test
    public void testGenererStatistiques() {
        centre.genererStatistiques(); // S'exécute sans erreur
    }

    @Test
    public void testTraiterRejet() {
        centre.traiterRejet(); // S'exécute sans erreur
    }

    @Test
    public void testAnalyserDepotsParQuartier() {
        centre.analyserDepotsParQuartier(); // S'exécute sans erreur
    }

    @Test
    public void testAnalyserDepotsParType() {
        centre.analyserDepotsParType(); // S'exécute sans erreur
    }
}
