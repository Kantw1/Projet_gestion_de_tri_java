package testDAO;

import dao.*;
import model.*;

import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BonDeCommandeDAOTest {

    private static Connection conn;
    private static BonDeCommandeDAO bdcDAO;
    private static Utilisateur utilisateur;
    private static CategorieProduit categorie1;

    @BeforeAll
    static void setup() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BDD", "root", "");

        CentreDeTriDAO centreDAO = new CentreDeTriDAO(conn);
        CentreDeTri centre = new CentreDeTri(0, "Centre 1", "Adresse Test");
        centreDAO.insert(centre);
        centre = centreDAO.getAll().get(centreDAO.getAll().size() - 1);

        UtilisateurDAO utilisateurDAO = new UtilisateurDAO(conn);
        utilisateur = new Utilisateur(0, "Testeur", 1234, "utilisateur", centre.getId());
        utilisateurDAO.insert(utilisateur);
        utilisateur = utilisateurDAO.getAll().get(utilisateurDAO.getAll().size() - 1);

        CategorieProduitDAO categorieDAO = new CategorieProduitDAO(conn);
        categorie1 = new CategorieProduit(0, "Alimentation", 80, 0.15f);
        categorieDAO.insert(categorie1);
        categorie1 = categorieDAO.getAll().get(categorieDAO.getAll().size() - 1);

        bdcDAO = new BonDeCommandeDAO(conn);
    }

    @Test
    void testInsertAndGet() throws SQLException {
        BonDeCommande bdc = new BonDeCommande(0, utilisateur, categorie1, LocalDate.now(), 80);
        bdcDAO.insert(bdc);

        List<BonDeCommande> liste = bdcDAO.getByUtilisateurId(utilisateur.getId());
        assertFalse(liste.isEmpty(), "La liste ne doit pas être vide après insertion");

        BonDeCommande dernier = liste.get(liste.size() - 1);
        assertEquals(categorie1.getNom(), dernier.getCategorieProduit().getNom(), "La catégorie du bon doit correspondre");
        assertEquals(80, dernier.getPointsUtilises(), "Les points utilisés doivent correspondre");
    }

    @Test
    void testDelete() throws SQLException {
        BonDeCommande bdc = new BonDeCommande(0, utilisateur, categorie1, LocalDate.now(), 80);
        bdcDAO.insert(bdc);

        List<BonDeCommande> toutes = bdcDAO.getByUtilisateurId(utilisateur.getId());
        BonDeCommande dernier = toutes.get(toutes.size() - 1);

        bdcDAO.delete(dernier.getId());

        List<BonDeCommande> apresSuppression = bdcDAO.getByUtilisateurId(utilisateur.getId());
        boolean existeEncore = apresSuppression.stream().anyMatch(b -> b.getId() == dernier.getId());
        assertFalse(existeEncore, "Le bon de commande doit avoir été supprimé");
    }

    @AfterAll
    static void tearDown() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
}
