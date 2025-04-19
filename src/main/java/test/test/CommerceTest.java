package test;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CommerceTest {

    private Commerce commerce;
    private CentreDeTri centre;
    private CategorieProduit cat1;
    private Utilisateur utilisateur;
    private BonDeCommande commande;

    @BeforeEach
    public void setUp() {
        centre = new CentreDeTri("Centre 1", "Adresse", 1);
        commerce = new Commerce("Commerce Test", centre);
        utilisateur = new Utilisateur(1, "Client", 1234);
        utilisateur.ajouterPoints(100);
        cat1 = new CategorieProduit(1, "Produit", 50, 0.2f);

        List<CategorieProduit> produits = new ArrayList<>();
        produits.add(cat1);

        commande = new BonDeCommande(1, utilisateur, produits, commerce);
        commerce.setContrat(new ContratPartenariat(1,
                java.time.LocalDate.now().minusDays(1),
                java.time.LocalDate.now().plusDays(1),
                centre, commerce));
        commerce.ajouterCategorie(cat1);
    }

    @Test
    public void testEchangerPoints() {
        boolean result = commerce.echangerPoints(utilisateur, cat1);
        assertTrue(result);
    }

    @Test
    public void testGetCategoriesProduits() {
        List<CategorieProduit> categories = commerce.getCategoriesProduits();
        assertTrue(categories.contains(cat1));
    }

    @Test
    public void testVerifierConditionsContrat() {
        boolean result = commerce.verifierConditionsContrat(commerce.getContrat());
        assertTrue(result);
    }

    @Test
    public void testAccepterCommande() {
        boolean result = commerce.accepterCommande(commande);
        assertTrue(result);
    }

    @Test
    public void testGetReductionPourCategorie() {
        float reduc = commerce.getReductionPourCategorie(cat1);
        assertEquals(0.2f, reduc);
    }

    @Test
    public void testAjouterCategorie() {
        CategorieProduit nouvelle = new CategorieProduit(2, "Autre", 60, 0.15f);
        commerce.ajouterCategorie(nouvelle);
        assertTrue(commerce.getCategoriesProduits().contains(nouvelle));
    }

    @Test
    public void testSupprimerCategorie() {
        commerce.supprimerCategorie(cat1);
        assertFalse(commerce.getCategoriesProduits().contains(cat1));
    }

    @Test
    public void testGetHistoriqueCommandes() {
        commerce.accepterCommande(commande);
        assertTrue(commerce.getHistoriqueCommandes().contains(commande));
    }

    @Test
    public void testGetCentre() {
        assertEquals(centre, commerce.getCentre());
    }

    @Test
    public void testGetNom() {
        assertEquals("Commerce Test", commerce.getNom());
    }

    @Test
    public void testGetContrat() {
        assertNotNull(commerce.getContrat());
    }
}
