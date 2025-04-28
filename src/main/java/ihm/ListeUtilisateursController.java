package ihm;

import dao.UtilisateurDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Utilisateur;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ListeUtilisateursController {
    @FXML
    private void handleRetour() {
        Stage stage = (Stage) utilisateurTable.getScene().getWindow();
        stage.close();
    }
    @FXML
    private TableView<Utilisateur> utilisateurTable;
    @FXML
    private TableColumn<Utilisateur, Integer> idCol;
    @FXML
    private TableColumn<Utilisateur, String> nomCol;
    @FXML
    private TableColumn<Utilisateur, Integer> ptsCol;
    @FXML
    private TableColumn<Utilisateur, Integer> codeAccesCol;
    @FXML
    private TableColumn<Utilisateur, String> roleCol;
    @FXML
    private TableColumn<Utilisateur, Void> actionCol;

    private ObservableList<Utilisateur> utilisateurs;
    private UtilisateurDAO utilisateurDAO;

    @FXML
    private void initialize() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            utilisateurDAO = new UtilisateurDAO(conn);

            idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()).asObject());
            nomCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNom()));
            ptsCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getPtsFidelite()).asObject());
            codeAccesCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getCodeAcces()).asObject());
            roleCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getRole()));

            loadUtilisateurs();
            addButtonToTable();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadUtilisateurs() throws SQLException {
        List<Utilisateur> list = utilisateurDAO.getAll();
        utilisateurs = FXCollections.observableArrayList(list);
        utilisateurTable.setItems(utilisateurs);
    }

    private void addButtonToTable() {
        actionCol.setCellFactory(param -> new TableCell<Utilisateur, Void>() {
            private final Button deleteBtn = new Button("Supprimer");
            private final Button voirDepotsBtn = new Button("Voir Dépôts");
            private final HBox pane = new HBox(10, voirDepotsBtn, deleteBtn);

            {
                deleteBtn.setOnAction(event -> {
                    Utilisateur utilisateur = getTableView().getItems().get(getIndex());
                    try {
                        utilisateurDAO.delete(utilisateur.getId());
                        loadUtilisateurs();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });

                voirDepotsBtn.setOnAction(event -> {
                    Utilisateur utilisateur = getTableView().getItems().get(getIndex());
                    if (!"admin".equalsIgnoreCase(utilisateur.getRole())) {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ListeDepotsView.fxml"));
                            Parent root = loader.load();

                            ListeDepotsController controller = loader.getController();
                            controller.setUtilisateur(utilisateur);

                            Stage stage = new Stage();
                            stage.setTitle("Dépôts de " + utilisateur.getNom());
                            stage.setScene(new Scene(root));
                            stage.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Action interdite");
                        alert.setHeaderText(null);
                        alert.setContentText("Impossible de voir les dépôts d'un administrateur !");
                        alert.showAndWait();
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
