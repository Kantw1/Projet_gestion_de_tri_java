package testDAO;

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

public class PoubelleDAOTest {

    private static Connection conn;
    private static PoubelleDAO poubelleDAO;
    private static int centreIdTest = 1; // Utiliser un ID de centre de tri existant pour les tests

    @BeforeAll
    static void setUp() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BDD", "root", "");
        poubelleDAO = new PoubelleDAO(conn);
    }

    @Test
    void testInsertAndGetById() throws SQLException {
        Poubelle p = new Poubelle(0, 200, "TestRue", TypePoubelle.JAUNE, 80, 20);
        poubelleDAO.insert(p, centreIdTest);

        List<Poubelle> toutes = poubelleDAO.getAll();
        assertFalse(toutes.isEmpty(), "La liste des poubelles ne doit pas être vide");

        Poubelle derniere = toutes.get(toutes.size() - 1);
        Poubelle p2 = poubelleDAO.getById(derniere.getId());
        assertNotNull(p2);
        assertEquals("TestRue", p2.getEmplacement());
        assertEquals(TypePoubelle.JAUNE, p2.getTypePoubelle());
    }

    @Test
    void testUpdate() throws SQLException {
        Poubelle p = new Poubelle(0, 200, "TestUpdateRue", TypePoubelle.VERTE, 50, 10);
        poubelleDAO.insert(p, centreIdTest);

        List<Poubelle> liste = poubelleDAO.getAll();
        assertFalse(liste.isEmpty());

        Poubelle derniere = liste.get(liste.size() - 1);
        derniere.setEmplacement("RueModifiee");
        poubelleDAO.update(derniere);

        Poubelle modifiee = poubelleDAO.getById(derniere.getId());
        assertEquals("RueModifiee", modifiee.getEmplacement());
    }

    @AfterEach
    void cleanup() throws SQLException {
        List<Poubelle> liste = poubelleDAO.getAll();
        for (Poubelle p : liste) {
            if (p.getEmplacement().startsWith("Test")) {
                poubelleDAO.delete(p.getId());
            }
        }
    }

    @AfterAll
    static void tearDown() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
}