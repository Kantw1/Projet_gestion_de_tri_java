package testDAO;

import dao.CategorieProduitDAO;
import dao.CommerceCategorieProduitDAO;
import dao.CommerceDAO;
import dao.CentreDeTriDAO;
import model.CategorieProduit;
import model.CentreDeTri;
import model.Commerce;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CommerceCategorieProduitDAOTest {

    private static Connection conn;
    private static CommerceCategorieProduitDAO ccDAO;
    private static CategorieProduitDAO categorieDAO;
    private static CommerceDAO commerceDAO;
    private static Commerce commerce;
    private static CategorieProduit categorie;

    @BeforeAll
    static void setup() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BDD", "root", "");

        ccDAO = new CommerceCategorieProduitDAO(conn);
        commerceDAO = new CommerceDAO(conn);
        categorieDAO = new CategorieProduitDAO(conn);

        CentreDeTriDAO centreDAO = new CentreDeTriDAO(conn);
        CentreDeTri centre = new CentreDeTri(0, "CentreDAO", "Zone A");
        centreDAO.insert(centre);
        centre = centreDAO.getAll().get(centreDAO.getAll().size() - 1);

        commerce = new Commerce(0, "Decathlon", centre);
        commerceDAO.insert(commerce);
        commerce = commerceDAO.getAll(centre).get(commerceDAO.getAll(centre).size() - 1);

        categorie = new CategorieProduit(0, "Sport", 100, 0.25f);
        categorieDAO.insert(categorie);
        categorie = categorieDAO.getAll().get(categorieDAO.getAll().size() - 1);
    }

    @Test
    @Order(1)
    void testInsertAndGetCategories() throws SQLException {
        ccDAO.insert(commerce.getId(), categorie.getId());

        List<Integer> categories = ccDAO.getCategoriesByCommerce(commerce.getId());
        assertNotNull(categories);
        assertTrue(categories.contains(categorie.getId()));
    }

    @Test
    @Order(2)
    void testDelete() throws SQLException {
        ccDAO.insert(commerce.getId(), categorie.getId());
        ccDAO.delete(commerce.getId(), categorie.getId());

        List<Integer> categories = ccDAO.getCategoriesByCommerce(commerce.getId());
        assertNotNull(categories);
        assertFalse(categories.contains(categorie.getId()));
    }

    @Test
    @Order(3)
    void testGetCommercesByCategorie() throws SQLException {
        ccDAO.insert(commerce.getId(), categorie.getId());

        List<Integer> commerces = ccDAO.getCommercesByCategorie(categorie.getId());
        assertNotNull(commerces);
        assertTrue(commerces.contains(commerce.getId()));

        ccDAO.delete(commerce.getId(), categorie.getId());
    }

    @AfterAll
    static void teardown() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
}