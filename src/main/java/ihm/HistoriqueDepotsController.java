package ihm;

import dao.DepotDAO;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Depot;
import model.Utilisateur;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.util.List;

public class HistoriqueDepotsController {

    @FXML
    private TableView<Depot> tableDepots;

    @FXML
    private TableColumn<Depot, String> colType;

    @FXML
    private TableColumn<Depot, Float> colPoids;

    @FXML
    private TableColumn<Depot, Integer> colQuantite;

    @FXML
    private TableColumn<Depot, java.time.LocalDateTime> colDate;

    private Utilisateur utilisateurConnecte;

    /**
     * Setter pour recevoir l'utilisateur connecté.
     */
    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateurConnecte = utilisateur;
        chargerHistorique();
    }

    @FXML
    public void initialize() {
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colPoids.setCellValueFactory(new PropertyValueFactory<>("poids"));
        colQuantite.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("heureDepot"));
    }

    private void chargerHistorique() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            DepotDAO depotDAO = new DepotDAO(conn);
            List<Depot> depots = depotDAO.getByUtilisateurId(utilisateurConnecte.getId());
            tableDepots.getItems().setAll(depots);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
