package test;

import model.*;
import model.CentreDeTri;
import model.Commerce;
import model.Poubelle;
import model.TypePoubelle;
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
        commerce = new Commerce("Commerce 1", centre);
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
        assertEquals("Adresse", centre.getAdresse());
    }

    @Test
    public void testGetId() {
        assertEquals(1, centre.getId());
    }

    @Test
    public void testCollecterDechets() {
        centre.ajouterPoubelle(poubelle);
        centre.collecterDechets(); // S'assure que la méthode est appelable
    }

    @Test
    public void testGenererStatistiques() {
        centre.genererStatistiques(); // Doit s'exécuter sans erreur
    }

    @Test
    public void testTraiterRejet() {
        centre.traiterRejet(); // Doit s'exécuter sans erreur
    }

    @Test
    public void testAnalyserDepotsParQuartier() {
        centre.analyserDepotsParQuartier(); // Doit s'exécuter sans erreur
    }

    @Test
    public void testAnalyserDepotsParType() {
        centre.analyserDepotsParType(); // Doit s'exécuter sans erreur
    }
}
