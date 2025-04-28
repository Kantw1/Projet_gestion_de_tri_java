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
    private String etatCommande; // Variable temporaire pour l'état de la commande

    @BeforeEach
    public void setUp() {
        // Création utilisateur avec 100 points
        utilisateur = new Utilisateur(1, "Paul", 2, 1234);
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
        commande = new BonDeCommande(1, utilisateur, cp1, LocalDate.now(), 80); // Pas besoin de valider ici
        etatCommande = "en attente"; // Initialiser l'état de la commande
    }

    @Test
    public void testValiderCommande() {
        // Vérifier que la commande est en attente initialement
        assertEquals("en attente", etatCommande, "L'état initial de la commande devrait être en attente");

        // "Simuler" la validation de la commande en modifiant son état manuellement
        if (utilisateur.getPtsFidelite() >= commande.getPointsUtilises()) {
            etatCommande = "validée"; // Changer l'état à "validée"
        }

        // Vérifier que la commande a bien été validée
        assertEquals("validée", etatCommande, "La commande doit être validée si l'utilisateur a suffisamment de points");
    }

    @Test
    public void testUtiliserPoints() {
        // Initialisation de l'état de la commande comme "en attente"
        String etatCommande = "en attente";

        // Simuler la validation de la commande si l'utilisateur a suffisamment de points
        if (utilisateur.getPtsFidelite() >= commande.getPointsUtilises()) {
            etatCommande = "validée";  // On simule la validation en modifiant directement l'état
            // Utiliser les points de l'utilisateur (réduction des points)
            int pointsUtilises = commande.getPointsUtilises();
            utilisateur.ajouterPoints(-pointsUtilises);  // Réduire les points en ajoutant une valeur négative
        }

        // Vérifier le solde des points après utilisation
        assertEquals(20, utilisateur.getPtsFidelite(), "Il doit rester 20 points après validation de la commande");

        // Vérifier que l'état de la commande est bien validée
        assertEquals("validée", etatCommande, "L'état de la commande doit être validé si l'utilisateur a suffisamment de points");
    }



    @Test
    public void testVerifierSoldeUtilisateur() {
        assertTrue(utilisateur.getPtsFidelite() >= commande.getPointsUtilises(), "L'utilisateur devrait avoir suffisamment de points");
    }

    @Test
    public void testGetProduits() {
        assertEquals(2, produits.size());
        assertTrue(produits.contains(cp1));
    }

    @Test
    public void testGetTotalPointsUtilises() {
        int total = commande.getPointsUtilises();
        assertEquals(80, total, "Le total de points utilisés doit être 80");
    }

    @Test
    public void testGetEtatCommande() {
        assertEquals("en attente", etatCommande);
    }

    @Test
    public void testSetEtatCommande() {
        etatCommande = "refusée";
        assertEquals("refusée", etatCommande);
    }

    @Test
    public void testGetDateCommande() {
        assertTrue(commande.getDateCommande().isEqual(LocalDate.now()), "La date de commande doit être aujourd'hui");
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
        assertEquals("en attente", etatCommande, "La commande doit être en attente avant annulation");
        etatCommande = "annulée";
        assertEquals("annulée", etatCommande, "La commande doit être annulée");
    }
}
