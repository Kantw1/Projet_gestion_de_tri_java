package ihm;

import dao.ContratPartenariatDAO;
import dao.CommerceCategorieProduitDAO;
import dao.CategorieProduitDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
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
    @FXML
    private TableColumn<ContratPartenariat, Void> actionCol; // Ajout d'une colonne pour les boutons

    private ContratPartenariatDAO contratDAO;
    private CentreDeTri centre;

    @FXML
    private void initialize() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            contratDAO = new ContratPartenariatDAO(conn);

            nomCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCommerce().getNom()));
            dateDebutCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDateDebut().toString()));
            dateFinCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDateFin().toString()));

            addButtonToTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setCentre(CentreDeTri centre) {
        this.centre = centre;
        try {
            loadContrats();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadContrats() throws SQLException {
        List<ContratPartenariat> contrats = contratDAO.getContratsByCentre(centre);
        ObservableList<ContratPartenariat> observableList = FXCollections.observableArrayList(contrats);
        commerceTable.setItems(observableList);
    }

    private void addButtonToTable() {
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button voirCategoriesBtn = new Button("Voir Catégories");

            {
                voirCategoriesBtn.setOnAction(event -> {
                    ContratPartenariat contrat = getTableView().getItems().get(getIndex());
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ListeCategoriesView.fxml"));
                        Parent root = loader.load();

                        ListeCategoriesController controller = loader.getController();
                        controller.setCommerce(contrat.getCommerce());

                        Stage stage = new Stage();
                        stage.setTitle("Catégories du commerce " + contrat.getCommerce().getNom());
                        stage.setScene(new Scene(root));
                        stage.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(voirCategoriesBtn);
                }
            }
        });
    }
}
