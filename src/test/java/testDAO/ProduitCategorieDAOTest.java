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

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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

        CategorieProduit cat = new CategorieProduit(0, "Tests", 10, 0.1f);
        categorieDAO.insert(cat);
        List<CategorieProduit> categories = categorieDAO.getAll();
        categorieID = categories.get(categories.size() - 1).getId();
    }

    @Test
    @Order(1)
    void testInsertAndGet() throws SQLException {
        produitCategorieDAO.insert("ProduitTest1", categorieID);
        produitCategorieDAO.insert("ProduitTest2", categorieID);

        List<String> produits = produitCategorieDAO.getProduitsByCategorie(categorieID);
        assertNotNull(produits);
        assertTrue(produits.contains("ProduitTest1"));
        assertTrue(produits.contains("ProduitTest2"));
    }

    @Test
    @Order(2)
    void testDeleteByCategorie() throws SQLException {
        produitCategorieDAO.deleteByCategorie(categorieID);

        List<String> produits = produitCategorieDAO.getProduitsByCategorie(categorieID);
        assertNotNull(produits);
        assertTrue(produits.isEmpty());
    }

    @AfterAll
    static void teardown() throws SQLException {
        if (categorieDAO != null && categorieID != 0) {
            categorieDAO.delete(categorieID);
        }
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
}