package test;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CommerceTest {

    private Commerce commerce;
    private CentreDeTri centre;
    private CategorieProduit cat1;
    private Utilisateur utilisateur;
    private BonDeCommande commande;
    private ContratPartenariat contrat;

    @BeforeEach
    public void setUp() {
        centre = new CentreDeTri(1, "Centre_1", "Adresse Test");
        commerce = new Commerce(1, "Commerce Test", centre);

        utilisateur = new Utilisateur(1, "Client", 1234);
        utilisateur.ajouterPoints(100);

        cat1 = new CategorieProduit(1, "Produit", 50, 0.2f);
        commerce.ajouterCategorie(cat1);

        contrat = new ContratPartenariat(1,
                LocalDate.now().minusDays(5),
                LocalDate.now().plusDays(5),
                centre, commerce);

        contrat.ajouterCategorie(cat1); // ✅ AJOUT À FAIRE pour passer les tests

        commerce.setContrat(contrat);

        List<CategorieProduit> produits = new ArrayList<>();
        produits.add(cat1);
        commande = new BonDeCommande(1, utilisateur, produits, commerce);
    }


    @Test
    public void testEchangerPoints() {
        boolean result = commerce.echangerPoints(utilisateur, cat1);
        assertTrue(result);
        assertEquals(50, utilisateur.getPtsFidelite(), "Les points doivent être déduits après l'échange");
    }

    @Test
    public void testGetCategoriesProduits() {
        List<CategorieProduit> categories = commerce.getCategoriesProduits();
        assertEquals(1, categories.size());
        assertTrue(categories.contains(cat1));
    }

    @Test
    public void testVerifierConditionsContrat() {
        assertTrue(commerce.verifierConditionsContrat(contrat));
    }

    @Test
    public void testAccepterCommande() {
        assertTrue(commerce.accepterCommande(commande));
        assertTrue(commerce.getHistoriqueCommandes().contains(commande));
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
    public void testGetCentre() {
        assertEquals(centre, commerce.getCentre());
    }

    @Test
    public void testGetNom() {
        assertEquals("Commerce Test", commerce.getNom());
    }

    @Test
    public void testGetContrat() {
        assertEquals(contrat, commerce.getContrat());
    }
}
