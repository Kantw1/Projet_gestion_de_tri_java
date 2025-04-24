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
        categorieDAO.insert(cp);

        List<CategorieProduit> toutes = categorieDAO.getAll();
        assertFalse(toutes.isEmpty());

        CategorieProduit derniere = toutes.get(toutes.size() - 1);
        CategorieProduit chargee = categorieDAO.getById(derniere.getId());

        assertNotNull(chargee);
        assertEquals("Culture", chargee.getNom());
        assertEquals(40, chargee.getPointNecessaire());
        assertEquals(0.25f, chargee.getBonReduction());
    }

    @Test
    void testUpdate() throws SQLException {
        List<CategorieProduit> liste = categorieDAO.getAll();
        assertFalse(liste.isEmpty());

        CategorieProduit cp = liste.get(0);
        CategorieProduit modifie = new CategorieProduit(cp.getId(), "Loisirs", 60, 0.3f);
        categorieDAO.update(modifie);

        CategorieProduit verif = categorieDAO.getById(modifie.getId());
        assertEquals("Loisirs", verif.getNom());
        assertEquals(60, verif.getPointNecessaire());
        assertEquals(0.3f, verif.getBonReduction());
    }

    @AfterAll
    static void teardown() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
    @Test
    void testDelete() throws SQLException {
        CategorieProduit cp = new CategorieProduit(0, "Test Delete", 70, 0.15f);
        categorieDAO.insert(cp);

        List<CategorieProduit> liste = categorieDAO.getAll();
        CategorieProduit derniere = liste.get(liste.size() - 1);
        int idASupprimer = derniere.getId();

        categorieDAO.delete(idASupprimer);
        CategorieProduit supprimee = categorieDAO.getById(idASupprimer);
        assertNull(supprimee, "La catégorie supprimée ne devrait plus être retrouvée");
    }

}
