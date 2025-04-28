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
    private CentreDeTri centre;

    @BeforeEach
    void setUp() {
        centre = new CentreDeTri(1, "Centre_1", "Adresse Test");
        utilisateur = new Utilisateur(1, "Test", 1234, 1); // Ajout du centreId obligatoire
        poubelle = new Poubelle(1, 100, "Zone A", TypePoubelle.JAUNE, 80, 80, centre); // Tous les paramètres nécessaires

        // Ajout du code d'accès autorisé
        poubelle.getAccesAutorises().add(1234);

        // Plus besoin d'ajouter NatureDechet manuellement (TypePoubelle.JAUNE accepte déjà PLASTIQUE)

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
        assertEquals(1.0f, depot.getPoids(), 0.0001);
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
        assertTrue(result.contains("Type"));
        assertTrue(result.contains("Points"));
        assertTrue(result.contains("Poids"));
        assertTrue(result.contains("Quantité"));
    }
}
