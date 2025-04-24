package testDAO;

import dao.CentreDeTriDAO;
import dao.CommerceDAO;
import dao.ContratPartenariatDAO;
import model.CentreDeTri;
import model.Commerce;
import model.ContratPartenariat;

import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ContratPartenariatDAOTest {

    private static Connection conn;
    private static ContratPartenariatDAO contratDAO;
    private static CentreDeTri centre;
    private static Commerce commerce;

    @BeforeAll
    static void setup() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BDD", "root", "");

        CentreDeTriDAO centreDAO = new CentreDeTriDAO(conn);
        centre = new CentreDeTri(0, "Centre Test", "Zone 51");
        centreDAO.insert(centre);
        centre = centreDAO.getAll().get(centreDAO.getAll().size() - 1);

        CommerceDAO commerceDAO = new CommerceDAO(conn);
        commerce = new Commerce(0, "Boulanger", centre);
        commerceDAO.insert(commerce);
        commerce = commerceDAO.getAll(centre).get(commerceDAO.getAll(centre).size() - 1);

        contratDAO = new ContratPartenariatDAO(conn);
    }

    @Test
    void testInsertAndGetById() throws SQLException {
        ContratPartenariat contrat = new ContratPartenariat(0,
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2026, 12, 31),
                centre, commerce);
        contratDAO.insert(contrat);

        List<ContratPartenariat> tous = contratDAO.getAll(centre, commerce);
        assertFalse(tous.isEmpty());

        ContratPartenariat dernier = tous.get(tous.size() - 1);
        ContratPartenariat charge = contratDAO.getById(dernier.getId(), centre, commerce);

        assertNotNull(charge);
        assertEquals(centre.getId(), charge.getCentre().getId());
        assertEquals(commerce.getId(), charge.getCommerce().getId());
    }

    @Test
    void testUpdate() throws SQLException {
        List<ContratPartenariat> liste = contratDAO.getAll(centre, commerce);
        assertFalse(liste.isEmpty());

        ContratPartenariat contrat = liste.get(0);
        ContratPartenariat modifie = new ContratPartenariat(
                contrat.getId(),
                LocalDate.of(2023, 3, 1),
                LocalDate.of(2026, 3, 1),
                centre, commerce
        );
        contratDAO.update(modifie);

        ContratPartenariat verif = contratDAO.getById(modifie.getId(), centre, commerce);
        assertEquals(LocalDate.of(2023, 3, 1), verif.getDateDebut());
        assertEquals(LocalDate.of(2026, 3, 1), verif.getDateFin());
    }

    @Test
    void testDelete() throws SQLException {
        ContratPartenariat contrat = new ContratPartenariat(0,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2026, 1, 1),
                centre, commerce);
        contratDAO.insert(contrat);

        List<ContratPartenariat> liste = contratDAO.getAll(centre, commerce);
        ContratPartenariat dernier = liste.get(liste.size() - 1);
        int idASupprimer = dernier.getId();

        contratDAO.delete(idASupprimer);
        ContratPartenariat supprime = contratDAO.getById(idASupprimer, centre, commerce);
        assertNull(supprime);
    }

    @AfterAll
    static void teardown() throws SQLException {
        if (conn != null && !conn.isClosed()) conn.close();
    }
}
