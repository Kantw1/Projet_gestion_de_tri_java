package testDAO;

import dao.*;
import model.*;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DepotDAOTest {

    private Connection conn;
    private DepotDAO depotDAO;
    private Utilisateur utilisateur;
    private Poubelle poubelle;
    private UtilisateurDAO utilisateurDAO;
    private PoubelleDAO poubelleDAO;
    private CentreDeTri centre;

    @BeforeAll
    void setUp() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BDD", "root", "");

        CentreDeTriDAO centreDAO = new CentreDeTriDAO(conn);
        centre = new CentreDeTri(0, "Centre Dépôt Test", "Adresse Test");
        centreDAO.insert(centre);
        centre = centreDAO.getAll().get(centreDAO.getAll().size() - 1);

        utilisateurDAO = new UtilisateurDAO(conn);
        utilisateur = new Utilisateur(0, "Déposeur", 9999, centre.getId());
        utilisateur.setPtsFidelite(0);
        utilisateurDAO.insert(utilisateur);
        utilisateur = utilisateurDAO.getAll().get(utilisateurDAO.getAll().size() - 1);

        poubelleDAO = new PoubelleDAO(conn);
        poubelle = new Poubelle(0, 100, "TestQuartier", TypePoubelle.JAUNE, 70, 90, centre);
        poubelleDAO.insert(poubelle, centre.getId());
        poubelle = poubelleDAO.getAll().get(poubelleDAO.getAll().size() - 1);

        depotDAO = new DepotDAO(conn);
    }

    @Test
    @Order(1)
    void testInsertAndGet() throws SQLException {
        Depot d = new Depot(0, NatureDechet.PLASTIQUE, 1.5f, 3, LocalDateTime.now(), poubelle, utilisateur);
        depotDAO.insert(d);

        List<Depot> depots = depotDAO.getByPoubelleId(poubelle.getId());
        assertTrue(depots.stream().anyMatch(dep -> dep.getId() == d.getId()), "Le dépôt doit exister");
    }

    @Test
    @Order(2)
    void testReInsertInsteadOfUpdate() throws SQLException {
        // Puisqu'on n'a pas de setPoids() ni setQuantite(), on recrée un nouveau dépôt avec les nouvelles valeurs
        Depot d = new Depot(0, NatureDechet.PAPIER, 1.0f, 2, LocalDateTime.now(), poubelle, utilisateur);
        depotDAO.insert(d);

        // Suppression de l'ancien dépôt et insertion d'un nouveau "mis à jour"
        depotDAO.delete(d.getId());

        Depot nouveauDepot = new Depot(0, NatureDechet.PAPIER, 2.0f, 5, LocalDateTime.now(), poubelle, utilisateur);
        depotDAO.insert(nouveauDepot);

        List<Depot> depots = depotDAO.getByPoubelleId(poubelle.getId());
        assertTrue(depots.stream().anyMatch(dep -> dep.getPoids() == 2.0f && dep.getQuantite() == 5), "Le nouveau dépôt doit avoir les valeurs mises à jour");
    }

    @Test
    @Order(3)
    void testDelete() throws SQLException {
        Depot d = new Depot(0, NatureDechet.VERRE, 1.0f, 2, LocalDateTime.now(), poubelle, utilisateur);
        depotDAO.insert(d);

        int id = d.getId();
        depotDAO.delete(id);

        List<Depot> depots = depotDAO.getByPoubelleId(poubelle.getId());
        boolean exists = depots.stream().anyMatch(dep -> dep.getId() == id);
        assertFalse(exists, "Le dépôt doit être supprimé");
    }

    @Test
    @Order(4)
    void testGetByPoubelleId() throws SQLException {
        Depot d = new Depot(0, NatureDechet.METAL, 2.5f, 4, LocalDateTime.now(), poubelle, utilisateur);
        depotDAO.insert(d);

        List<Depot> liste = depotDAO.getByPoubelleId(poubelle.getId());
        assertTrue(liste.stream().anyMatch(dep -> dep.getId() == d.getId()));
    }

    @Test
    @Order(5)
    void testGetByUtilisateurId() throws SQLException {
        Depot d = new Depot(0, NatureDechet.VERRE, 3.0f, 5, LocalDateTime.now(), poubelle, utilisateur);
        depotDAO.insert(d);

        List<Depot> liste = depotDAO.getByUtilisateurId(utilisateur.getId());
        assertTrue(liste.stream().anyMatch(dep -> dep.getId() == d.getId()));
    }

    @Test
    @Order(6)
    void testGetByDateRange() throws SQLException {
        LocalDateTime now = LocalDateTime.now();
        Depot d = new Depot(0, NatureDechet.CARTON, 1.2f, 2, now, poubelle, utilisateur);
        depotDAO.insert(d);

        List<Depot> liste = depotDAO.getByDateRange(now.minusMinutes(5), now.plusMinutes(5));
        assertTrue(liste.stream().anyMatch(dep -> dep.getId() == d.getId()));
    }

    @Test
    @Order(7)
    void testGetTotalPoidsByPoubelle() throws SQLException {
        Depot d = new Depot(0, NatureDechet.PLASTIQUE, 1.5f, 3, LocalDateTime.now(), poubelle, utilisateur);
        depotDAO.insert(d);

        float totalPoids = depotDAO.getTotalPoidsByPoubelle(poubelle.getId());
        assertTrue(totalPoids >= d.getPoids(), "Le poids total doit être au moins égal au poids inséré");
    }

    @AfterAll
    void tearDown() throws SQLException {
        if (depotDAO != null) {
            List<Depot> allDepots = depotDAO.getByUtilisateurId(utilisateur.getId());
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
