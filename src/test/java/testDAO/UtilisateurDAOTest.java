package testDAO;

import dao.CentreDeTriDAO;
import dao.UtilisateurDAO;
import model.CentreDeTri;
import model.Utilisateur;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UtilisateurDAOTest {

    private Connection conn;
    private UtilisateurDAO utilisateurDAO;
    private CentreDeTri centre;

    @BeforeAll
    void setup() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BDD", "root", "");

        CentreDeTriDAO centreDAO = new CentreDeTriDAO(conn);
        centre = new CentreDeTri(0, "Centre Utilisateurs Test", "Adresse Test");
        centreDAO.insert(centre);
        centre = centreDAO.getAll().get(centreDAO.getAll().size() - 1);

        utilisateurDAO = new UtilisateurDAO(conn);
    }

    @Test
    void testInsertAndGetById() throws SQLException {
        Utilisateur u = new Utilisateur(0, "Alice", 1111, "utilisateur", centre.getId());
        u.setPtsFidelite(100);
        utilisateurDAO.insert(u);

        List<Utilisateur> liste = utilisateurDAO.getAll();
        assertFalse(liste.isEmpty(), "La liste d'utilisateurs ne doit pas être vide");

        Utilisateur dernier = liste.get(liste.size() - 1);
        Utilisateur charge = utilisateurDAO.getById(dernier.getId());

        assertNotNull(charge);
        assertEquals("Alice", charge.getNom());
        assertEquals(100, charge.getPtsFidelite());
        assertEquals(1111, charge.getCodeAcces());
    }

    @Test
    void testUpdate() throws SQLException {
        Utilisateur u = new Utilisateur(0, "Modifiable", 2222, "utilisateur", centre.getId());
        utilisateurDAO.insert(u);

        Utilisateur dernier = utilisateurDAO.getAll().get(utilisateurDAO.getAll().size() - 1);
        dernier.setNom("Modifié");
        dernier.setPtsFidelite(200);
        utilisateurDAO.update(dernier);

        Utilisateur verif = utilisateurDAO.getById(dernier.getId());
        assertEquals("Modifié", verif.getNom());
        assertEquals(200, verif.getPtsFidelite());
    }

    @Test
    void testDelete() throws SQLException {
        Utilisateur u = new Utilisateur(0, "Temporaire", 3333, "utilisateur", centre.getId());
        utilisateurDAO.insert(u);

        Utilisateur dernier = utilisateurDAO.getAll().get(utilisateurDAO.getAll().size() - 1);
        int idASupprimer = dernier.getId();

        utilisateurDAO.delete(idASupprimer);
        Utilisateur supprime = utilisateurDAO.getById(idASupprimer);
        assertNull(supprime, "L'utilisateur doit être supprimé");
    }

    @Test
    void testGetByCodeAcces() throws SQLException {
        Utilisateur u = new Utilisateur(0, "Jean", 9999, "utilisateur", centre.getId());
        utilisateurDAO.insert(u);

        Utilisateur recupere = utilisateurDAO.getByCodeAcces(9999);
        assertNotNull(recupere);
        assertEquals("Jean", recupere.getNom());
    }

    @Test
    void testUpdateFidelite() throws SQLException {
        Utilisateur u = new Utilisateur(0, "PointsOnly", 1234, "utilisateur", centre.getId());
        utilisateurDAO.insert(u);

        Utilisateur inserted = utilisateurDAO.getAll().get(utilisateurDAO.getAll().size() - 1);
        utilisateurDAO.updateFidelite(inserted.getId(), 999);

        Utilisateur updated = utilisateurDAO.getById(inserted.getId());
        assertEquals(999, updated.getPtsFidelite(), "La fidélité doit être mise à jour");
    }

    @Test
    void testIsAdmin() throws SQLException {
        Utilisateur admin = new Utilisateur(0, "AdminTest", 4321, "admin", -1);
        utilisateurDAO.insert(admin);

        Utilisateur recupere = utilisateurDAO.getByCodeAcces(4321);
        assertNotNull(recupere);
        assertTrue(utilisateurDAO.isAdmin(recupere.getId()), "Cet utilisateur doit être administrateur");
    }

    @AfterEach
    void cleanup() throws SQLException {
        List<Utilisateur> utilisateurs = utilisateurDAO.getAll();
        for (Utilisateur u : utilisateurs) {
            if (u.getNom().equals("Alice") || u.getNom().equals("Modifiable") ||
                    u.getNom().equals("Modifié") || u.getNom().equals("Temporaire") ||
                    u.getNom().equals("Jean") || u.getNom().equals("PointsOnly") ||
                    u.getNom().equals("AdminTest")) {
                utilisateurDAO.delete(u.getId());
            }
        }
    }

    @AfterAll
    void teardown() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
}
