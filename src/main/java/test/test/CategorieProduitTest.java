package test;

import model.CategorieProduit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CategorieProduitTest {

    private CategorieProduit categorie;

    @BeforeEach
    public void setUp() {
        categorie = new CategorieProduit(1, "Hygiène", 50, 0.2f);
    }

    @Test
    public void testGetNom() {
        assertEquals("Hygiène", categorie.getNom());
    }

    @Test
    public void testAssocierProduit() {
        categorie.associerProduit("ProduitA");
        assertTrue(categorie.getProduits().contains("ProduitA"));
    }

    @Test
    public void testVerifierProduit() {
        categorie.associerProduit("ProduitB");
        assertTrue(categorie.verifierProduit("ProduitB"));
    }

    @Test
    public void testGetPointNecessaire() {
        assertEquals(50, categorie.getPointNecessaire());
    }

    @Test
    public void testGetBonReduction() {
        assertEquals(0.2f, categorie.getBonReduction());
    }

    @Test
    public void testEstEligible() {
        assertTrue(categorie.estEligible(60));
        assertFalse(categorie.estEligible(30));
    }

    @Test
    public void testAppliquerReduction() {
        float prixInitial = 100f;
        float prixFinal = categorie.appliquerReduction(prixInitial);
        assertEquals(80f, prixFinal);
    }

    @Test
    public void testRetirerProduit() {
        categorie.associerProduit("ProduitC");
        categorie.retirerProduit("ProduitC");
        assertFalse(categorie.getProduits().contains("ProduitC"));
    }

    @Test
    public void testGetProduits() {
        categorie.associerProduit("ProduitD");
        List<String> produits = categorie.getProduits();
        assertEquals(1, produits.size());
        assertTrue(produits.contains("ProduitD"));
    }
}
