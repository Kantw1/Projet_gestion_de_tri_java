package test;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CentreDeTriTest {

    private CentreDeTri centre;
    private Poubelle poubelle;
    private Commerce commerce;
    private Depot depotValide;
    private Depot depotInvalide;
    private Utilisateur utilisateur;

    @BeforeEach
    public void setUp() {
        centre = new CentreDeTri(7, "Centre Principal", "1 rue de la medaille");

        poubelle = new Poubelle(1, 100, "Zone 1", TypePoubelle.JAUNE, 0, 10, centre);
        //                             ↑↑    ↑↑
        // Niveau de remplissage: 0    Points associés: 10 (par exemple)

        commerce = new Commerce(1, "Commerce 1", centre);

        utilisateur = new Utilisateur(1, "John Doe", 0, 1);


        depotValide = new Depot(1, NatureDechet.PLASTIQUE, 10.0f, 0, LocalDateTime.now(), poubelle, utilisateur);
        depotInvalide = new Depot(2, NatureDechet.VERRE, 5.0f, 0, LocalDateTime.now(), poubelle, utilisateur);

        poubelle.getHistoriqueDepots().add(depotValide);
        poubelle.getHistoriqueDepots().add(depotInvalide);

        centre.ajouterPoubelle(poubelle);
    }

    @Test
    public void testAjouterPoubelle() {
        assertTrue(centre.getPoubelle().contains(poubelle));
    }

    @Test
    public void testRetirerPoubelle() {
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
        assertTrue(quartiers.isEmpty());
    }

    @Test
    public void testGetHistoriqueRejets() {
        List<Depot> rejets = centre.getHistoriqueRejets();
        assertNotNull(rejets);
        assertTrue(rejets.isEmpty());
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
        assertEquals(7, centre.getId());
    }

    @Test
    public void testCollecterDechets() {
        assertDoesNotThrow(() -> centre.collecterDechets());
    }

    @Test
    public void testGenererStatistiques() {
        assertDoesNotThrow(() -> centre.genererStatistiques());
    }

    @Test
    public void testTraiterRejet() {
        // Avant traitement
        assertEquals(2, poubelle.getHistoriqueDepots().size());

        centre.traiterRejet();

        // Après traitement : le dépôt invalide doit être déplacé dans historiqueRejets
        assertEquals(1, poubelle.getHistoriqueDepots().size());
        assertEquals(1, centre.getHistoriqueRejets().size());
        assertEquals(depotInvalide, centre.getHistoriqueRejets().get(0));
    }

    @Test
    public void testAnalyserDepotsParQuartier() {
        assertDoesNotThrow(() -> centre.analyserDepotsParQuartier());
    }

    @Test
    public void testAnalyserDepotsParType() {
        assertDoesNotThrow(() -> centre.analyserDepotsParType());
    }
}

