package test;

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
    static void setUp() throws SQLException {
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BDD", "root", "root");
        utilisateurDAO = new UtilisateurDAO(conn);
    }

    @Test
    void testInsertAndGetById() throws SQLException {
        Utilisateur u = new Utilisateur(0, "Testeur", 12345);
        u.setPtsFidelite(50);
        utilisateurDAO.inserer(u);

        List<Utilisateur> liste = utilisateurDAO.getAll();
        assertFalse(liste.isEmpty(), "La liste des utilisateurs ne doit pas être vide");

        Utilisateur dernier = liste.get(liste.size() - 1);
        Utilisateur charge = utilisateurDAO.getById(dernier.getId());

        assertNotNull(charge, "L'utilisateur récupéré ne doit pas être null");
        assertEquals("Testeur", charge.getNom(), "Le nom doit être 'Testeur'");
    }

    @Test
    void testUpdate() throws SQLException {
        List<Utilisateur> liste = utilisateurDAO.getAll();
        assertFalse(liste.isEmpty(), "Il faut au moins un utilisateur pour le test");

        Utilisateur u = liste.get(0);
        u.setNom("NomModifie");
        utilisateurDAO.update(u);

        Utilisateur charge = utilisateurDAO.getById(u.getId());
        assertNotNull(charge, "L'utilisateur modifié ne doit pas être null");
        assertEquals("NomModifie", charge.getNom(), "Le nom doit être mis à jour");
    }

    @AfterAll
    static void tearDown() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
}
