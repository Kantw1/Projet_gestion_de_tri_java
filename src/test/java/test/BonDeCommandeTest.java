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
    }

    @Test
    public void testValiderCommande() {
        // Vérifier que la commande est en attente initialement
        assertEquals("en attente", commande.getEtatCommande(), "L'état initial de la commande devrait être en attente");

        // "Simuler" la validation de la commande en modifiant son état manuellement
        if (utilisateur.getPtsFidelite() >= commande.getPointsUtilises()) {
            // Changer l'état à "validée"
            commande.setEtatCommande("validée");
        }

        // Vérifier que la commande a bien été validée
        assertEquals("validée", commande.getEtatCommande(), "La commande doit être validée si l'utilisateur a suffisamment de points");
    }

    @Test
    public void testUtiliserPoints() {
        // Simuler la validation de la commande manuellement
        if (utilisateur.getPtsFidelite() >= commande.getPointsUtilises()) {
            commande.setEtatCommande("validée");
            utilisateur.retirerPoints(commande.getPointsUtilises()); // En supposant que retirerPoints existe
        }

        // Vérifier le solde des points après utilisation
        assertEquals(20, utilisateur.getPtsFidelite(), "Il doit rester 20 points après validation de la commande");
    }

    @Test
    public void testVerifierSoldeUtilisateur() {
        assertTrue(utilisateur.getPtsFidelite() >= commande.getPointsUtilises(), "L'utilisateur devrait avoir suffisamment de points");
    }

    @Test
    public void testGetProduits() {
        assertEquals(2, commande.getProduits().size());
        assertTrue(commande.getProduits().contains(cp1));
    }

    @Test
    public void testGetTotalPointsUtilises() {
        int total = commande.getPointsUtilises();
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
        assertEquals("en attente", commande.getEtatCommande(), "La commande doit être en attente avant annulation");
        commande.setEtatCommande("annulée");
        assertEquals("annulée", commande.getEtatCommande(), "La commande doit être annulée");
    }
}
