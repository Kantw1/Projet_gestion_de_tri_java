package testDAO;

import dao.CentreDeTriDAO;
import dao.PoubelleDAO;
import model.CentreDeTri;
import model.Poubelle;
import model.TypePoubelle;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PoubelleDAOTest {

    private Connection conn;
    private PoubelleDAO poubelleDAO;
    private CentreDeTri centre;

    @BeforeAll
    void setUp() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BDD", "root", "");

        CentreDeTriDAO centreDAO = new CentreDeTriDAO(conn);
        centre = new CentreDeTri(0, "Centre Test Poubelle", "Adresse Test");
        centreDAO.insert(centre);
        centre = centreDAO.getAll().get(centreDAO.getAll().size() - 1);

        poubelleDAO = new PoubelleDAO(conn);
    }

    @Test
    @Order(1)
    void testInsertAndGetById() throws SQLException {
        Poubelle p = new Poubelle(0, 200, "TestRue1", TypePoubelle.JAUNE, 80, 20, centre);
        poubelleDAO.insert(p, centre.getId());

        List<Poubelle> toutes = poubelleDAO.getAll();
        assertFalse(toutes.isEmpty(), "La liste des poubelles ne doit pas être vide après insertion");

        Poubelle derniere = toutes.get(toutes.size() - 1);
        Poubelle p2 = poubelleDAO.getById(derniere.getId());

        assertNotNull(p2, "La poubelle récupérée ne doit pas être nulle");
        assertEquals("TestRue1", p2.getEmplacement(), "L'emplacement doit correspondre à 'TestRue1'");
        assertEquals(TypePoubelle.JAUNE, p2.getTypePoubelle(), "Le type de poubelle doit correspondre à JAUNE");
    }

    @Test
    @Order(2)
    void testInsertSecondAndCheck() throws SQLException {
        Poubelle p = new Poubelle(0, 250, "TestRue2", TypePoubelle.VERTE, 60, 25, centre);
        poubelleDAO.insert(p, centre.getId());

        List<Poubelle> toutes = poubelleDAO.getAll();
        assertFalse(toutes.isEmpty(), "La liste des poubelles doit contenir des éléments");

        boolean found = toutes.stream().anyMatch(pb -> "TestRue2".equals(pb.getEmplacement()));
        assertTrue(found, "Une poubelle avec emplacement 'TestRue2' doit exister");
    }

    @AfterEach
    void cleanup() throws SQLException {
        List<Poubelle> liste = poubelleDAO.getAll();
        for (Poubelle p : liste) {
            if (p.getEmplacement().startsWith("TestRue")) {
                poubelleDAO.delete(p.getId());
            }
        }
    }

    @AfterAll
    void tearDown() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
}
