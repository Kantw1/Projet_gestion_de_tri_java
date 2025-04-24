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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CentrePoubelleDAOTest {

    private static Connection conn;
    private static CentrePoubelleDAO cpDAO;
    private static CentreDeTri centre;
    private static Poubelle poubelle;

    @BeforeAll
    static void setup() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BDD", "root", "");
        cpDAO = new CentrePoubelleDAO(conn);

        CentreDeTriDAO centreDAO = new CentreDeTriDAO(conn);
        centre = new CentreDeTri(0, "Centre Test CP", "Rue des Tests");
        centreDAO.insert(centre);
        centre = centreDAO.getAll().get(centreDAO.getAll().size() - 1);

        PoubelleDAO poubelleDAO = new PoubelleDAO(conn);
        poubelle = new Poubelle(0, 120, "Zone CP", TypePoubelle.JAUNE, 30);
        poubelleDAO.insert(poubelle);
        poubelle = poubelleDAO.getAll().get(poubelleDAO.getAll().size() - 1);
    }

    @Test
    void testInsertAndGet() throws Exception {
        cpDAO.insert(centre.getId(), poubelle.getId());

        List<Integer> poubelles = cpDAO.getPoubellesByCentre(centre.getId());
        assertTrue(poubelles.contains(poubelle.getId()));

        List<Integer> centres = cpDAO.getCentresByPoubelle(poubelle.getId());
        assertTrue(centres.contains(centre.getId()));
    }

    @Test
    void testDelete() throws Exception {
        // Vérifie si la relation n'existe pas déjà pour éviter le duplicata
        if (!cpDAO.exists(centre.getId(), poubelle.getId())) {
            cpDAO.insert(centre.getId(), poubelle.getId());
        }

        // Suppression de l'association
        cpDAO.delete(centre.getId(), poubelle.getId());

        // Vérification que la suppression est effective
        List<Integer> poubelles = cpDAO.getPoubellesByCentre(centre.getId());
        assertFalse(poubelles.contains(poubelle.getId()), "La poubelle ne devrait plus être liée au centre");

        List<Integer> centres = cpDAO.getCentresByPoubelle(poubelle.getId());
        assertFalse(centres.contains(centre.getId()), "Le centre ne devrait plus être lié à la poubelle");
    }
    @AfterAll
    static void teardown() throws Exception {
        if (conn != null && !conn.isClosed()) conn.close();
    }


}
