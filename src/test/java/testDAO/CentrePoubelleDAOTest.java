package testDAO;

import dao.CentreDeTriDAO;
import dao.CentrePoubelleDAO;
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
public class CentrePoubelleDAOTest {

    private Connection conn;
    private CentrePoubelleDAO cpDAO;
    private CentreDeTri centre;
    private Poubelle poubelle;

    @BeforeAll
    void setup() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BDD", "root", "");
        cpDAO = new CentrePoubelleDAO(conn);

        CentreDeTriDAO centreDAO = new CentreDeTriDAO(conn);
        centre = new CentreDeTri(0, "Centre Test CP", "Rue des Tests");
        centreDAO.insert(centre);
        centre = centreDAO.getAll().get(centreDAO.getAll().size() - 1);

        PoubelleDAO poubelleDAO = new PoubelleDAO(conn);
        poubelle = new Poubelle(0, 120, "Zone CP", TypePoubelle.JAUNE, 30, 90, centre);
        poubelleDAO.insert(poubelle, centre.getId());
        poubelle = poubelleDAO.getAll().get(poubelleDAO.getAll().size() - 1);
    }

    @Test
    @Order(1)
    void testLienCentrePoubelleExiste() throws SQLException {
        List<Integer> poubelles = cpDAO.getPoubellesByCentre(centre.getId());
        assertNotNull(poubelles, "Liste des poubelles liée au centre ne doit pas être nulle");
        assertTrue(poubelles.contains(poubelle.getId()), "La poubelle doit être liée automatiquement au centre");

        List<Integer> centres = cpDAO.getCentresByPoubelle(poubelle.getId());
        assertNotNull(centres, "Liste des centres liée à la poubelle ne doit pas être nulle");
        assertTrue(centres.contains(centre.getId()), "Le centre doit être lié automatiquement à la poubelle");
    }

    @Test
    @Order(2)
    void testDeleteLienCentrePoubelle() throws SQLException {
        if (!cpDAO.exists(centre.getId(), poubelle.getId())) {
            fail("Le lien centre-poubelle n'existe pas alors qu'il devrait exister");
        }

        cpDAO.delete(centre.getId(), poubelle.getId());

        List<Integer> poubelles = cpDAO.getPoubellesByCentre(centre.getId());
        assertNotNull(poubelles, "Liste des poubelles après suppression ne doit pas être nulle");
        assertFalse(poubelles.contains(poubelle.getId()), "La poubelle ne doit plus être liée au centre");

        List<Integer> centres = cpDAO.getCentresByPoubelle(poubelle.getId());
        assertNotNull(centres, "Liste des centres après suppression ne doit pas être nulle");
        assertFalse(centres.contains(centre.getId()), "Le centre ne doit plus être lié à la poubelle");
    }

    @AfterAll
    void teardown() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
}
