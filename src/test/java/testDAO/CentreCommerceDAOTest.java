package testDAO;

import dao.*;
import model.*;

import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CentreCommerceDAOTest {

    private static Connection conn;
    private static CentreCommerceDAO ccDAO;
    private static CentreDeTri centre;
    private static Commerce commerce;

    @BeforeAll
    static void setup() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BDD", "root", "");

        ccDAO = new CentreCommerceDAO(conn);

        CentreDeTriDAO centreDAO = new CentreDeTriDAO(conn);
        centre = new CentreDeTri(0, "Centre CC", "Zone Centre");
        centreDAO.insert(centre);
        centre = centreDAO.getAll().get(centreDAO.getAll().size() - 1);

        CommerceDAO commerceDAO = new CommerceDAO(conn);
        commerce = new Commerce(0, "Fnac", centre);
        commerceDAO.insert(commerce);
        commerce = commerceDAO.getAll(centre).get(commerceDAO.getAll(centre).size() - 1);
    }

    @Test
    void testInsertAndGet() throws Exception {
        ccDAO.insert(centre.getId(), commerce.getId());

        List<Integer> commerces = ccDAO.getCommercesByCentre(centre.getId());
        assertTrue(commerces.contains(commerce.getId()));

        List<Integer> centres = ccDAO.getCentresByCommerce(commerce.getId());
        assertTrue(centres.contains(centre.getId()));
    }

    @Test
    void testDelete() throws Exception {
        if (!ccDAO.exists(centre.getId(), commerce.getId())) {
            ccDAO.insert(centre.getId(), commerce.getId());
        }

        ccDAO.delete(centre.getId(), commerce.getId());

        List<Integer> commerces = ccDAO.getCommercesByCentre(centre.getId());
        assertFalse(commerces.contains(commerce.getId()));
    }

    @AfterAll
    static void teardown() throws Exception {
        if (conn != null && !conn.isClosed()) conn.close();
    }
}
