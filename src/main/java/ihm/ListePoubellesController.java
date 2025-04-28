package ihm;

import dao.PoubelleDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import model.CentreDeTri;
import model.Poubelle;
import model.TypePoubelle;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ListePoubellesController {

    @FXML
    private TableView<Poubelle> poubelleTable;
    @FXML
    private TableColumn<Poubelle, Integer> idCol;
    @FXML
    private TableColumn<Poubelle, String> typeCol;
    @FXML
    private TableColumn<Poubelle, Integer> capaciteCol;
    @FXML
    private TableColumn<Poubelle, String> emplacementCol;
    @FXML
    private TableColumn<Poubelle, Void> actionCol;

    private CentreDeTri centre;
    private PoubelleDAO poubelleDAO;

    private ObservableList<Poubelle> poubelles;

    public void setCentre(CentreDeTri centre) {
        this.centre = centre;
        loadPoubelles();
    }

    private void loadPoubelles() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            poubelleDAO = new PoubelleDAO(conn);

            List<Poubelle> list = poubelleDAO.getPoubellesByCentreId(centre.getId());
            poubelles = FXCollections.observableArrayList(list);

            idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()).asObject());
            typeCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTypePoubelle().name()));
            capaciteCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getCapaciteMax()).asObject());
            emplacementCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEmplacement()));

            addButtonToTable();

            poubelleTable.setItems(poubelles);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addButtonToTable() {
        actionCol.setCellFactory(param -> new TableCell<Poubelle, Void>() {
            private final Button deleteBtn = new Button("Supprimer");

            {
                deleteBtn.setOnAction(event -> {
                    Poubelle p = getTableView().getItems().get(getIndex());
                    try {
                        poubelleDAO.delete(p.getId());
                        loadPoubelles();
                    } catch (SQLException e) {
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
                    setGraphic(new HBox(deleteBtn));
                }
            }
        });
    }

    @FXML
    private void handleAjouterPoubelle() {
        try {
            TextInputDialog typeDialog = new TextInputDialog();
            typeDialog.setTitle("Type de Poubelle");
            typeDialog.setHeaderText("Ajout d'une nouvelle poubelle");
            typeDialog.setContentText("Type de poubelle (JAUNE, VERTE, BLEUE, CLASSIQUE) :");

            typeDialog.showAndWait().ifPresent(typeStr -> {
                TextInputDialog capaciteDialog = new TextInputDialog();
                capaciteDialog.setTitle("Capacité");
                capaciteDialog.setHeaderText("Ajout d'une nouvelle poubelle");
                capaciteDialog.setContentText("Capacité maximale (en kg) :");

                capaciteDialog.showAndWait().ifPresent(capStr -> {
                    TextInputDialog emplacementDialog = new TextInputDialog();
                    emplacementDialog.setTitle("Emplacement");
                    emplacementDialog.setHeaderText("Ajout d'une nouvelle poubelle");
                    emplacementDialog.setContentText("Emplacement :");

                    emplacementDialog.showAndWait().ifPresent(emplacement -> {
                        try {
                            int capacite = Integer.parseInt(capStr.trim());
                            TypePoubelle type = TypePoubelle.valueOf(typeStr.trim().toUpperCase());

                            Poubelle p = new Poubelle(0, capacite, emplacement, type, 80, centre); // seuilAlerte par défaut 80%
                            Connection conn = DatabaseConnection.getConnection();
                            PoubelleDAO dao = new PoubelleDAO(conn);
                            dao.insert(p, centre.getId());

                            loadPoubelles();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                });
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
