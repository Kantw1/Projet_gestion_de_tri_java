package ihm;

import dao.DepotDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Depot;
import model.Utilisateur;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ListeHistoriqueController {

    @FXML
    private TableView<Depot> historiqueTable;
    @FXML
    private TableColumn<Depot, String> dateCol;
    @FXML
    private TableColumn<Depot, String> typeCol;
    @FXML
    private TableColumn<Depot, Integer> pointsCol;
    @FXML
    private TableColumn<Depot, String> utilisateurCol;

    private int poubelleId;

    public void setPoubelleId(int poubelleId) {
        this.poubelleId = poubelleId;
        loadHistorique();
    }

    private void loadHistorique() {
        try {
            // Crée la connexion à la base de données
            Connection conn = DatabaseConnection.getConnection();
            DepotDAO dao = new DepotDAO(conn);

            // Utilise l'ID de l'utilisateur pour récupérer son historique de dépôts
            List<Depot> depots = dao.getByUtilisateurId(utilisateurId); // Récupère les dépôts pour l'utilisateur

            // Crée une ObservableList pour afficher les dépôts dans la TableView
            ObservableList<Depot> data = FXCollections.observableArrayList(depots);

            // Formate la colonne "date"
            dateCol.setCellValueFactory(cellData ->
                    new javafx.beans.property.SimpleStringProperty(
                            cellData.getValue().getHeureDepot().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                    )
            );

            // Formate la colonne "type" (type de déchet)
            typeCol.setCellValueFactory(cellData ->
                    new javafx.beans.property.SimpleStringProperty(cellData.getValue().getType().name())
            );

            // Formate la colonne "points" (points attribués)
            pointsCol.setCellValueFactory(cellData ->
                    new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getPoints()).asObject()
            );

            // Formate la colonne "utilisateur" (nom de l'utilisateur)
            utilisateurCol.setCellValueFactory(cellData -> {
                Utilisateur utilisateur = cellData.getValue().getUtilisateur();
                if (utilisateur != null) {
                    return new javafx.beans.property.SimpleStringProperty(utilisateur.getNom()); // Affiche le nom de l'utilisateur
                } else {
                    return new javafx.beans.property.SimpleStringProperty("Inconnu");
                }
            });

            // Ajoute les données à la TableView
            historiqueTable.setItems(data);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private int utilisateurId;

    public void setUtilisateurId(int utilisateurId) {
        this.utilisateurId = utilisateurId;
        loadHistorique();  // Charge l'historique dès qu'on a l'ID de l'utilisateur
    }

}
