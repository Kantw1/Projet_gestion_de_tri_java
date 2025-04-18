package test;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UtilisateurTest {

    private Utilisateur utilisateur;
    private Poubelle poubelle;
    private Depot depot;

    @BeforeEach
    public void setUp() {
        utilisateur = new Utilisateur(1, "Alice", 100, 1234);
        poubelle = new Poubelle(1, 100, "Entrée", TypePoubelle.JAUNE);
        poubelle.ajouterUtilisateurAutorise(utilisateur);
        utilisateur.ajouterPoubelleAccessible(poubelle);

        depot = new Depot(1, NatureDechet.PLASTIQUE, "1.0kg", 2, java.time.LocalDateTime.now(), poubelle, utilisateur);
        utilisateur.deposerDechets(poubelle, depot);
    }

    @Test
    public void testDeposerDechets() {
        assertEquals(1, utilisateur.getHistoriqueDepots().size());
        assertEquals(depot, utilisateur.getHistoriqueDepots().get(0));
    }

    @Test
    public void testConsulterHistorique() {
        List<Depot> historique = utilisateur.consulterHistorique();
        assertEquals(1, historique.size());
        assertEquals(depot, historique.get(0));
    }

    @Test
    public void testConvertirPoints() {
        BonDeCommande commande = utilisateur.convertirPoints(50);
        assertNotNull(commande);
        assertEquals(50, commande.getPointsUtilises());
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
        // Créer un produit nécessitant moins de points que ce que l'utilisateur possède
        List<String> nomsProduits = new ArrayList<>();
        nomsProduits.add("ProduitTest");
        CategorieProduit produit = new CategorieProduit(1, "Test", nomsProduits, 30, 0.2f);

        boolean result = utilisateur.acheterProduits(produit);
        assertTrue(result);
        assertTrue(utilisateur.getPtsFidelite() < 100); // Vérifie que les points ont été retirés
    }
}
