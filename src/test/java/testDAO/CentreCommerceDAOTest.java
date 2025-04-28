package testDAO;

import dao.*;
import model.*;

import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CentreCommerceDAOTest {

    private static Connection conn;
    private static CentreCommerceDAO ccDAO;
    private static CentreDeTriDAO centreDAO;
    private static CommerceDAO commerceDAO;
    private CentreDeTri centre;
    private Commerce commerce;

    @BeforeAll
    static void setupAll() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BDD", "root", "");
        ccDAO = new CentreCommerceDAO(conn);
        centreDAO = new CentreDeTriDAO(conn);
        commerceDAO = new CommerceDAO(conn);
    }

    @BeforeEach
    void setup() throws SQLException {
        centre = new CentreDeTri(0, "Centre Test", "Zone Test");
        centreDAO.insert(centre);
        centre = centreDAO.getAll().get(centreDAO.getAll().size() - 1);

        commerce = new Commerce(0, "Commerce Test", centre);
        commerceDAO.insert(commerce);
        commerce = commerceDAO.getAll(centre).get(commerceDAO.getAll(centre).size() - 1);
    }

    @Test
    void testInsertAndGet() throws SQLException {
        ccDAO.insert(centre.getId(), commerce.getId());

        List<Integer> commerces = ccDAO.getCommercesByCentre(centre.getId());
        assertTrue(commerces.contains(commerce.getId()));

        List<Integer> centres = ccDAO.getCentresByCommerce(commerce.getId());
        assertTrue(centres.contains(centre.getId()));
    }

    @Test
    void testDelete() throws SQLException {
        if (!ccDAO.exists(centre.getId(), commerce.getId())) {
            ccDAO.insert(centre.getId(), commerce.getId());
        }

        ccDAO.delete(centre.getId(), commerce.getId());

        List<Integer> commerces = ccDAO.getCommercesByCentre(centre.getId());
        assertFalse(commerces.contains(commerce.getId()));
    }

    @AfterEach
    void cleanup() throws SQLException {
        commerceDAO.delete(commerce.getId());
        centreDAO.delete(centre.getId());
    }

    @AfterAll
    static void teardownAll() throws SQLException {
        if (conn != null && !conn.isClosed()) conn.close();
    }
}