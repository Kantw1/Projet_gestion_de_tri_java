package test;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UtilisateurTest {

    private Utilisateur utilisateur;
    private Poubelle poubelle;
    private Depot depot;

    @BeforeEach
    public void setUp() {
        utilisateur = new Utilisateur(1, "Alice", 1234, "utilisateur"); // 👈 Ajout du rôle explicitement
        utilisateur.ajouterPoints(100);

        poubelle = new Poubelle(1, 100, "Chatelet", TypePoubelle.JAUNE, 5);
        utilisateur.ajouterPoubelleAccessible(poubelle);

        // Important : autoriser le code d'accès pour le dépôt
        poubelle.getAccesAutorises().add(utilisateur.getCodeAcces());

        depot = new Depot(1, NatureDechet.PLASTIQUE, 1.0f, 2, LocalDateTime.now(), poubelle, utilisateur);
        utilisateur.deposerDechets(poubelle, depot);
    }

    @Test
    public void testDeposerDechets() {
        assertEquals(1, utilisateur.getHistoriqueDepots().size());
        assertEquals(depot, utilisateur.getHistoriqueDepots().get(0));
    }

    @Test
    public void testConsulterHistorique() {
        List<Depot> historique = utilisateur.getHistoriqueDepots();
        assertEquals(1, historique.size());
        assertEquals(depot, historique.get(0));
    }

    @Test
    public void testConvertirPoints() {
        int pointsAvant = utilisateur.getPtsFidelite();
        BonDeCommande commande = utilisateur.convertirPoints(50);

        assertNotNull(commande);
        assertEquals(50, commande.getPointsUtilises());
        assertEquals(pointsAvant - 50, utilisateur.getPtsFidelite());
    }

    @Test
    public void testGetCodeAcces() {
        assertEquals(1234, utilisateur.getCodeAcces());
    }

    @Test
    public void testGetNom() {
        assertEquals("Alice", utilisateur.getNom());
    }

    @Test
    public void testGetPtsFidelite() {
        assertTrue(utilisateur.getPtsFidelite() >= 0);
    }

    @Test
    public void testAcheterProduits() {
        CategorieProduit produit = new CategorieProduit(1, "Test", 30, 0.2f);
        int pointsAvant = utilisateur.getPtsFidelite();

        boolean result = utilisateur.acheterProduits(produit);
        assertTrue(result);
        assertEquals(pointsAvant - produit.getPointNecessaire(), utilisateur.getPtsFidelite());
    }

    @Test
    public void testGetRole() {
        assertEquals("utilisateur", utilisateur.getRole());
    }

    @Test
    public void testSetRole() {
        utilisateur.setRole("admin");
        assertEquals("admin", utilisateur.getRole());
    }
}
