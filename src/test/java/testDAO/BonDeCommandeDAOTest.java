package testDAO;

import dao.*;
import model.*;

import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;


import static org.junit.jupiter.api.Assertions.*;

public class BonDeCommandeDAOTest {

    private static Connection conn;
    private static BonDeCommandeDAO bdcDAO;
    private static Utilisateur utilisateur;
    private static Commerce commerce;
    private static CategorieProduit categorie1;
    private static List<CategorieProduit> categories;

    @BeforeAll
    static void setup() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BDD", "root", "");

        // Création des dépendances via DAO
        UtilisateurDAO utilisateurDAO = new UtilisateurDAO(conn);
        utilisateur = new Utilisateur(0, "Testeur", 1234);
        utilisateur.ajouterPoints(200);
        utilisateurDAO.insert(utilisateur);
        utilisateur = utilisateurDAO.getAll().get(utilisateurDAO.getAll().size() - 1);

        CentreDeTriDAO centreDAO = new CentreDeTriDAO(conn);
        CentreDeTri centre = new CentreDeTri(0, "Centre 1", "Adresse");
        centreDAO.insert(centre);
        centre = centreDAO.getAll().get(centreDAO.getAll().size() - 1);

        CommerceDAO commerceDAO = new CommerceDAO(conn);
        commerce = new Commerce(0, "Carrefour", centre);
        commerceDAO.insert(commerce);
        commerce = commerceDAO.getAll(centre).get(commerceDAO.getAll(centre).size() - 1);

        CategorieProduitDAO categorieDAO = new CategorieProduitDAO(conn);
        categorie1 = new CategorieProduit(0, "Alimentation", 80, 0.15f);
        categorieDAO.insert(categorie1);
        categorie1 = categorieDAO.getAll().get(categorieDAO.getAll().size() - 1);

        categories = new ArrayList<>();
        categories.add(categorie1);

        bdcDAO = new BonDeCommandeDAO(conn);
    }

    @Test
    void testInsertAndGet() throws SQLException {
        BonDeCommande bdc = new BonDeCommande(0, utilisateur, categories, commerce);
        bdc.utiliserPoints(); // simule la validation
        bdcDAO.insert(bdc);

        List<BonDeCommande> liste = bdcDAO.getAll(utilisateur, commerce, categories);
        assertFalse(liste.isEmpty());

        BonDeCommande dernier = liste.get(liste.size() - 1);
        BonDeCommande charge = bdcDAO.getById(dernier.getId(), utilisateur, commerce, categories);

        assertNotNull(charge);
        assertEquals("en attente", charge.getEtatCommande()); // avant validation
    }

    @Test
    void testUpdate() throws SQLException {
        List<BonDeCommande> liste = bdcDAO.getAll(utilisateur, commerce, categories);
        assertFalse(liste.isEmpty());

        BonDeCommande bdc = liste.get(0);
        bdc.setEtatCommande("validée");
        bdcDAO.update(bdc);

        BonDeCommande verif = bdcDAO.getById(bdc.getId(), utilisateur, commerce, categories);
        assertEquals("validée", verif.getEtatCommande());
    }

    @AfterAll
    static void tearDown() throws SQLException {
        if (conn != null && !conn.isClosed()) conn.close();
    }
    @Test
    void testDelete() throws SQLException {
        BonDeCommande bdc = new BonDeCommande(0, utilisateur, categories, commerce);
        bdc.utiliserPoints();
        bdcDAO.insert(bdc);

        List<BonDeCommande> toutes = bdcDAO.getAll(utilisateur, commerce, categories);
        BonDeCommande dernier = toutes.get(toutes.size() - 1);

        // Appel direct au DAO
        bdcDAO.delete(dernier.getId());

        BonDeCommande supprimee = bdcDAO.getById(dernier.getId(), utilisateur, commerce, categories);
        assertNull(supprimee, "La commande doit avoir été supprimée");
    }
}
