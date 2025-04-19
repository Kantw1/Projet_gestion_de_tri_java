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
    private BonDeCommande commande;

    @BeforeEach
    public void setUp() {
        utilisateur = new Utilisateur(1, "Utilisateur", 1234);
        utilisateur.ajouterPoints(100);

        cp1 = new CategorieProduit(1, "Cat1", 30, 0.2f);
        cp2 = new CategorieProduit(2, "Cat2", 50, 0.3f);
        produits = new ArrayList<>();
        produits.add(cp1);
        produits.add(cp2);

        commerce = new Commerce("Commerce Test", null);
        commande = new BonDeCommande(1, utilisateur, produits, commerce);
    }

    @Test
    public void testValiderCommande() {
        boolean result = commande.validerCommande();
        assertTrue(result);
        assertEquals("validée", commande.getEtatCommande());
    }

    @Test
    public void testUtiliserPoints() {
        commande.utiliserPoints();
        assertEquals(20, utilisateur.getPtsFidelite());
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
        assertEquals(80, total);
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
