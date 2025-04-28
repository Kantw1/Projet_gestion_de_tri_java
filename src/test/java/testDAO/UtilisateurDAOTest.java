package testDAO;

import dao.UtilisateurDAO;
import model.Utilisateur;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UtilisateurDAOTest {

    private static Connection conn;
    private static UtilisateurDAO utilisateurDAO;

    @BeforeAll
    static void setup() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BDD", "root", "");
        utilisateurDAO = new UtilisateurDAO(conn);
    }

    @Test
    void testInsertAndGetById() throws SQLException {
        Utilisateur u = new Utilisateur(0, "Alice", 1111);
        u.setPtsFidelite(100);
        utilisateurDAO.insert(u);

        List<Utilisateur> liste = utilisateurDAO.getAll();
        assertFalse(liste.isEmpty());

        Utilisateur dernier = liste.get(liste.size() - 1);
        Utilisateur charge = utilisateurDAO.getById(dernier.getId());

        assertNotNull(charge);
        assertEquals("Alice", charge.getNom());
        assertEquals(100, charge.getPtsFidelite());
        assertEquals(1111, charge.getCodeAcces());
    }

    @Test
    void testUpdate() throws SQLException {
        List<Utilisateur> liste = utilisateurDAO.getAll();
        assertFalse(liste.isEmpty());

        Utilisateur u = liste.get(0);
        u.setNom("Modifié");
        u.setPtsFidelite(200);
        utilisateurDAO.update(u);

        Utilisateur verif = utilisateurDAO.getById(u.getId());
        assertEquals("Modifié", verif.getNom());
        assertEquals(200, verif.getPtsFidelite());
    }

    @Test
    void testDelete() throws SQLException {
        Utilisateur u = new Utilisateur(0, "Temporaire", 2222);
        u.setPtsFidelite(50);
        utilisateurDAO.insert(u);

        List<Utilisateur> liste = utilisateurDAO.getAll();
        Utilisateur dernier = liste.get(liste.size() - 1);
        int idASupprimer = dernier.getId();

        utilisateurDAO.delete(idASupprimer);
        Utilisateur supprime = utilisateurDAO.getById(idASupprimer);
        assertNull(supprime);
    }

    @Test
    void testGetByCodeAcces() throws SQLException {
        // Nettoyer si déjà existant
        List<Utilisateur> tous = utilisateurDAO.getAll();
        for (Utilisateur u : tous) {
            if (u.getCodeAcces() == 9999) {
                utilisateurDAO.delete(u.getId());
            }
        }

        Utilisateur u = new Utilisateur(0, "Jean", 9999);
        u.setPtsFidelite(88);
        utilisateurDAO.insert(u);

        Utilisateur recupere = utilisateurDAO.getByCodeAcces(9999);
        assertNotNull(recupere);
        assertEquals("Jean", recupere.getNom());
    }

    @Test
    void testUpdateFidelite() throws SQLException {
        Utilisateur u = new Utilisateur(0, "PointsOnly", 1234);
        u.setPtsFidelite(30);
        utilisateurDAO.insert(u);

        Utilisateur inserted = utilisateurDAO.getAll().get(utilisateurDAO.getAll().size() - 1);
        utilisateurDAO.updateFidelite(inserted.getId(), 999);
        Utilisateur updated = utilisateurDAO.getById(inserted.getId());

        assertEquals(999, updated.getPtsFidelite());
    }

    @Test
    void testIsAdmin() throws SQLException {
        // Nettoyer si un "AdminTest" existe
        List<Utilisateur> tous = utilisateurDAO.getAll();
        for (Utilisateur u : tous) {
            if (u.getNom().equals("AdminTest")) {
                utilisateurDAO.delete(u.getId());
            }
        }

        // Ajouter un utilisateur admin
        Utilisateur admin = new Utilisateur(0, "AdminTest", 4321, "admin");
        utilisateurDAO.insert(admin);

        Utilisateur recupere = utilisateurDAO.getByCodeAcces(4321);
        assertNotNull(recupere);
        assertTrue(utilisateurDAO.isAdmin(recupere.getId())); // ✅ doit être admin
    }

    @AfterAll
    static void teardown() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
}