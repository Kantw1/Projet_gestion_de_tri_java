package testDAO;

import dao.DepotDAO;
import dao.PoubelleDAO;
import dao.UtilisateurDAO;
import model.*;

import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DepotDAOTest {

    private Connection conn;
    private DepotDAO depotDAO;
    private Utilisateur utilisateur;
    private Poubelle poubelle;
    private UtilisateurDAO utilisateurDAO;
    private PoubelleDAO poubelleDAO;

    @BeforeAll
    void setUp() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BDD", "root", "");

        utilisateurDAO = new UtilisateurDAO(conn);
        utilisateur = new Utilisateur(0, "Déposeur", 9999);
        utilisateur.setPtsFidelite(0);
        utilisateurDAO.insert(utilisateur);
        utilisateur = utilisateurDAO.getAll().get(utilisateurDAO.getAll().size() - 1);

        poubelleDAO = new PoubelleDAO(conn);
        poubelle = new Poubelle(0, 100, "TestQuartier", TypePoubelle.JAUNE, 70);
        poubelleDAO.insert(poubelle);
        poubelle = poubelleDAO.getAll().get(poubelleDAO.getAll().size() - 1);

        depotDAO = new DepotDAO(conn);
    }

    @Test
    void testInsertAndGet() throws SQLException {
        Depot d = new Depot(0, NatureDechet.PLASTIQUE, 1.5f, 3, LocalDateTime.now(), poubelle, utilisateur);
        depotDAO.insert(d);

        Depot recupere = depotDAO.getById(d.getId(), poubelle, utilisateur);
        assertNotNull(recupere);
        assertEquals(NatureDechet.PLASTIQUE, recupere.getType());
    }

    @Test
    void testUpdate() throws SQLException {
        Depot d = new Depot(0, NatureDechet.PAPIER, 1.0f, 2, LocalDateTime.now(), poubelle, utilisateur);
        depotDAO.insert(d);

        d.setType(NatureDechet.CARTON);
        d.setPoids(2.0f);
        d.setQuantite(5);
        depotDAO.update(d);

        Depot verif = depotDAO.getById(d.getId(), poubelle, utilisateur);
        assertEquals(NatureDechet.CARTON, verif.getType());
        assertEquals(2.0f, verif.getPoids());
        assertEquals(5, verif.getQuantite());
    }

    @Test
    void testDelete() throws SQLException {
        Depot d = new Depot(0, NatureDechet.VERRE, 1.0f, 2, LocalDateTime.now(), poubelle, utilisateur);
        depotDAO.insert(d);

        int id = d.getId();
        depotDAO.delete(id);

        Depot supprime = depotDAO.getById(id, poubelle, utilisateur);
        assertNull(supprime);
    }

    @Test
    void testGetByPoubelleId() throws SQLException {
        Depot d = new Depot(0, NatureDechet.METAL, 2.5f, 4, LocalDateTime.now(), poubelle, utilisateur);
        depotDAO.insert(d);

        List<Depot> liste = depotDAO.getByPoubelleId(poubelle.getId());
        assertTrue(liste.stream().anyMatch(dep -> dep.getId() == d.getId()));
    }

    @Test
    void testGetByUtilisateurId() throws SQLException {
        Depot d = new Depot(0, NatureDechet.VERRE, 3.0f, 5, LocalDateTime.now(), poubelle, utilisateur);
        depotDAO.insert(d);

        List<Depot> liste = depotDAO.getByUtilisateurId(utilisateur.getId());
        assertTrue(liste.stream().anyMatch(dep -> dep.getId() == d.getId()));
    }

    @Test
    void testGetByDateRange() throws SQLException {
        LocalDateTime now = LocalDateTime.now();
        Depot d = new Depot(0, NatureDechet.CARTON, 1.2f, 2, now, poubelle, utilisateur);
        depotDAO.insert(d);

        List<Depot> liste = depotDAO.getByDateRange(now.minusMinutes(5), now.plusMinutes(5));
        assertTrue(liste.stream().anyMatch(dep -> dep.getId() == d.getId()));
    }

    @Test
    void testGetTotalPoidsByPoubelle() throws SQLException {
        Depot d = new Depot(0, NatureDechet.PLASTIQUE, 1.5f, 3, LocalDateTime.now(), poubelle, utilisateur);
        depotDAO.insert(d);

        float totalPoids = depotDAO.getTotalPoidsByPoubelle(poubelle.getId());
        assertTrue(totalPoids >= d.getPoids(), "Le poids total doit être au moins égal au poids inséré");
    }

    @AfterAll
    void tearDown() throws SQLException {
        if (depotDAO != null) {
            List<Depot> allDepots = depotDAO.getAll(poubelle, utilisateur);
            for (Depot d : allDepots) {
                depotDAO.delete(d.getId());
            }
        }
        if (poubelleDAO != null) {
            poubelleDAO.delete(poubelle.getId());
        }
        if (utilisateurDAO != null) {
            utilisateurDAO.delete(utilisateur.getId());
        }
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
}