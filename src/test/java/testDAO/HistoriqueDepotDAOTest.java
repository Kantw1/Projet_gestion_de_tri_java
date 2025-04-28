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

public class HistoriqueDepotDAOTest {

    private static Connection conn;
    private static Utilisateur utilisateur;
    private static Poubelle poubelle;
    private static Depot depot;
    private static HistoriqueDepotDAO historiqueDAO;
    private static DepotDAO depotDAO;
    private static UtilisateurDAO utilisateurDAO;
    private static PoubelleDAO poubelleDAO;

    @BeforeAll
    static void setup() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BDD", "root", "");

        historiqueDAO = new HistoriqueDepotDAO(conn);
        utilisateurDAO = new UtilisateurDAO(conn);
        poubelleDAO = new PoubelleDAO(conn);
        depotDAO = new DepotDAO(conn);

        utilisateur = new Utilisateur(0, "HistoriqueTest", 3333);
        utilisateurDAO.insert(utilisateur);
        utilisateur = utilisateurDAO.getAll().get(utilisateurDAO.getAll().size() - 1);

        poubelle = new Poubelle(0, 150, "Rue Historique", TypePoubelle.JAUNE, 40);
        poubelleDAO.insert(poubelle);
        poubelle = poubelleDAO.getAll().get(poubelleDAO.getAll().size() - 1);

        depot = new Depot(0, NatureDechet.VERRE, 2.5f, 4, LocalDateTime.now(), poubelle, utilisateur);
        depotDAO.insert(depot);
        depot = depotDAO.getAll(poubelle, utilisateur).get(depotDAO.getAll(poubelle, utilisateur).size() - 1);
    }

    @Test
    void testInsertAndGet() throws SQLException {
        if (!historiqueDAO.existe(utilisateur.getId(), depot.getId())) {
            historiqueDAO.insert(utilisateur.getId(), depot.getId(), Timestamp.valueOf(depot.getHeureDepot()), depot.getType().name(), depot.getPoints());
        }

        List<Integer> depots = historiqueDAO.getDepotsByUtilisateur(utilisateur.getId());
        assertTrue(depots.contains(depot.getId()));

        List<Integer> utilisateurs = historiqueDAO.getUtilisateursByDepot(depot.getId());
        assertTrue(utilisateurs.contains(utilisateur.getId()));
    }

    @Test
    void testDelete() throws SQLException {
        if (!historiqueDAO.existe(utilisateur.getId(), depot.getId())) {
            historiqueDAO.insert(utilisateur.getId(), depot.getId(), Timestamp.valueOf(depot.getHeureDepot()), depot.getType().name(), depot.getPoints());
        }
        historiqueDAO.delete(utilisateur.getId(), depot.getId());

        List<Integer> depots = historiqueDAO.getDepotsByUtilisateur(utilisateur.getId());
        assertFalse(depots.contains(depot.getId()));
    }

    @AfterAll
    static void teardown() throws SQLException {
        if (conn != null && !conn.isClosed()) conn.close();
    }
}