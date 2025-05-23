package ihm;

import dao.CentreDeTriDAO;
import dao.CommerceDAO;
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
import model.Commerce;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ListeCentresController {
    @FXML
    private void handleRetour(javafx.event.ActionEvent event) {
        try {
            // Charger la nouvelle vue AdminView.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AdminView.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène avec la vue chargée
            Scene scene = new Scene(root);
            scene.getStylesheets().add(HelloApplication.class.getResource("/style.css").toExternalForm());



            // Créer un nouveau stage pour afficher la nouvelle vue
            Stage stage = new Stage();
            stage.setTitle("Page Admin");
            stage.setScene(scene);


            // Fermer la fenêtre actuelle
            Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            currentStage.close();

            // Afficher la nouvelle fenêtre
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private TableView<CentreDeTri> centreTable;
    @FXML
    private TableColumn<CentreDeTri, Integer> idCol;
    @FXML
    private TableColumn<CentreDeTri, String> nomCol;
    @FXML
    private TableColumn<CentreDeTri, String> adresseCol;
    @FXML
    private TableColumn<CentreDeTri, Void> actionCol;

    private ObservableList<CentreDeTri> centres;
    private CentreDeTriDAO centreDAO;
    private CommerceDAO commerceDAO;

    @FXML
    private void handleAjouterCentre() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Ajouter un Centre de Tri");
        dialog.setHeaderText("Créer un nouveau centre de tri");
        dialog.setContentText("Entrez le nom du centre :");

        dialog.showAndWait().ifPresent(nom -> {
            TextInputDialog adresseDialog = new TextInputDialog();
            adresseDialog.setTitle("Adresse du Centre");
            adresseDialog.setHeaderText("Créer un nouveau centre de tri");
            adresseDialog.setContentText("Entrez l'adresse du centre :");

            adresseDialog.showAndWait().ifPresent(adresse -> {
                try {
                    CentreDeTri nouveauCentre = new CentreDeTri(0, nom, adresse);
                    centreDAO.insert(nouveauCentre);
                    loadCentres(); // Recharge la liste après ajout
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        });
    }

    @FXML
    private void initialize() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            centreDAO = new CentreDeTriDAO(conn);
            commerceDAO = new CommerceDAO(conn);

            idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()).asObject());
            nomCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNom()));
            adresseCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getAdresse()));

            loadCentres();
            addButtonToTable();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadCentres() throws SQLException {
        List<CentreDeTri> list = centreDAO.getAll();
        centres = FXCollections.observableArrayList(list);
        centreTable.setItems(centres);
    }

    private void addButtonToTable() {
        actionCol.setCellFactory(param -> new TableCell<CentreDeTri, Void>() {
            private final Button deleteBtn = new Button("Supprimer");
            private final Button voirPoubellesBtn = new Button("Voir Poubelles");
            private final Button voirCommercesBtn = new Button("Voir Commerces");
            private final HBox pane = new HBox(10, voirPoubellesBtn, voirCommercesBtn, deleteBtn);

            {
                deleteBtn.setOnAction(event -> {
                    CentreDeTri centre = getTableView().getItems().get(getIndex());
                    try {
                        centreDAO.delete(centre.getId());
                        loadCentres();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });

                voirPoubellesBtn.setOnAction(event -> {
                    CentreDeTri centre = getTableView().getItems().get(getIndex());
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ListePoubellesView.fxml"));
                        Parent root = loader.load();

                        ListePoubellesController controller = loader.getController();
                        controller.setCentre(centre);

                        Stage stage = new Stage();
                        stage.setTitle("Poubelles du Centre " + centre.getNom());
                        stage.setScene(new Scene(root));
                        stage.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                voirCommercesBtn.setOnAction(event -> {
                    CentreDeTri centre = getTableView().getItems().get(getIndex());
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ListeCommercesView.fxml"));
                        Parent root = loader.load();

                        ListeCommercesController controller = loader.getController();
                        controller.setCentre(centre);

                        Stage stage = new Stage();
                        stage.setTitle("Commerces du Centre " + centre.getNom());
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
                    setGraphic(pane);
                }
            }
        });
    }
}
