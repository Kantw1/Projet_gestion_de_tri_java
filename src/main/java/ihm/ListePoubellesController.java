package ihm;

import dao.PoubelleDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
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
    private TableColumn<Poubelle, Integer> quantiteCol;
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
            quantiteCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getQuantiteActuelle()).asObject());
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
            private final Button viderBtn = new Button("Appeler Entreprise");
            private final Button voirHistoriqueBtn = new Button("Voir Historique");

            private final HBox pane = new HBox(10, viderBtn, voirHistoriqueBtn, deleteBtn);

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

                viderBtn.setOnAction(event -> {
                    Poubelle p = getTableView().getItems().get(getIndex());
                    try {
                        p.setQuantiteActuelle(0);
                        poubelleDAO.update(p);
                        loadPoubelles();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });

                // ✨ Ton code ici pour Voir l'Historique
                voirHistoriqueBtn.setOnAction(event -> {
                    Poubelle p = getTableView().getItems().get(getIndex());
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ListeHistoriqueView.fxml"));
                        Parent root = loader.load();

                        ListeHistoriqueController controller = loader.getController();
                        controller.setPoubelleId(p.getId());

                        Stage stage = new Stage();
                        stage.setTitle("Historique de la Poubelle #" + p.getId());
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

                            // 🔥 Quand on ajoute une poubelle, quantité actuelle = 0
                            Poubelle p = new Poubelle(
                                    0,
                                    capacite,
                                    emplacement,
                                    type,
                                    0, // quantiteActuelle = 0 car nouvelle poubelle
                                    80, // seuilAlerte à 80% par défaut
                                    centre
                            );

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
