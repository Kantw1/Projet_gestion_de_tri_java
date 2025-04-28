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
    public void testGetId() {
        assertEquals(1, categorie.getId(), "L'id de la catégorie doit être 1");
    }

    @Test
    public void testGetNom() {
        assertEquals("Hygiène", categorie.getNom(), "Le nom de la catégorie doit être 'Hygiène'");
    }

    @Test
    public void testAssocierProduit() {
        categorie.associerProduit("ProduitA");
        assertTrue(categorie.getProduits().contains("ProduitA"), "La liste doit contenir 'ProduitA'");
    }

    @Test
    public void testAssocierProduitDejaPresent() {
        categorie.associerProduit("ProduitB");
        categorie.associerProduit("ProduitB"); // tentative d'ajout du même produit
        assertEquals(1, categorie.getProduits().size(), "Le produit ne doit pas être ajouté deux fois");
    }

    @Test
    public void testVerifierProduit() {
        categorie.associerProduit("ProduitC");
        assertTrue(categorie.verifierProduit("ProduitC"), "ProduitC doit être présent");
        assertFalse(categorie.verifierProduit("ProduitInexistant"), "ProduitInexistant ne doit pas être trouvé");
    }

    @Test
    public void testGetPointNecessaire() {
        assertEquals(50, categorie.getPointNecessaire(), "Le nombre de points nécessaires doit être 50");
    }

    @Test
    public void testGetBonReduction() {
        assertEquals(0.2f, categorie.getBonReduction(), 0.0001, "La réduction doit être 0.2 (20%)");
    }

    @Test
    public void testEstEligible() {
        assertTrue(categorie.estEligible(60), "60 points doivent rendre éligible");
        assertTrue(categorie.estEligible(50), "50 points doivent aussi rendre éligible");
        assertFalse(categorie.estEligible(49), "49 points ne doivent pas suffire");
    }

    @Test
    public void testAppliquerReduction() {
        float prixInitial = 100f;
        float prixReduit = categorie.appliquerReduction(prixInitial);
        assertEquals(80f, prixReduit, 0.001f, "Le prix réduit doit être 80.0 avec 20% de réduction");
    }

    @Test
    public void testRetirerProduit() {
        categorie.associerProduit("ProduitD");
        categorie.retirerProduit("ProduitD");
        assertFalse(categorie.getProduits().contains("ProduitD"), "ProduitD doit avoir été retiré");
    }

    @Test
    public void testGetProduits() {
        categorie.associerProduit("ProduitE");
        List<String> produits = categorie.getProduits();
        assertEquals(1, produits.size(), "Il doit y avoir un produit associé");
        assertTrue(produits.contains("ProduitE"), "ProduitE doit être dans la liste");
    }
}