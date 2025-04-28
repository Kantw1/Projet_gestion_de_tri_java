package testDAO;

import dao.CategorieProduitDAO;
import model.CategorieProduit;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CategorieProduitDAOTest {

    private static Connection conn;
    private static CategorieProduitDAO categorieDAO;

    @BeforeAll
    static void setup() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BDD", "root", "");
        categorieDAO = new CategorieProduitDAO(conn);
    }

    @Test
    void testInsertAndGet() throws SQLException {
        CategorieProduit cp = new CategorieProduit(0, "Culture", 40, 0.25f);
        int idGenere = categorieDAO.insertAndGetId(cp);

        CategorieProduit chargee = categorieDAO.getById(idGenere);

        assertNotNull(chargee);
        assertEquals("Culture", chargee.getNom());
        assertEquals(40, chargee.getPointNecessaire());
        assertEquals(0.25f, chargee.getBonReduction());
    }

    @Test
    void testUpdate() throws SQLException {
        CategorieProduit cp = new CategorieProduit(0, "Sport", 50, 0.20f);
        int idGenere = categorieDAO.insertAndGetId(cp);

        CategorieProduit modifie = new CategorieProduit(idGenere, "Loisirs", 60, 0.3f);
        categorieDAO.update(modifie);

        CategorieProduit verif = categorieDAO.getById(modifie.getId());
        assertEquals("Loisirs", verif.getNom());
        assertEquals(60, verif.getPointNecessaire());
        assertEquals(0.3f, verif.getBonReduction());
    }

    @Test
    void testDelete() throws SQLException {
        CategorieProduit cp = new CategorieProduit(0, "Test Delete", 70, 0.15f);
        int idGenere = categorieDAO.insertAndGetId(cp);

        categorieDAO.delete(idGenere);
        CategorieProduit supprimee = categorieDAO.getById(idGenere);
        assertNull(supprimee, "La catégorie supprimée ne devrait plus être retrouvée");
    }

    @AfterAll
    static void teardown() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
}