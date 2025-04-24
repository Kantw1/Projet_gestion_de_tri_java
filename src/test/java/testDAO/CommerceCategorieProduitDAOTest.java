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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

        // Centre
        CentreDeTriDAO centreDAO = new CentreDeTriDAO(conn);
        CentreDeTri centre = new CentreDeTri(0, "CentreDAO", "Zone A");
        centreDAO.insert(centre);
        centre = centreDAO.getAll().get(centreDAO.getAll().size() - 1);

        // Commerce
        commerce = new Commerce(0, "Decathlon", centre);
        commerceDAO.insert(commerce);
        commerce = commerceDAO.getAll(centre).get(commerceDAO.getAll(centre).size() - 1);

        // Catégorie
        categorie = new CategorieProduit(0, "Sport", 100, 0.25f);
        categorieDAO.insert(categorie);
        categorie = categorieDAO.getAll().get(categorieDAO.getAll().size() - 1);
    }

    @Test
    void testInsertAndGetCategories() throws Exception {
        ccDAO.insert(commerce.getId(), categorie.getId());

        List<Integer> categories = ccDAO.getCategoriesByCommerce(commerce.getId());
        assertTrue(categories.contains(categorie.getId()));
    }

    @Test
    void testDelete() throws Exception {
        ccDAO.insert(commerce.getId(), categorie.getId());
        ccDAO.delete(commerce.getId(), categorie.getId());

        List<Integer> categories = ccDAO.getCategoriesByCommerce(commerce.getId());
        assertFalse(categories.contains(categorie.getId()));
    }
    @Test
    void testGetCommercesByCategorie() throws Exception {
        ccDAO.insert(commerce.getId(), categorie.getId());

        List<Integer> commerces = ccDAO.getCommercesByCategorie(categorie.getId());
        assertTrue(commerces.contains(commerce.getId()));

        // nettoyage
        ccDAO.delete(commerce.getId(), categorie.getId());
    }


    @AfterAll
    static void teardown() throws Exception {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
}
