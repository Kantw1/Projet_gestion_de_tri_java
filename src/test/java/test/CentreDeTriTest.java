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
        // Assurez-vous que le constructeur de Poubelle a la signature correcte sans ajouter de méthodes.
        // Ajouter le paramètre manquant pour le constructeur de Poubelle (CentreDeTri)
        poubelle = new Poubelle(1, 100, "Zone 1", TypePoubelle.JAUNE, 1, centre); // Maintenant, le constructeur a 6 arguments

        commerce = new Commerce(1, "Commerce 1", centre);

        // Utilisation de la méthode de dépôt si possible.
        depotValide = new Depot(1, NatureDechet.PLASTIQUE, 10, 1, LocalDateTime.now(), poubelle, utilisateur);
        depotInvalide = new Depot(2, NatureDechet.VERRE, 5, 1, LocalDateTime.now(), poubelle, utilisateur);

        // Si vous ne pouvez pas utiliser "ajouterDepot", assurez-vous de gérer autrement les dépôts.
        // Si vous avez une méthode dans CentreDeTri pour l'ajout de dépôts, utilisez-la.
        centre.ajouterDepot(depotValide);
        centre.ajouterDepot(depotInvalide);
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

