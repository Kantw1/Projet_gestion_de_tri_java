package test;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BonDeCommandeTest {

    private Utilisateur utilisateur;
    private CategorieProduit cp1;
    private CategorieProduit cp2;
    private List<CategorieProduit> produits;
    private Commerce commerce;
    private CentreDeTri centre;
    private BonDeCommande commande;

    @BeforeEach
    public void setUp() {
        // Création utilisateur avec 100 points
        utilisateur = new Utilisateur(1, "Utilisateur", 1234);
        utilisateur.ajouterPoints(100);

        // Deux catégories à échanger
        cp1 = new CategorieProduit(1, "Cat1", 30, 0.2f);
        cp2 = new CategorieProduit(2, "Cat2", 50, 0.3f);
        produits = new ArrayList<>();
        produits.add(cp1);
        produits.add(cp2);

        // Commerce attaché à un centre fictif
        centre = new CentreDeTri(1, "Centre Test", "Adresse Centre");
        commerce = new Commerce(1, "Commerce Test", centre);

        // Création de la commande
        commande = new BonDeCommande(1, utilisateur, produits, commerce);
    }

    @Test
    public void testValiderCommande() {
        boolean result = commande.validerCommande();
        assertTrue(result, "La commande doit être validée");
        assertEquals("validée", commande.getEtatCommande());
    }

    @Test
    public void testUtiliserPoints() {
        commande.utiliserPoints();
        assertEquals(20, utilisateur.getPtsFidelite(), "Il doit rester 20 points après utilisation");
    }

    @Test
    public void testVerifierSoldeUtilisateur() {
        assertTrue(commande.verifierSoldeUtilisateur());
    }

    @Test
    public void testGetProduits() {
        assertEquals(2, commande.getProduits().size());
        assertTrue(commande.getProduits().contains(cp1));
    }

    @Test
    public void testGetTotalPointsUtilises() {
        int total = commande.getTotalPointsUtilises();
        assertEquals(80, total, "Le total de points utilisés doit être 80");
    }

    @Test
    public void testGetEtatCommande() {
        assertEquals("en attente", commande.getEtatCommande());
    }

    @Test
    public void testSetEtatCommande() {
        commande.setEtatCommande("refusée");
        assertEquals("refusée", commande.getEtatCommande());
    }

    @Test
    public void testGetDateCommande() {
        assertEquals(LocalDate.now(), commande.getDateCommande());
    }

    @Test
    public void testGetCommerce() {
        assertEquals(commerce, commande.getCommerce());
    }

    @Test
    public void testGetUtilisateur() {
        assertEquals(utilisateur, commande.getUtilisateur());
    }

    @Test
    public void testAnnulerCommande() {
        boolean result = commande.annulerCommande();
        assertTrue(result);
        assertEquals("annulée", commande.getEtatCommande());
    }
}
