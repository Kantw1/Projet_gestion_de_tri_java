package testDAO;

import dao.CategorieProduitDAO;
import dao.ProduitCategorieDAO;
import model.CategorieProduit;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProduitCategorieDAOTest {

    private static Connection conn;
    private static ProduitCategorieDAO produitCategorieDAO;
    private static CategorieProduitDAO categorieDAO;
    private static int categorieID;

    @BeforeAll
    static void setup() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BDD", "root", "");
        produitCategorieDAO = new ProduitCategorieDAO(conn);
        categorieDAO = new CategorieProduitDAO(conn);

        // Création d'une catégorie test
        CategorieProduit cat = new CategorieProduit(0, "Tests", 10, 0.1f);
        categorieDAO.insert(cat);
        categorieID = categorieDAO.getAll().get(categorieDAO.getAll().size() - 1).getId();
    }

    @Test
    void testInsertAndGet() throws SQLException {
        produitCategorieDAO.insert("ProduitTest1", categorieID);
        produitCategorieDAO.insert("ProduitTest2", categorieID);

        List<String> produits = produitCategorieDAO.getProduitsByCategorie(categorieID);
        assertTrue(produits.contains("ProduitTest1"));
        assertTrue(produits.contains("ProduitTest2"));
    }

    @Test
    void testDeleteByCategorie() throws SQLException {
        produitCategorieDAO.deleteByCategorie(categorieID);
        List<String> produits = produitCategorieDAO.getProduitsByCategorie(categorieID);
        assertTrue(produits.isEmpty());
    }

    @AfterAll
    static void teardown() throws SQLException {
        if (conn != null && !conn.isClosed()) conn.close();
    }
}
