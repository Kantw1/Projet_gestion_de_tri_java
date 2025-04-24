package testDAO;

import dao.PoubelleDAO;
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

    @BeforeAll
    static void setUp() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver"); // important pour enregistrer le driver
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BDD", "root", "");
        poubelleDAO = new PoubelleDAO(conn);
    }

    @Test
    void testInsertAndGetById() throws SQLException {
        Poubelle p = new Poubelle(0, 200, "TestRue", TypePoubelle.JAUNE, 80);
        poubelleDAO.insert(p);

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
        List<Poubelle> liste = poubelleDAO.getAll();
        assertFalse(liste.isEmpty());

        Poubelle p = liste.get(0);
        p = new Poubelle(p.getId(), p.getCapaciteMax(), "RueModifiée", p.getTypePoubelle(), p.getSeuilAlerte());
        poubelleDAO.update(p);

        Poubelle modifiee = poubelleDAO.getById(p.getId());
        assertEquals("RueModifiée", modifiee.getEmplacement());
    }

    @AfterAll
    static void tearDown() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
    @AfterEach
    void cleanup() throws SQLException {
        List<Poubelle> liste = poubelleDAO.getAll();
        for (Poubelle p : liste) {
            if (p.getEmplacement().startsWith("Test")) {
                // Suppression possible si méthode delete() est implémentée
                poubelleDAO.delete(p.getId());
            }
        }
    }

}

