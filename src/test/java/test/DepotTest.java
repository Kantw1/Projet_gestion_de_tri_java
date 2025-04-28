package test;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DepotTest {

    private Depot depot;
    private Utilisateur utilisateur;
    private Poubelle poubelle;

    @BeforeEach
    void setUp() {
        utilisateur = new Utilisateur(1, "Test", 1234);
        poubelle = new Poubelle(1, 100, "Zone A", TypePoubelle.JAUNE, 80);
        poubelle.getAccesAutorises().add(1234);
        poubelle.getTypePoubelle().getTypesAcceptes().add(NatureDechet.PLASTIQUE);
        depot = new Depot(1, NatureDechet.PLASTIQUE, 1.0f, 2, LocalDateTime.now(), poubelle, utilisateur);
    }

    @Test
    void testGetId() {
        assertEquals(1, depot.getId());
    }

    @Test
    void testGetType() {
        assertEquals(NatureDechet.PLASTIQUE, depot.getType());
    }

    @Test
    void testGetPoids() {
        assertEquals(1.0f, depot.getPoids());
    }

    @Test
    void testGetQuantite() {
        assertEquals(2, depot.getQuantite());
    }

    @Test
    void testGetHeureDepot() {
        assertNotNull(depot.getHeureDepot());
    }

    @Test
    void testGetPoints() {
        assertEquals(4, depot.getPoints());
    }

    @Test
    void testGetPoubelle() {
        assertEquals(poubelle, depot.getPoubelle());
    }

    @Test
    void testGetUtilisateur() {
        assertEquals(utilisateur, depot.getUtilisateur());
    }

    @Test
    void testCalculerQuantiteDechets() {
        assertEquals(2, depot.calculerQuantiteDechets());
    }

    @Test
    void testVerifierTypeDechet() {
        assertTrue(depot.verifierTypeDechet());
    }

    @Test
    void testVerifierConformite() {
        assertTrue(depot.verifierConformite(poubelle));
    }

    @Test
    void testAfficherDepot() {
        String result = depot.afficherDepot();
        assertTrue(result.contains("Dépôt"));
        assertTrue(result.contains("Points"));
    }
}