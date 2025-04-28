package ihm;

import dao.ContratPartenariatDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.CentreDeTri;
import model.ContratPartenariat;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ListeCommercesController {

    @FXML
    private TableView<ContratPartenariat> commerceTable;
    @FXML
    private TableColumn<ContratPartenariat, String> nomCol;
    @FXML
    private TableColumn<ContratPartenariat, String> dateDebutCol;
    @FXML
    private TableColumn<ContratPartenariat, String> dateFinCol;

    private ContratPartenariatDAO contratDAO;
    private CentreDeTri centre;

    public void setCentre(CentreDeTri centre) {
        this.centre = centre;
        try {
            loadContrats();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            contratDAO = new ContratPartenariatDAO(conn);

            nomCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCommerce().getNom()));
            dateDebutCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDateDebut().toString()));
            dateFinCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDateFin().toString()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadContrats() throws SQLException {
        List<ContratPartenariat> contrats = contratDAO.getContratsByCentre(centre);
        ObservableList<ContratPartenariat> observableList = FXCollections.observableArrayList(contrats);
        commerceTable.setItems(observableList);
    }
}
