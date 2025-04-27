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

public class DepotDAOTest {

    private static Connection conn;
    private static DepotDAO depotDAO;
    private static Utilisateur utilisateur;
    private static Poubelle poubelle;
    private static UtilisateurDAO utilisateurDAO;
    private static PoubelleDAO poubelleDAO;

    @BeforeAll
    static void setUp() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BDD", "root", "");

        utilisateurDAO = new UtilisateurDAO(conn);
        utilisateur = new Utilisateur(0, "Déposeur", 9999);
        utilisateur.setPtsFidelite(0);
        utilisateurDAO.insert(utilisateur);
        utilisateur = utilisateurDAO.getAll().get(utilisateurDAO.getAll().size() - 1); // dernier ajouté

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

        List<Depot> liste = depotDAO.getAll(poubelle, utilisateur);
        assertFalse(liste.isEmpty(), "La liste des dépôts ne doit pas être vide");

        Depot dernier = liste.get(liste.size() - 1);
        Depot recupere = depotDAO.getById(dernier.getId(), poubelle, utilisateur);
        assertNotNull(recupere);
        assertEquals(NatureDechet.PLASTIQUE, recupere.getType());
    }

    @Test
    void testUpdate() throws SQLException {
        List<Depot> liste = depotDAO.getAll(poubelle, utilisateur);
        assertFalse(liste.isEmpty());
        Depot d = liste.get(liste.size() - 1);

        Depot modifie = new Depot(d.getId(), NatureDechet.CARTON, 2.0f, 5, d.getHeureDepot(), poubelle, utilisateur);
        depotDAO.update(modifie);

        Depot verif = depotDAO.getById(modifie.getId(), poubelle, utilisateur);
        assertEquals(NatureDechet.CARTON, verif.getType());
    }

    @Test
    void testDelete() throws SQLException {
        Depot d = new Depot(0, NatureDechet.PAPIER, 1.0f, 2, LocalDateTime.now(), poubelle, utilisateur);
        depotDAO.insert(d);

        List<Depot> liste = depotDAO.getAll(poubelle, utilisateur);
        Depot dernier = liste.get(liste.size() - 1);
        int id = dernier.getId();

        depotDAO.delete(id);
        Depot supprime = depotDAO.getById(id, poubelle, utilisateur);
        assertNull(supprime);
    }

    @Test
    void testGetByPoubelleId() throws SQLException {
        Depot d = new Depot(0, NatureDechet.METAL, 2.5f, 4, LocalDateTime.now(), poubelle, utilisateur);
        depotDAO.insert(d);

        List<Depot> liste = depotDAO.getByPoubelleId(poubelle.getId());
        assertFalse(liste.isEmpty(), "Il devrait y avoir des dépôts pour cette poubelle");
    }

    @Test
    void testGetByUtilisateurId() throws SQLException {
        Depot d = new Depot(0, NatureDechet.VERRE, 3.0f, 5, LocalDateTime.now(), poubelle, utilisateur);
        depotDAO.insert(d);

        List<Depot> liste = depotDAO.getByUtilisateurId(utilisateur.getId());
        assertFalse(liste.isEmpty(), "Il devrait y avoir des dépôts pour cet utilisateur");
    }

    @Test
    void testGetByDateRange() throws SQLException {
        LocalDateTime now = LocalDateTime.now();
        Depot d = new Depot(0, NatureDechet.CARTON, 1.2f, 2, now, poubelle, utilisateur);
        depotDAO.insert(d);

        List<Depot> liste = depotDAO.getByDateRange(now.minusMinutes(10), now.plusMinutes(10));
        assertFalse(liste.isEmpty(), "Il devrait y avoir des dépôts dans la plage de dates");
    }

    @Test
    void testGetTotalPoidsByPoubelle() throws SQLException {
        float totalPoids = depotDAO.getTotalPoidsByPoubelle(poubelle.getId());
        assertTrue(totalPoids >= 0, "Le poids total doit être positif ou nul");
    }

    @AfterAll
    static void tearDown() throws SQLException {
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
