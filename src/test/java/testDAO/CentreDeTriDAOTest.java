package testDAO;

import dao.CentreDeTriDAO;
import model.CentreDeTri;

import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CentreDeTriDAOTest {

    private static Connection conn;
    private static CentreDeTriDAO centreDAO;

    @BeforeAll
    static void setup() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BDD", "root", "");
        centreDAO = new CentreDeTriDAO(conn);
    }

    @Test
    void testInsertAndGet() throws SQLException {
        CentreDeTri centre = new CentreDeTri(0, "Centre Test", "Rambouillet");
        centreDAO.insert(centre);

        List<CentreDeTri> liste = centreDAO.getAll();
        assertFalse(liste.isEmpty());

        CentreDeTri dernier = liste.get(liste.size() - 1);
        CentreDeTri c = centreDAO.getById(dernier.getId());

        assertNotNull(c);
        assertEquals("Centre Test", c.getNom());
    }

    @Test
    void testUpdate() throws SQLException {
        List<CentreDeTri> liste = centreDAO.getAll();
        assertFalse(liste.isEmpty());

        CentreDeTri c = liste.get(0);
        CentreDeTri modifie = new CentreDeTri(c.getId(), "Centre Modifié", c.getAdresse());
        centreDAO.update(modifie);

        CentreDeTri verif = centreDAO.getById(modifie.getId());
        assertEquals("Centre Modifié", verif.getNom());
    }

    @AfterAll
    static void teardown() throws SQLException {
        if (conn != null && !conn.isClosed()) conn.close();
    }
    @Test
    void testDelete() throws SQLException {
        CentreDeTri centre = new CentreDeTri(0, "Centre Temp", "TestAdresse");
        centreDAO.insert(centre);

        List<CentreDeTri> liste = centreDAO.getAll();
        CentreDeTri dernier = liste.get(liste.size() - 1);

        // Suppose que tu ajoutes cette méthode dans CentreDeTriDAO
        centreDAO.delete(dernier.getId());

        CentreDeTri supprime = centreDAO.getById(dernier.getId());
        assertNull(supprime, "Le centre doit avoir été supprimé");
    }

}
