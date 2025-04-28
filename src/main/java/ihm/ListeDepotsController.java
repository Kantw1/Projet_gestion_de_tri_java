package ihm;

import dao.DepotDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Depot;
import model.Utilisateur;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ListeDepotsController {

    @FXML
    private TableView<Depot> depotTable;
    @FXML
    private TableColumn<Depot, Integer> idDepotCol;
    @FXML
    private TableColumn<Depot, String> typeCol;
    @FXML
    private TableColumn<Depot, Float> poidsCol;
    @FXML
    private TableColumn<Depot, Integer> quantiteCol;
    @FXML
    private TableColumn<Depot, Integer> pointsCol;
    @FXML
    private TableColumn<Depot, String> dateCol;

    private ObservableList<Depot> depots;

    private Utilisateur utilisateur;

    private DepotDAO depotDAO;

    /**
     * Appelé par le contrôleur parent pour fixer l'utilisateur dont on veut voir les dépôts.
     */
    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        loadDepots();
    }

    private void loadDepots() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            depotDAO = new DepotDAO(conn);

            List<Depot> list = depotDAO.getByUtilisateurId(utilisateur.getId());
            depots = FXCollections.observableArrayList(list);

            idDepotCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()).asObject());
            typeCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getType().name()));
            poidsCol.setCellValueFactory(data -> new javafx.beans.property.SimpleFloatProperty(data.getValue().getPoids()).asObject());
            quantiteCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getQuantite()).asObject());
            pointsCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getPoints()).asObject());
            dateCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                    data.getValue().getDateDepot().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
            ));

            depotTable.setItems(depots);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
