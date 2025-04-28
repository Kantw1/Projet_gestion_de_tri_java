package test;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UtilisateurTest {

    private Utilisateur utilisateur;
    private Poubelle poubelle;
    private Depot depot;
    private CentreDeTri centreDeTri;

    @BeforeEach
    void setUp() {
        centreDeTri = new CentreDeTri(1, "Centre Principal", "Adresse Centre"); // ✅ Correction : constructeur complet
        utilisateur = new Utilisateur(1, "Alice", 1234, "utilisateur", 1);
        utilisateur.ajouterPoints(100);

        poubelle = new Poubelle(1, 100, "Chatelet", TypePoubelle.JAUNE, 5, 90, centreDeTri);
        utilisateur.ajouterPoubelleAccessible(poubelle);

        // Autoriser accès
        poubelle.getAccesAutorises().add(utilisateur.getCodeAcces());

        depot = new Depot(1, NatureDechet.PLASTIQUE, 1.0f, 2, LocalDateTime.now(), poubelle, utilisateur);
        utilisateur.deposerDechets(poubelle, depot);
    }

    @Test
    void testDeposerDechets() {
        assertEquals(1, utilisateur.getHistoriqueDepots().size());
        assertEquals(depot, utilisateur.getHistoriqueDepots().get(0));
    }

    @Test
    void testConsulterHistorique() {
        List<Depot> historique = utilisateur.getHistoriqueDepots();
        assertEquals(1, historique.size());
        assertEquals(depot, historique.get(0));
    }

    @Test
    void testConvertirPoints() {
        CategorieProduit categorie = new CategorieProduit(1, "Test", 50, 0.2f);
        int pointsAvant = utilisateur.getPtsFidelite();

        BonDeCommande commande = utilisateur.convertirPoints(categorie);

        assertNotNull(commande);
        assertEquals(50, commande.getPointsUtilises());
        assertEquals(pointsAvant - 50, utilisateur.getPtsFidelite());
    }

    @Test
    void testGetCodeAcces() {
        assertEquals(1234, utilisateur.getCodeAcces());
    }

    @Test
    void testGetNom() {
        assertEquals("Alice", utilisateur.getNom());
    }

    @Test
    void testGetPtsFidelite() {
        assertTrue(utilisateur.getPtsFidelite() >= 0);
    }

    @Test
    void testAcheterProduits() {
        CategorieProduit produit = new CategorieProduit(1, "Test", 30, 0.2f);
        int pointsAvant = utilisateur.getPtsFidelite();

        boolean result = utilisateur.acheterProduits(produit);
        assertTrue(result);
        assertEquals(pointsAvant - produit.getPointNecessaire(), utilisateur.getPtsFidelite());
    }

    @Test
    void testGetRole() {
        assertEquals("utilisateur", utilisateur.getRole());
    }

    @Test
    void testSetRole() {
        utilisateur.setRole("admin");
        assertEquals("admin", utilisateur.getRole());
    }
}
