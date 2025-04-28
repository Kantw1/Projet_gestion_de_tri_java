package ihm;

import dao.BonDeCommandeDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.BonDeCommande;
import model.Utilisateur;
import utils.DatabaseConnection;


import java.sql.Connection;
import java.util.List;

public class ListeBonsController {

    @FXML
    private TableView<BonDeCommande> tableBons;

    @FXML
    private TableColumn<BonDeCommande, String> colCategorieProduit;

    @FXML
    private TableColumn<BonDeCommande, String> colCommerce;

    @FXML
    private TableColumn<BonDeCommande, Integer> colPointsUtilises;

    @FXML
    private TableColumn<BonDeCommande, String> colDateCommande;

    private Utilisateur utilisateurConnecte;

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateurConnecte = utilisateur;
        chargerBons();
    }

    @FXML
    public void initialize() {
        colCategorieProduit.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getNomCategorieProduit())
        );
        colPointsUtilises.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(data.getValue().getPointsUtilises()).asObject()
        );
        colDateCommande.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getDateCommande().toString())
        );
    }
    private void chargerBons() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            BonDeCommandeDAO bonDAO = new BonDeCommandeDAO(conn);
            List<BonDeCommande> bons = bonDAO.getByUtilisateurId(utilisateurConnecte.getId());
            tableBons.getItems().setAll(bons);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRetour() {
        Stage stage = (Stage) tableBons.getScene().getWindow();
        stage.close();
    }
}
