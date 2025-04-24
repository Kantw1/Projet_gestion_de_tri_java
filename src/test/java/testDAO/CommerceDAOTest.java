package testDAO;

import dao.CentreDeTriDAO;
import dao.CommerceDAO;
import model.CentreDeTri;
import model.Commerce;

import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CommerceDAOTest {

    private static Connection conn;
    private static CentreDeTri centre;
    private static CommerceDAO commerceDAO;

    @BeforeAll
    static void setup() throws Exception {
        // Charger le driver JDBC MySQL
        Class.forName("com.mysql.cj.jdbc.Driver");

        // Connexion à la base
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BDD", "root", "");

        // Insérer un CentreDeTri de référence
        CentreDeTriDAO centreDAO = new CentreDeTriDAO(conn);
        centre = new CentreDeTri(0, "Centre Commerce", "Zone A");
        centreDAO.insert(centre);
        centre = centreDAO.getAll().get(centreDAO.getAll().size() - 1); // dernier inséré

        commerceDAO = new CommerceDAO(conn);
    }

    @Test
    void testInsertAndGet() throws SQLException {
        // Créer un commerce et l’insérer
        Commerce c = new Commerce(0, "Leclerc", centre);
        commerceDAO.insert(c);

        // Récupérer la liste des commerces et tester
        List<Commerce> liste = commerceDAO.getAll(centre);
        assertFalse(liste.isEmpty(), "La liste des commerces ne doit pas être vide");

        Commerce dernier = liste.get(liste.size() - 1);
        assertEquals("Leclerc", dernier.getNom(), "Le nom du commerce doit être 'Leclerc'");
    }

    @Test
    void testUpdate() throws SQLException {
        List<Commerce> liste = commerceDAO.getAll(centre);
        assertFalse(liste.isEmpty(), "Il faut au moins un commerce en base");

        Commerce c = liste.get(0);
        Commerce modifie = new Commerce(c.getId(), "Auchan", centre);
        commerceDAO.update(modifie);

        Commerce verif = commerceDAO.getById(modifie.getId(), centre);
        assertEquals("Auchan", verif.getNom(), "Le nom du commerce doit être mis à jour en 'Auchan'");
    }

    @AfterAll
    static void teardown() throws SQLException {
        if (conn != null && !conn.isClosed()) conn.close();
    }

    @Test
    void testDelete() throws SQLException {
        Commerce c = new Commerce(0, "Temporaire", centre);
        commerceDAO.insert(c);

        List<Commerce> liste = commerceDAO.getAll(centre);
        Commerce dernier = liste.get(liste.size() - 1);
        int idASupprimer = dernier.getId();

        commerceDAO.delete(idASupprimer);  // à implémenter si ce n’est pas encore fait
        Commerce supprime = commerceDAO.getById(idASupprimer, centre);

        assertNull(supprime, "Le commerce doit avoir été supprimé de la base");
    }

}
