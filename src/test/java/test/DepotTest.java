package test;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class DepotTest {

    private Depot depot;
    private Utilisateur utilisateur;
    private Poubelle poubelle;

    @BeforeEach
    public void setUp() {
        utilisateur = new Utilisateur(1, "Test", 1234);
        poubelle = new Poubelle(1, 100, "Zone A", TypePoubelle.JAUNE, 80);
        poubelle.getAccesAutorises().add(1234);
        depot = new Depot(1, NatureDechet.PLASTIQUE, 1.0f, 2, LocalDateTime.now(), poubelle, utilisateur);
    }

    @Test
    public void testGetId() {
        assertEquals(1, depot.getId());
    }

    @Test
    public void testGetType() {
        assertEquals(NatureDechet.PLASTIQUE, depot.getType());
    }

    @Test
    public void testGetPoids() {
        assertEquals(1.0f, depot.getPoids());
    }

    @Test
    public void testGetQuantite() {
        assertEquals(2, depot.getQuantite());
    }

    @Test
    public void testGetHeureDepot() {
        assertNotNull(depot.getHeureDepot());
    }

    @Test
    public void testGetPoints() {
        assertEquals(depot.getQuantite() * 2, depot.getPoints());
    }

    @Test
    public void testGetPoubelle() {
        assertEquals(poubelle, depot.getPoubelle());
    }

    @Test
    public void testGetUtilisateur() {
        assertEquals(utilisateur, depot.getUtilisateur());
    }

    @Test
    public void testCalculerQuantiteDechets() {
        assertEquals(2, depot.calculerQuantiteDechets());
    }

    @Test
    public void testVerifierTypeDechet() {
        assertTrue(depot.verifierTypeDechet());
    }

    @Test
    public void testVerifierConformite() {
        assertTrue(depot.verifierConformite(poubelle));
    }

    @Test
    public void testAfficherDepot() {
        String result = depot.afficherDepot();
        assertTrue(result.contains("Dépôt"));
        assertTrue(result.contains("Points"));
    }
}
