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
    void setUp() {
        centre = new CentreDeTri(1, "Centre_1", "Adresse Test");
        commerce = new Commerce(1, "Commerce Test", centre);

<<<<<<< HEAD
        utilisateur = new Utilisateur(1, "Client", 1234, "utilisateur", 1);
        utilisateur.ajouterPoints(100);
        assertEquals(100, utilisateur.getPtsFidelite(), "L'utilisateur doit commencer avec 100 points");
=======
        utilisateur = new Utilisateur(1, "Client", 100, 1);

        utilisateur.ajouterPoints(100); // Ajout de 100 points
>>>>>>> b0bec017eb47513429820aa01dc801d454906266

        cat1 = new CategorieProduit(1, "Produit", 50, 0.2f);
        commerce.ajouterCategorie(cat1);

        contrat = new ContratPartenariat(
                1,
                LocalDate.now().minusDays(5),
                LocalDate.now().plusDays(5),
                centre,
                commerce
        );

        contrat.ajouterCategorie(cat1);
        commerce.setContrat(contrat);

<<<<<<< HEAD
        commande = new BonDeCommande(1, utilisateur, cat1, LocalDate.now(), cat1.getPointNecessaire());
=======
        List<CategorieProduit> produits = new ArrayList<>();
        produits.add(cat1);
        commande = new BonDeCommande(1, utilisateur, cat1, LocalDate.now(), 0);
>>>>>>> b0bec017eb47513429820aa01dc801d454906266
    }

    @Test
    void testEchangerPoints() {
        boolean result = commerce.echangerPoints(utilisateur, cat1);
        assertTrue(result, "L'échange de points doit réussir");
        assertEquals(50, utilisateur.getPtsFidelite(), "Il doit rester 50 points après échange");
    }

    @Test
    void testGetCategoriesProduits() {
        List<CategorieProduit> categories = commerce.getCategoriesProduits();
        assertNotNull(categories, "La liste des catégories ne doit pas être nulle");
        assertEquals(1, categories.size(), "Il doit y avoir une catégorie");
        assertTrue(categories.contains(cat1), "La catégorie initiale doit être présente");
    }

    @Test
    void testVerifierConditionsContrat() {
        assertTrue(commerce.verifierConditionsContrat(commerce.getContrat()), "Le contrat doit être valide");
    }

    @Test
    void testAccepterCommande() {
        assertTrue(commerce.accepterCommande(commande), "La commande doit être acceptée");
        assertTrue(commerce.getHistoriqueCommandes().contains(commande), "La commande doit être dans l'historique");
    }

    @Test
    void testGetReductionPourCategorie() {
        float reduction = commerce.getReductionPourCategorie(cat1);
        assertEquals(0.2f, reduction, 0.0001, "La réduction doit être 0.2");
    }

    @Test
    void testAjouterCategorie() {
        CategorieProduit nouvelle = new CategorieProduit(2, "Autre", 60, 0.15f);
        commerce.ajouterCategorie(nouvelle);
        assertTrue(commerce.getCategoriesProduits().contains(nouvelle), "La nouvelle catégorie doit être ajoutée");
    }

    @Test
    void testSupprimerCategorie() {
        commerce.supprimerCategorie(cat1);
        assertFalse(commerce.getCategoriesProduits().contains(cat1), "La catégorie doit être supprimée");
    }

    @Test
    void testGetCentre() {
        assertEquals(centre, commerce.getCentre(), "Le centre associé doit être correct");
    }

    @Test
    void testGetNom() {
        assertEquals("Commerce Test", commerce.getNom(), "Le nom doit être 'Commerce Test'");
    }

    @Test
    void testGetContrat() {
        assertEquals(contrat, commerce.getContrat(), "Le contrat associé doit être correct");
    }
}
