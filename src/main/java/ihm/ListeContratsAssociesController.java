package ihm;

import dao.ContratPartenariatDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Commerce;
import model.ContratPartenariat;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import dao.ContratPartenariatDAO;

public class ListeContratsAssociesController {

    @FXML
    private TableView<ContratPartenariat> contratTable;
    @FXML
    private TableColumn<ContratPartenariat, Integer> idCol;
    @FXML
    private TableColumn<ContratPartenariat, String> dateDebutCol;
    @FXML
    private TableColumn<ContratPartenariat, String> dateFinCol;

    private ContratPartenariatDAO contratDAO;
    private Commerce commerce;

    @FXML
    private void initialize() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            contratDAO = new ContratPartenariatDAO(conn);

            idCol.setCellValueFactory(data ->
                    new javafx.beans.property.SimpleIntegerProperty(
                            data.getValue().getCentre().getId() // récupère l'ID du centre de tri
                    ).asObject()
            );

            dateDebutCol.setCellValueFactory(data ->
                    new javafx.beans.property.SimpleStringProperty(data.getValue().getDateDebut().toString())
            );
            dateFinCol.setCellValueFactory(data ->
                    new javafx.beans.property.SimpleStringProperty(data.getValue().getDateFin().toString())
            );

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setCommerce(Commerce commerce) {
        this.commerce = commerce;
        loadContrats();
    }

    private void loadContrats() {
        try {
            List<ContratPartenariat> contrats = contratDAO.getContratsByCommerce(commerce);
            ObservableList<ContratPartenariat> contratsList = FXCollections.observableArrayList(contrats);
            contratTable.setItems(contratsList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
