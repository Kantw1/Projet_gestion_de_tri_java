package testDAO;

import dao.*;
import model.*;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HistoriqueDepotDAOTest {

    private Connection conn;
    private Utilisateur utilisateur;
    private Poubelle poubelle;
    private Depot depot;
    private HistoriqueDepotDAO historiqueDAO;
    private DepotDAO depotDAO;
    private UtilisateurDAO utilisateurDAO;
    private PoubelleDAO poubelleDAO;
    private CentreDeTri centre;

    @BeforeAll
    void setup() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BDD", "root", "");

        historiqueDAO = new HistoriqueDepotDAO(conn);
        utilisateurDAO = new UtilisateurDAO(conn);
        poubelleDAO = new PoubelleDAO(conn);
        depotDAO = new DepotDAO(conn);

        // Centre de tri
        CentreDeTriDAO centreDAO = new CentreDeTriDAO(conn);
        centre = new CentreDeTri(0, "Centre Historique", "Rue Historique");
        centreDAO.insert(centre);
        centre = centreDAO.getAll().get(centreDAO.getAll().size() - 1);

        // Utilisateur
        utilisateur = new Utilisateur(0, "HistoriqueTest", 3333, centre.getId());
        utilisateurDAO.insert(utilisateur);
        utilisateur = utilisateurDAO.getAll().get(utilisateurDAO.getAll().size() - 1);

        // Poubelle
        poubelle = new Poubelle(0, 150, "Rue Historique", TypePoubelle.JAUNE, 40, 90, centre);
        poubelleDAO.insert(poubelle, centre.getId());
        poubelle = poubelleDAO.getAll().get(poubelleDAO.getAll().size() - 1);

        // Dépot
        depot = new Depot(0, NatureDechet.VERRE, 2.5f, 4, LocalDateTime.now(), poubelle, utilisateur);
        depotDAO.insert(depot);
        depot = depotDAO.getByPoubelleId(poubelle.getId()).get(depotDAO.getByPoubelleId(poubelle.getId()).size() - 1);
    }

    @Test
    void testInsertAndGet() throws SQLException {
        if (!historiqueDAO.existe(utilisateur.getId(), depot.getId())) {
            historiqueDAO.insert(
                    utilisateur.getId(),
                    depot.getId(),
                    Timestamp.valueOf(depot.getHeureDepot()),
                    depot.getType().name(),
                    depot.getPoints()
            );
        }

        List<Integer> depots = historiqueDAO.getDepotsByUtilisateur(utilisateur.getId());
        assertTrue(depots.contains(depot.getId()), "Le dépôt doit apparaître dans l'historique utilisateur");

        List<Integer> utilisateurs = historiqueDAO.getUtilisateursByDepot(depot.getId());
        assertTrue(utilisateurs.contains(utilisateur.getId()), "L'utilisateur doit apparaître dans l'historique du dépôt");
    }

    @Test
    void testDelete() throws SQLException {
        if (!historiqueDAO.existe(utilisateur.getId(), depot.getId())) {
            historiqueDAO.insert(
                    utilisateur.getId(),
                    depot.getId(),
                    Timestamp.valueOf(depot.getHeureDepot()),
                    depot.getType().name(),
                    depot.getPoints()
            );
        }

        historiqueDAO.delete(utilisateur.getId(), depot.getId());

        List<Integer> depots = historiqueDAO.getDepotsByUtilisateur(utilisateur.getId());
        assertFalse(depots.contains(depot.getId()), "Le dépôt doit être supprimé de l'historique utilisateur");
    }

    @AfterAll
    void teardown() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
}
